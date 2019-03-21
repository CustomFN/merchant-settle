package com.z.merchantsettle.modules.poi.dao;

import com.z.merchantsettle.modules.poi.domain.PhysicalPoiReqParam;
import com.z.merchantsettle.modules.poi.domain.db.PhysicalPoiDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhysicalPoiDBMapper {


    List<PhysicalPoiDB> selectList(@Param("userId") String userId, @Param("physicalPoiReqParam") PhysicalPoiReqParam physicalPoiReqParam);

    void updateSelective(PhysicalPoiDB physicalPoiDB);

    void insertSelective(PhysicalPoiDB physicalPoiDB);

    PhysicalPoiDB getById(@Param("id") Integer physicalPoiId);
}
