package com.z.merchantsettle.modules.poi.dao;

import com.z.merchantsettle.modules.poi.domain.WmPoiSearchParam;
import com.z.merchantsettle.modules.poi.domain.db.WmPoiBaseInfoDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WmPoiBaseInfoDBMapper {

    void updateSelective(WmPoiBaseInfoDB wmPoiBaseInfoDB);

    void insertSelective(WmPoiBaseInfoDB wmPoiBaseInfoDB);

    WmPoiBaseInfoDB getById(Integer wmPoiId);

    List<WmPoiBaseInfoDB> getByIdList(@Param("wmPoiIdList") List<Integer> wmPoiIdList);

    List<WmPoiBaseInfoDB> getList(@Param("wmPoiSearchParam") WmPoiSearchParam wmPoiSearchParam);
}
