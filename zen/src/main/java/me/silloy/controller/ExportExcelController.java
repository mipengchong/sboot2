package me.silloy.controller;

import lombok.extern.slf4j.Slf4j;
import me.silloy.domain.AsyncExportProp;
import me.silloy.domain.Response;
import me.silloy.domain.req.ExportExcelListRequest;
import me.silloy.service.AsyncExcelExporterService;
import me.silloy.service.ExportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: ybbdhfhv
 * @Date: 2018/11/13 17:19
 * @Description: 导出中心
 */
@Slf4j
@RestController
@RequestMapping("/exp/center")
public class ExportExcelController {
    @Autowired
    ExportExcelService exportExcelService;

    @Autowired
    AsyncExcelExporterService asyncExporter;

    /**
     * 获取下载任务列表
     *
     * @param request
     * @return
     */
    @PostMapping("/list")
    public Response listFiles(@RequestBody ExportExcelListRequest request) {
//        String userName = super.getUserName();
//        request.setUserName(userName);
//        PageInfo page = exportExcelService.listFiles(request);
        return Response.getInstance();
    }

    /**
     * 重启任务
     *
     * @param request
     * @return
     */
    @PostMapping("/restart")
    public Response<Boolean> restart(@RequestBody ExportExcelListRequest request) {
        boolean ret = exportExcelService.restart(request);
        return Response.getInstance(ret);
    }

    /**
     * 消费任务
     *
     * @return
     */
    @PostMapping("/takeTask")
    public Response<Boolean> takeTask() {
        asyncExporter.takeTask();
        return Response.getInstance(true);
    }

    /**
     * 测试随机返回
     *
     * @return
     * @TODO 功能验证方法，验证通过后删除
     */
    @PostMapping("/testRandom")
    @Deprecated
    public Response<Boolean> testRandom(@RequestParam("time") Integer time) {

        String[] columnNames = {"ID", "规格", "价格"};
        String[] methodNames = {"id", "spec", "marketPrice"};

        AsyncExportProp prop = new AsyncExportProp();
//        prop.setUserName(super.getUserName());
        prop.setModelName("测试随机停留");
//        prop.setClazz(this.getClass());
        prop.setMethodName("genRandomData");
        prop.setArgs(new Object[]{time});
        prop.setTitles(columnNames);
        prop.setFields(methodNames);
        prop.setSheetName("测试随机停留");

        asyncExporter.addTask(prop);
        return Response.getInstance(true);
    }

//    Random random = new Random();
//    @Deprecated
//    public List<VvActivitySku> genRandomData(Integer time){
//        try {
//            Thread.sleep(time * 1000);
//        } catch (InterruptedException e) {}
//
//        List<VvActivitySku> list = Lists.newArrayList();
//
//        for(int i=0;i<time;i++){
//            VvActivitySku item = new VvActivitySku();
//            item.setId(random.nextLong());
//            item.setSpec("这是一个很长的名字");
//            item.setMarketPrice(random.nextLong());
//            list.add(item);
//        }
//        return list;
//    }

}
