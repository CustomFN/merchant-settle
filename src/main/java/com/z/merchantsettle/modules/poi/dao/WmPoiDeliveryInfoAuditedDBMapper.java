package com.z.merchantsettle.modules.poi.dao;

import com.z.merchantsettle.modules.poi.domain.db.WmPoiDeliveryInfoDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WmPoiDeliveryInfoAuditedDBMapper {

    WmPoiDeliveryInfoDB getByWmPoiId(Integer wmPoiId);

    void insertSelective(WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB);

    void updateSelective(WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB);

    void deleteByWmPoiIdList(@Param("wmPoiIdList") List<Integer> wmPoiIdList);
}
