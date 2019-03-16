package com.z.merchantsettle.modules.poi.service;

import com.z.merchantsettle.modules.poi.domain.bo.WmPoiDeliveryInfo;

import java.util.List;

public interface WmPoiDeliveryInfoService {

    void saveOrUpdate(WmPoiDeliveryInfo wmPoiDeliveryInfo, String opUserId);

    WmPoiDeliveryInfo getWmPoiDeliveryInfoById(Integer wmPoiId, Integer effective);

    void setupEffectWmPoiDeliveryInfo(Integer wmPoiId);

    List<WmPoiDeliveryInfo> getByWmPoiIdList(List<Integer> wmPoiIdList);
}
