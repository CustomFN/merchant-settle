package com.z.merchantsettle.modules.poi.service;


import cn.hutool.poi.exceptions.POIException;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiQua;

import java.util.List;


public interface WmPoiQuaAuditedService {

    void saveOrUpdate(WmPoiQua wmPoiQua);

    WmPoiQua getWmPoiQuaAuditedById(Integer wmPoiId) throws POIException;

    void deleteByWmPoiIdList(List<Integer> wmPoiIdList);
}
