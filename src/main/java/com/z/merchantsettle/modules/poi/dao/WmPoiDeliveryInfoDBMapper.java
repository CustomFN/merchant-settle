package com.z.merchantsettle.modules.poi.dao;

import com.z.merchantsettle.modules.poi.domain.db.WmPoiDeliveryInfoDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WmPoiDeliveryInfoDBMapper {

    void updateSelective(WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB);

    void insertSelective(WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB);

    WmPoiDeliveryInfoDB getByWmPoiId(Integer wmPoiId);

    List<WmPoiDeliveryInfoDB> getByWmPoiIdList(@Param("wmPoiIdList") List<Integer> wmPoiIdList);

    void deleteByWmPoiIdList(@Param("wmPoiIdList") List<Integer> wmPoiIdList);
}
