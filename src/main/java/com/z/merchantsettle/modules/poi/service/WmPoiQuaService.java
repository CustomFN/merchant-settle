package com.z.merchantsettle.modules.poi.service;


import cn.hutool.poi.exceptions.POIException;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiQua;

import java.util.List;


public interface WmPoiQuaService {

    void saveOrUpdate(WmPoiQua wmPoiQua, String userId);

    WmPoiQua getWmPoiQuaByWmPoiId(Integer wmPoiId, Integer effective) throws POIException;

    void setupEffectWmPoiQua(Integer wmPoiId, String opUserId);

    List<WmPoiQua> getByWmPoiIdList(List<Integer> wmPoiIdList);
}
