package com.z.merchantsettle.modules.poi.service;

import com.z.merchantsettle.modules.poi.domain.bo.WmPoiBaseInfo;

import java.util.List;

public interface WmPoiBaseInfoService {

    void saveOrUpdate(WmPoiBaseInfo wmPoiBaseInfo, String userId);

    WmPoiBaseInfo getWmPoiBaseInfoById(Integer wmPoiId, Integer effective);

    void setupEffectWmPoiBaseInfo(Integer wmPoiId, String opUserId);

    List<WmPoiBaseInfo> getByIdList(List<Integer> wmPoiIdList);

    void distributePrincipal(Integer wmPoiId, String principalId, String opUserId);
}
