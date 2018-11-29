package me.silloy.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageHeader extends Header {
    private long total;
    private int pages;
}