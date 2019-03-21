package com.z.merchantsettle.modules.poi.service;

import com.z.merchantsettle.modules.poi.domain.bo.WmPoiBaseInfo;

public interface WmPoiBusinessInfoService {

    WmPoiBaseInfo saveOrUpdate(WmPoiBaseInfo wmPoiBaseInfo,  String userId);

    WmPoiBaseInfo getWmPoiBaseInfoById(Integer wmPoiId, Integer effective);

    void setupEffectWmPoiBusinessInfo(Integer wmPoiId);
}
