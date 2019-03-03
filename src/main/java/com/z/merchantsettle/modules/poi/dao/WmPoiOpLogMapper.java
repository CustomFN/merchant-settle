package com.z.merchantsettle.modules.poi.dao;

import com.z.merchantsettle.modules.poi.domain.WmPoiOpLog;
import com.z.merchantsettle.modules.poi.domain.WmPoiOpLogSearchParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WmPoiOpLogMapper {

    void insert(@Param("wmPoiOpLog") WmPoiOpLog wmPoiOpLog);

    List<WmPoiOpLog> getLogByWmPoiId(@Param("opLogSearchParam") WmPoiOpLogSearchParam opLogSearchParam);
}
