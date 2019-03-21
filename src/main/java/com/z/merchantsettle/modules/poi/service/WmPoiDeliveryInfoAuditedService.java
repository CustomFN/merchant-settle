package com.z.merchantsettle.modules.poi.service;

import com.z.merchantsettle.modules.poi.domain.bo.WmPoiDeliveryInfo;

public interface WmPoiDeliveryInfoAuditedService {

    void saveOrUpdate(WmPoiDeliveryInfo wmPoiDeliveryInfoAudited);

    WmPoiDeliveryInfo getWmPoiDeliveryInfoAuditedById(Integer wmPoiId);
}
