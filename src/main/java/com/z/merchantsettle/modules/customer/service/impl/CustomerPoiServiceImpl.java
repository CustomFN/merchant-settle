package com.z.merchantsettle.modules.customer.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.z.merchantsettle.modules.customer.dao.CustomerPoiAuditedDBMapper;
import com.z.merchantsettle.modules.customer.dao.CustomerPoiDBMapper;
import com.z.merchantsettle.modules.customer.domain.db.CustomerPoiDB;
import com.z.merchantsettle.modules.customer.service.CustomerPoiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerPoiServiceImpl implements CustomerPoiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerPoiServiceImpl.class);

    @Autowired
    private CustomerPoiDBMapper customerPoiDBMapper;

    @Autowired
    private CustomerPoiAuditedDBMapper customerPoiAuditedDBMapper;

    @Override
    public List<Integer> getWmPoiIdListByCustomerId(Integer customerId) {
        List<CustomerPoiDB> customerPoiDBList = customerPoiDBMapper.selectByCustomerId(customerId);
        List<Integer> wmPoiIdList = Lists.newArrayList();
        wmPoiIdList = Lists.transform(customerPoiDBList, new Function<CustomerPoiDB, Integer>() {
            @Override
            public Integer apply(CustomerPoiDB input) {
                return input.getWmPoiId();
            }
        });
        return wmPoiIdList;
    }

    @Override
    public void deleteByCustomerIdOnOff(Integer customerId) {
        customerPoiDBMapper.unbindCustomerPoiAll(customerId);
        customerPoiAuditedDBMapper.unbindCustomerPoiAll(customerId);
    }

    @Override
    public void save(Integer customerId, Integer wmPoiId) {
        LOGGER.info("CustomerPoiServiceImpl save customerId = {}, wmPoiId = {}", customerId, wmPoiId);

        CustomerPoiDB customerPoiDB = new CustomerPoiDB();
        customerPoiDB.setCustomerId(customerId);
        customerPoiDB.setWmPoiId(wmPoiId);
        customerPoiDBMapper.insert(customerPoiDB);
    }

    @Override
    public void saveAudited(Integer customerId, Integer wmPoiId) {
        LOGGER.info("CustomerPoiServiceImpl saveAudited customerId = {}, wmPoiId = {}", customerId, wmPoiId);

        CustomerPoiDB customerPoiDB = new CustomerPoiDB();
        customerPoiDB.setCustomerId(customerId);
        customerPoiDB.setWmPoiId(wmPoiId);
        customerPoiAuditedDBMapper.insert(customerPoiDB);
    }
}
