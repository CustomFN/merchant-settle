package com.z.merchantsettle.modules.poi.service;


import cn.hutool.poi.exceptions.POIException;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiQua;

import java.util.List;


public interface WmPoiQuaService {

    WmPoiQua saveOrUpdate(WmPoiQua wmPoiQua, String userId);

    WmPoiQua getWmPoiQuaByWmPoiId(Integer wmPoiId, Integer effective) throws POIException;

    void setupEffectWmPoiQua(Integer wmPoiId);

    List<WmPoiQua> getByWmPoiIdList(List<Integer> wmPoiIdList);

    void updateByIdForAudit(WmPoiQua wmPoiQua, String opUserId);
}
