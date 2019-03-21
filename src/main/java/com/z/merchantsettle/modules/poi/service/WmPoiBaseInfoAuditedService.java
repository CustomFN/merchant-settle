package com.z.merchantsettle.modules.poi.service;

import com.z.merchantsettle.modules.poi.domain.bo.WmPoiBaseInfo;


public interface WmPoiBaseInfoAuditedService {

    void saveOrUpdate(WmPoiBaseInfo wmPoiBaseInfo);

    WmPoiBaseInfo getWmPoiBaseInfoById(Integer wmPoiId);
}
