package me.silloy.service;

import me.silloy.domain.req.ExportExcelListRequest;


public interface ExportExcelService {
    void listFiles(ExportExcelListRequest request);

    boolean restart(ExportExcelListRequest request);
}
