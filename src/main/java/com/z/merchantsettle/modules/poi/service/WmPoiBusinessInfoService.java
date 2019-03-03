package com.z.merchantsettle.modules.poi.service;

import com.z.merchantsettle.modules.poi.domain.bo.WmPoiBaseInfo;

public interface WmPoiBusinessInfoService {

    void saveOrUpdate(WmPoiBaseInfo wmPoiBaseInfo,  String userId);

    WmPoiBaseInfo getWmPoiBaseInfoById(Integer wmPoiId, Integer effective);

    void setupEffectWmPoiBusinessInfo(Integer wmPoiId, String opUserId);
}
