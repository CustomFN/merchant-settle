package com.z.merchantsettle.modules.poi.service;


import cn.hutool.poi.exceptions.POIException;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiQuaAudited;


public interface WmPoiQuaAuditedService {

    void saveOrUpdate(WmPoiQuaAudited wmPoiQuaAudited);

    WmPoiQuaAudited getWmPoiQuaAuditedById(Integer wmPoiId) throws POIException;
}
