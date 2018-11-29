package me.silloy.domain.req;

import lombok.Data;

@Data
public class PageReq {

    private Integer pageNum = 0;
    private Integer pageSize = 10;
}
