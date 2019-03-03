package com.z.merchantsettle.modules.poi.dao;

import com.z.merchantsettle.modules.poi.domain.db.WmPoiQuaDB;
import org.springframework.stereotype.Repository;

@Repository
public interface WmPoiQuaAuditedDBMapper {

    void updateSelective(WmPoiQuaDB wmPoiQuaDB);

    void insertSelective(WmPoiQuaDB wmPoiQuaDB);

    WmPoiQuaDB getById(Integer wmPoiId);
}
