package com.z.merchantsettle.modules.poi.dao;

import com.z.merchantsettle.modules.poi.domain.db.WmPoiDeliveryInfoDB;
import org.springframework.stereotype.Repository;

@Repository
public interface WmPoiDeliveryInfoAuditedDBMapper {

    WmPoiDeliveryInfoDB getByWmPoiId(Integer wmPoiId);

    void insertSelective(WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB);

    void updateSelective(WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB);
}
