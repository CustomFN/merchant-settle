package com.z.merchantsettle.modules.poi.service;

import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.modules.poi.domain.PhysicalPoiReqParam;
import com.z.merchantsettle.modules.poi.domain.bo.PhysicalPoi;


public interface PhysicalPoiService {


    PageData<PhysicalPoi> getList(String userId, PhysicalPoiReqParam physicalPoiReqParam, Integer pageNum, Integer pageSize);

    void claimPhysicalPoi(String userId, Integer physicalPoiId);

    void save(PhysicalPoi physicalPoi, String opUserId);

    PhysicalPoi getById(Integer physicalPoiId);

    void updateStateById(Integer physicalPoiId);
}
