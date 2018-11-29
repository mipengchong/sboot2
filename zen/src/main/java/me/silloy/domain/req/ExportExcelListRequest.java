package me.silloy.domain.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: ybbdhfhv
 * @Date: 2018/11/13 17:32
 * @Description:
 */
@Data
public class ExportExcelListRequest extends PageReq {

    private String userName;

    @NotNull(message = "任务编号不能为空")
    private Long taskId;
}
