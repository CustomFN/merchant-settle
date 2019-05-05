package com.z.merchantsettle.modules.poi.dao;

import com.z.merchantsettle.modules.poi.domain.db.WmPoiQuaDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WmPoiQuaAuditedDBMapper {

    void updateSelective(WmPoiQuaDB wmPoiQuaDB);

    void insertSelective(WmPoiQuaDB wmPoiQuaDB);

    WmPoiQuaDB getById(Integer wmPoiId);

    void deleteByWmPoiIdList(@Param("wmPoiIdList") List<Integer> wmPoiIdList);
}
