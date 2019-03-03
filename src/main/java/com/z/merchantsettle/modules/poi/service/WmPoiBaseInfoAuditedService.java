package com.z.merchantsettle.modules.poi.service;

import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.modules.poi.domain.WmPoiSearchParam;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiBaseInfo;

import java.util.List;

public interface WmPoiBaseInfoAuditedService {

    void saveOrUpdate(WmPoiBaseInfo wmPoiBaseInfo);

    WmPoiBaseInfo getWmPoiBaseInfoById(Integer wmPoiId);

    PageData<WmPoiBaseInfo> getBaseInfoList(WmPoiSearchParam wmPoiSearchParam, Integer pageNum, Integer pageSize);
}
