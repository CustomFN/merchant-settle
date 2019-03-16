package com.z.merchantsettle.modules.customer.service;

import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettle;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettleBaseInfo;

import java.util.List;

public interface CustomerSettleService {

    CustomerSettle saveOrUpdate(CustomerSettle customerSettle, String opUserId);

    CustomerSettle getCustomerSettleBySettleId(Integer settleId, Integer effective);

    PageData<CustomerSettleBaseInfo> getCustomerSettleList(Integer customerId, String settleOrPoiId, Integer effective, Integer pageNum, Integer pageSize);

    void deleteBySettleId(Integer settleId);

    void setupEffectCustomerSettle(Integer customerId, Integer settleId);

    List<CustomerSettle> getBySettleIdList(List<Integer> customerSettleIdList);

    void deleteByCustomerId(Integer customerId, String opUserId) throws CustomerException;
}
