package me.silloy.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

/**
 * @Author: ybbdhfhv
 * @Date: 2018/11/12 17:20
 * @Description:        异步导出参数集合
 */
@Data
public class AsyncExportProp {
    /**
     * 系统内部使用，外部无需设置
     */
    @JsonPropertyDescription("系统内部使用ID")
    private Long id;

    /**
     * 必选参数项
     */
    @JsonPropertyDescription("导出用户姓名")
    private String userName;
    @JsonPropertyDescription("任务名称")
    private String modelName;
    @JsonPropertyDescription("数据源提供者类名")
    private Class clazz;
    @JsonPropertyDescription("数据源提供者方法名")
    private String methodName;
    @JsonPropertyDescription("数据源提供者所需参数")
    private Object[] args;
    @JsonPropertyDescription("Excel表头")
    private String[] titles;
    @JsonPropertyDescription("数据源列信息（与Excel表头相对应）")
    private String[] fields;

    /**
     * 可选参数
     */
    @JsonPropertyDescription("Sheet标签名称[如不配置取任务名称]")
    private String sheetName;

}
