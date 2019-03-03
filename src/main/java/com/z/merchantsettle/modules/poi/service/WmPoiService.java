package com.z.merchantsettle.modules.poi.service;

import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.modules.poi.domain.WmPoiSearchParam;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiInfo;

public interface WmPoiService {

    PageData<WmPoiInfo> getBaseList(WmPoiSearchParam wmPoiSearchParam, Integer pageNum, Integer pageSize);

    void distributePrincipal(Integer wmPoiId, String principalId, String opUserId);
}
