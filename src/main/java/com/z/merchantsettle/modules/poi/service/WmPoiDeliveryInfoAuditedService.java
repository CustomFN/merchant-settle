package com.z.merchantsettle.modules.poi.service;

import com.z.merchantsettle.modules.poi.domain.bo.WmPoiDeliveryInfo;

import java.util.List;

public interface WmPoiDeliveryInfoAuditedService {

    void saveOrUpdate(WmPoiDeliveryInfo wmPoiDeliveryInfoAudited);

    WmPoiDeliveryInfo getWmPoiDeliveryInfoAuditedById(Integer wmPoiId);

    void deleteByWmPoiIdList(List<Integer> wmPoiIdList);
}
