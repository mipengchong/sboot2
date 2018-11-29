package me.silloy.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * vv_async_export_excel
 * @author songxueyan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsyncExportExcel {
    /**
     * 系统流水号
     */
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务执行参数【方法名、方法参数、Excel导出配置信息】
     */
    private String taskParam;

    /**
     * 任务当前状态【1：新建、2：执行中、3：执行成功、4：执行失败】
     */
    private Integer taskState;

    /**
     * 下载失败描述
     */
    private String failReason;

    /**
     * 网络下载路径
     */
    private String visitUrl;

    /**
     * 任务创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后一次更新时间
     */
    private Date lastModifyTime;

    /**
     * 是否删除（1已删除，0未删除）
     */
    private Integer isDelete;
}