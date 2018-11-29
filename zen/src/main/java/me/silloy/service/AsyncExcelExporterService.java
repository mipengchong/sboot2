package me.silloy.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import me.silloy.common.RedisKeys;
import me.silloy.domain.AsyncExportExcel;
import me.silloy.domain.AsyncExportProp;
import me.silloy.enums.AsyncExportExcelStatusEnums;
import me.silloy.exception.BizException;
import me.silloy.util.ExcelExporter;
import me.silloy.util.IdUtil;
import me.silloy.util.ValidationUtil;
import me.silloy.util.qiniuUtils.ImageHandler;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class AsyncExcelExporterService {

    @Resource
    RedisTemplate<String, AsyncExportProp> redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ApplicationContext context;
//
//    @Autowired
//    AsyncExportExcelMapper exportExcelMapper;
//    @Autowired
//    AsyncExportExcelExtMapper exportExcelExtMapper;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");

    /**
     * 添加异步导出任务
     *
     * @param userName  导出用户姓名
     * @param modelName 导出模块名称
     * @param clazz     导出数据源所在类
     * @param method    导出数据源所在方法
     * @param args      导出数据源所需参数
     * @param titles    Excel 列头配置
     * @param fields    Excel 列头与数据对应关系
     * @return
     */
    public boolean addTask(String userName, String modelName, Class clazz, String method, Object[] args, String[] titles, String[] fields) {
        AsyncExportProp prop = new AsyncExportProp();
        prop.setUserName(userName);
        prop.setModelName(modelName);
        prop.setClazz(clazz);
        prop.setMethodName(method);
        prop.setArgs(args);
        prop.setTitles(titles);
        prop.setFields(fields);
        prop.setSheetName(modelName);
        return addTask(prop);
    }

    /**
     * 添加异步导出任务
     *
     * @param userName  导出用户名称
     * @param modelName 导出模块名称
     * @param clazz     导出数据源所在类
     * @param method    导出数据源所在方法
     * @param args      导出数据源所需参数
     * @param titles    Excel 列头配置
     * @param fields    Excel 列头与数据对应关系
     * @param sheetName 导出Excel 下方Sheet标签名称
     * @return
     */
    public boolean addTask(String userName, String modelName, Class clazz, String method, Object[] args, String[] titles, String[] fields, String sheetName) {
        AsyncExportProp prop = new AsyncExportProp();
        prop.setUserName(userName);
        prop.setModelName(modelName);
        prop.setClazz(clazz);
        prop.setMethodName(method);
        prop.setArgs(args);
        prop.setTitles(titles);
        prop.setFields(fields);
        prop.setSheetName(sheetName);
        return addTask(prop);
    }

    /**
     * 添加任务
     * 1、 校验参数
     * 2、 查询当前缓存中是否包含有该查询条件的任务存在
     * 校验规则： 前缀 + 用户名称 + 类名 + 方法名 + 参数
     * 3、 生成任务信息
     * 4、 添加查询条件缓存
     * 5、 添加任务至  redis 缓存队列
     *
     * @param prop
     * @return
     */
    public boolean addTask(AsyncExportProp prop) {
        if (!checkProps(prop)) {
            throw new BizException(-1, "异步导出参数配置错误...");
        }

        StringBuffer query = new StringBuffer();
        query.append(RedisKeys.ASYNC_EXPORT_UNION_KEY)
                .append(prop.getUserName())
                .append(":").append(prop.getClazz().getName())
                .append(":").append(prop.getMethodName())
                .append(":").append(JSON.toJSONString(prop.getArgs()));

        boolean hasExported = stringRedisTemplate.hasKey(query.toString());
        if (hasExported) {
            return false;
        }

        long id = IdUtil.nextId();
        prop.setId(id);
        stringRedisTemplate.opsForValue().set(query.toString(), "true", 1, TimeUnit.MINUTES);

        redisTemplate.opsForList().leftPush(RedisKeys.ASYNC_EXPORT_KEY, prop);
        AsyncExportExcel model = new AsyncExportExcel();
        model.setId(id);
        model.setTaskName(prop.getModelName() + sdf.format(new Date()));
        model.setCreateBy(prop.getUserName());
        model.setTaskState(AsyncExportExcelStatusEnums.NEW.getCode());
        model.setTaskParam(JSON.toJSONString(prop, SerializerFeature.WriteClassName));
//        exportExcelMapper.insertSelective(model);
        return true;
    }

    /**
     * 执行异步导出
     * <p>
     * 1、判断当前有没有正在执行的导出任务，如有 退出
     * 2、取【 1 】个任务，如果取不到退出
     * 3、添加任务执行标志（防止导出任务并行处理）
     * 4、执行任务
     * 5、执行任务结束后【正常执行、异常退出】都删除任务，页面支持重新执行该任务操作
     * 6、删除任务执行标志
     *
     * @return none
     */
    public boolean takeTask() {
        boolean isRunning = redisTemplate.hasKey(RedisKeys.ASYNC_EXPORT_RUNNING_KEY);
        if (isRunning) {
            return false;
        }

        AsyncExportProp task = null;
        boolean execing = true;
        while (execing) {
            try {
                stringRedisTemplate.opsForValue().set(RedisKeys.ASYNC_EXPORT_RUNNING_KEY, "true", 10, TimeUnit.MINUTES);
                task = redisTemplate.opsForList().rightPop(RedisKeys.ASYNC_EXPORT_KEY);
                if (Objects.nonNull(task)) {
                    log.info("1.>> 开始异步导出任务，任务号:[{}].....", task.getId());
                    processTask(task);
                } else {
                    execing = false;
                }
            } catch (Exception e) {
                execing = false;
                log.error("导出Excel出现问题:{}", e.getMessage());
                chgDbStatus(task.getId(), AsyncExportExcelStatusEnums.FAIL.getCode(), e.getMessage());
            }
        }

        stringRedisTemplate.delete(RedisKeys.ASYNC_EXPORT_RUNNING_KEY);
        return true;
    }

    /**
     * 执行任务
     * <p>
     * 1、更新数据库执行状态为 进行中
     * 2、抓取数据
     * 3、写入Excel
     * 4、上传Excel至云存储
     * 5、写入数据库网络访问URL，和执行完毕标志
     *
     * @param prop 任务信息
     */
    protected void processTask(AsyncExportProp prop) {
        chgDbStatus(prop.getId(), AsyncExportExcelStatusEnums.RUNNING.getCode(), "");

        List<?> data = null;
        try {
            data = fetchData(prop);
        } catch (Exception e) {
            throw new BizException(-1, "获取数据出现异常,原因：" + e.getMessage());
        }

        if (data == null) {
            log.error("抓取任务[{}]数据失败,类：[{}]，方法：[{}]", prop.getId(), prop.getClazz(), prop.getMethodName());
            throw new BizException(-1, "获取数据对象为空...");
        }

        log.info("2.>> 已抓取数据，开始写入Excel，任务号:[{}]...", prop.getId());
        Workbook book = writeExcel(data, prop);
        if (Objects.isNull(book)) {
            log.error("写入Excel失败,任务号[{}]", prop.getId());
            throw new BizException(-1, "写入Excel失败");
        }

        log.info("3.>> 已写入Excel，开始上传，任务号:[{}]...", prop.getId());
        String url = uploadExcel(book);
        if (Strings.isNullOrEmpty(url)) {
            log.error("上传云存储失败,任务号[{}]", prop.getId());
            throw new BizException(-1, "上传云存储失败...");
        }
        writeSuccess(prop.getId(), AsyncExportExcelStatusEnums.SUCCESS.getCode(), url);

        log.info("4.>> 任务号: [{}] 已成功导出 ^_^ ", prop.getId());
    }

    /**
     * 利用java 反射获取bean执行
     * 1、获取bean,注意这里用实现类的接口强转去获得目标bean的代理对象，才能成功执行下面的反射方法
     * 2、获取方法
     * 3、执行反射
     *
     * @param prop 任务
     * @param <T>
     * @return List接口形式的数据
     */
    private <T> List<T> fetchData(AsyncExportProp prop) {
        Object obj = context.getBean(prop.getClazz());
        ValidationUtil.validNull(obj, "未获取Bean...");
        Object[] args = prop.getArgs();
        Class<?>[] arguments = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            arguments[i] = args[i].getClass();
        }

        Method method = ReflectionUtils.findMethod(obj.getClass(), prop.getMethodName(), arguments);
        ValidationUtil.validNull(method, "获取要执行的方法失败,请检查参数类型与参数个数");
        Object objRe = ReflectionUtils.invokeMethod(method, obj, args);

        if (objRe instanceof List) {
            return (List<T>) objRe;
        }

        return null;
    }

    /**
     * 写入Excel
     *
     * @param data
     * @param prop
     * @param <T>
     * @return
     */
    private <T> Workbook writeExcel(List<T> data, AsyncExportProp prop) {
        String header = "";
        String footer = "";
        String sheetName = Strings.isNullOrEmpty(prop.getSheetName()) ? prop.getModelName() : prop.getSheetName();
        try {
            Workbook book = ExcelExporter.export2007Excel(header, footer, sheetName, prop.getTitles(), prop.getFields(), data);
            return book;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新数据状态
     *
     * @param id
     * @param status
     * @param reason
     */
    private void chgDbStatus(Long id, Integer status, String reason) {
        try {
            AsyncExportExcel model = new AsyncExportExcel();
            model.setId(id);
            model.setTaskState(status);
            model.setFailReason(reason);
            model.setLastModifyTime(new Date());
//            exportExcelMapper.updateByPrimaryKeySelective(model);
        } catch (Exception e) {
            log.error("更新数据库失败：{}", e.getMessage());
        }
    }

    /**
     * 执行成功写入网络访问URL
     *
     * @param id
     * @param status 成功标志
     * @param url    网络访问路径
     */
    private void writeSuccess(Long id, Integer status, String url) {
        try {
            AsyncExportExcel model = new AsyncExportExcel();
            model.setId(id);
            model.setTaskState(status);
            model.setLastModifyTime(new Date());
            model.setFailReason("");
            model.setVisitUrl(url);
//            exportExcelMapper.updateByPrimaryKeySelective(model);
        } catch (Exception e) {
            log.error("更新数据库失败：{}", e.getMessage());
        }
    }

    /**
     * 上传文件至云服务器
     *
     * @param book
     * @return
     */
    private String uploadExcel(Workbook book) {
        File excel = null;
        try {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            excel = File.createTempFile(uuid, ".xlsx");
            book.write(new FileOutputStream(excel));

            String url = ImageHandler.uploadImage(new FileInputStream(excel), "export_" + uuid + ".xlsx");
            url = ImageHandler.domainOfBucket + "/" + url;
            return url;
        } catch (IOException e) {
            log.error("上传云存储失败:", e.getMessage());
        } finally {
            if (excel != null) {
                excel.delete();
            }
        }
        return null;
    }

    /**
     * 校验必要参数
     *
     * @param prop
     * @return
     */
    private boolean checkProps(AsyncExportProp prop) {
        if (Objects.isNull(prop) ||
                Strings.isNullOrEmpty(prop.getUserName()) ||
                Strings.isNullOrEmpty(prop.getModelName()) ||
                Objects.isNull(prop.getClazz()) ||
                Strings.isNullOrEmpty(prop.getMethodName()) ||
                prop.getFields() == null ||
                prop.getTitles() == null) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        AsyncExportExcel model = new AsyncExportExcel();
        model.setId(110L);
        model.setTaskState(1);
        model.setTaskName("任务名");

        AsyncExportExcel model1 = new AsyncExportExcel();
        model1.setId(120L);
        model1.setTaskState(1);
        model1.setTaskName("任务名3");

        AsyncExportExcel[] arr = new AsyncExportExcel[2];
        arr[0] = model;
        arr[1] = model1;

        String str = JSONObject.toJSONString(arr, SerializerFeature.WriteClassName);
        System.out.println(str);

        ParserConfig defaultRedisConfig = new ParserConfig();
        defaultRedisConfig.setAutoTypeSupport(true);
        Object desc = JSON.parseObject(str, Object.class, defaultRedisConfig);
        System.out.println(desc);


        ArrayList<String> ll = new ArrayList<>();
        System.out.println(ll instanceof List);
    }

}
