package com.z.merchantsettle.modules.customer.service;

import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.domain.CustomerOpLog;
import com.z.merchantsettle.modules.customer.domain.CustomerOpLogSearchParam;


public interface CustomerOpLogService {

    void addLog(Integer customerId, String module, String content, String opUserId);

    PageData<CustomerOpLog> getLogByCustomerId(CustomerOpLogSearchParam opLogSearchParam, Integer pageNum, Integer pageSize) throws CustomerException;
}
