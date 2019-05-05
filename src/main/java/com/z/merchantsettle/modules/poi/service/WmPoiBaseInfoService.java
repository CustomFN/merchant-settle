package com.z.merchantsettle.modules.poi.service;

import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.modules.poi.domain.WmPoiSearchParam;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiBaseInfo;

import java.util.List;

public interface WmPoiBaseInfoService {

    WmPoiBaseInfo saveOrUpdate(WmPoiBaseInfo wmPoiBaseInfo, String userId);

    WmPoiBaseInfo getWmPoiBaseInfoById(Integer wmPoiId, Integer effective);

    void setupEffectWmPoiBaseInfo(Integer wmPoiId);

    List<WmPoiBaseInfo> getByIdList(List<Integer> wmPoiIdList);

    void distributePrincipal(Integer wmPoiId, String principalId, String opUserId);

    PageData<WmPoiBaseInfo> getBaseInfoList(WmPoiSearchParam wmPoiSearchParam, Integer pageNum, Integer pageSize);

    void updateByIdForAudit(WmPoiBaseInfo wmPoiBaseInfo, String opUserId);

    void deleteByIdList(List<Integer> wmPoiIdList);
}
