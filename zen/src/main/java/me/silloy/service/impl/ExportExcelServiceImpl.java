package me.silloy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import lombok.extern.slf4j.Slf4j;
import me.silloy.common.RedisKeys;
import me.silloy.domain.AsyncExportExcel;
import me.silloy.domain.AsyncExportProp;
import me.silloy.domain.req.ExportExcelListRequest;
import me.silloy.service.ExportExcelService;
import me.silloy.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author: ybbdhfhv
 * @Date: 2018/11/13 17:25
 * @Description:
 */
@Service
@Slf4j
public class ExportExcelServiceImpl implements ExportExcelService {

//    @Autowired
//    AsyncExportExcelMapper exportExcelMapper;
//    @Autowired
//    AsyncExportExcelExtMapper exportExcelExtMapper;

    @Autowired
    RedisTemplate<String, AsyncExportProp> redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void listFiles(ExportExcelListRequest request) {
//        PageHelper.startPage(request.getPageNum(), request.getPageSize());
//        List<ExportedFileList> list = exportExcelExtMapper.selectExportedFileList(request);
//        for (ExportedFileList e : list) {
//            e.setTaskStateName(AsyncExportExcelStatusEnums.getName(e.getTaskState()));
//        }
//        PageInfo pageInfo = new PageInfo(list);
//        return pageInfo;
    }

    @Override
    public boolean restart(ExportExcelListRequest request) {
//        AsyncExportExcel entity = exportExcelMapper.selectByPrimaryKey(request.getTaskId());
        AsyncExportExcel entity = new AsyncExportExcel();
        ValidationUtil.validNull(entity, "未找到任务信息");
        ParserConfig defaultRedisConfig = new ParserConfig();
        defaultRedisConfig.setAutoTypeSupport(true);

        AsyncExportProp prop = null;
        Object desc = JSON.parseObject(entity.getTaskParam(), Object.class, defaultRedisConfig);
        if (desc instanceof AsyncExportProp) {
            prop = (AsyncExportProp) desc;
        }
        ValidationUtil.validNull(prop, "未获取到任务执行参数");


        StringBuffer query = new StringBuffer();
        query.append(RedisKeys.ASYNC_EXPORT_UNION_KEY)
                .append(prop.getUserName())
                .append(":").append(prop.getClazz().getName())
                .append(":").append(prop.getMethodName())
                .append(":").append(JSON.toJSONString(prop.getArgs()));
        if (stringRedisTemplate.hasKey(query.toString())) {
            return true;
        }

        stringRedisTemplate.opsForValue().set(query.toString(), "true", 1, TimeUnit.MINUTES);
        redisTemplate.opsForList().leftPush(RedisKeys.ASYNC_EXPORT_KEY, prop);

        try {
//            exportExcelExtMapper.restart(prop.getId(), AsyncExportExcelStatusEnums.NEW.getCode());
        } catch (Exception e) {
            log.error("更新数据库状态失败...[{}]", e.getMessage());
        }

        return true;
    }

}
