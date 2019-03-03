package com.z.merchantsettle.modules.poi.service;

import com.z.merchantsettle.modules.poi.domain.bo.WmPoiDeliveryInfoAudited;

public interface WmPoiDeliveryInfoAuditedService {

    void saveOrUpdate(WmPoiDeliveryInfoAudited wmPoiDeliveryInfoAudited);

    WmPoiDeliveryInfoAudited getWmPoiDeliveryInfoAuditedById(Integer wmPoiId);
}
