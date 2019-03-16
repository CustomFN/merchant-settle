package com.z.merchantsettle.modules.customer.service;

import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettleAudited;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettleBaseInfo;

public interface CustomerSettleAuditedService {

    CustomerSettleAudited getCustomerSettleBySettleId(Integer settleId);

    PageData<CustomerSettleBaseInfo> getCustomerSettleList(Integer customerId, String settleOrPoiId, Integer pageNum, Integer pageSize);

    void saveOrUpdate(CustomerSettleAudited customerSettleAudited);

    void deleteByCustomerId(Integer customerId);
}
