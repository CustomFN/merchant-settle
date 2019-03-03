package com.z.merchantsettle.modules.customer.service;

import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettlePoi;

import java.util.List;

public interface CustomerSettlePoiService {

    List<Integer> getSettleIdByWmPoiId(Integer wmPoiId);

    List<Integer> getSettleIdByWmPoiIdAudited(Integer wmPoiId);

    List<CustomerSettlePoi> getBySettleId(Integer settleId);

    List<CustomerSettlePoi> getBySettleIdAudited(Integer settleId);

    void saveOrUpdateSettlePoi(Integer settleId, List<Integer> wmPoiIdList);

    void saveOrUpdateSettlePoiAudited(List<CustomerSettlePoi> customerSettlePoiList);

    List<CustomerSettlePoi> getByWmPoiIdList(List<Integer> wmPoiIdList);
}
