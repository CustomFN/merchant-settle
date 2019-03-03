package com.z.merchantsettle.modules.poi.service;

import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.modules.poi.domain.WmPoiOpLog;
import com.z.merchantsettle.modules.poi.domain.WmPoiOpLogSearchParam;

public interface WmPoiOpLogService {
    void addLog(Integer wmPoiId, String module, String content, String opUser);

    PageData<WmPoiOpLog> getLogByWmPoiId(WmPoiOpLogSearchParam opLogSearchParam, Integer pageNum, Integer pageSize) throws PoiException;
}