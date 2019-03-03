package com.z.merchantsettle.modules.customer.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.dao.CustomerSignerAuditedDBMapper;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSignerAudited;
import com.z.merchantsettle.modules.customer.domain.db.CustomerSignerAuditedDB;
import com.z.merchantsettle.modules.customer.service.CustomerSignerAuditedService;
import com.z.merchantsettle.utils.transfer.customer.CustomerTransferUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MultiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerSignerAuditedServiceImpl implements CustomerSignerAuditedService {

    @Autowired
    private CustomerSignerAuditedDBMapper customerSignerAuditedDBMapper;

    @Override
    public List<CustomerSignerAudited> getCustomerSignerByContractId(Integer contractId) throws CustomerException {
        if (contractId == null || contractId <= 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        List<CustomerSignerAuditedDB> customerSignerAuditedDBList = customerSignerAuditedDBMapper.getCustomerSignerByContractId(contractId);
        return CustomerTransferUtil.transCustomerSignerAuditedDBList2BoList(customerSignerAuditedDBList);
    }

    @Override
    public void saveOrUpdate(List<CustomerSignerAudited> customerSignerAuditedList) throws CustomerException {
        if (CollectionUtils.isEmpty(customerSignerAuditedList)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "保存更新客户合同签约人参数错误");
        }

        List<CustomerSignerAuditedDB> customerSignerAuditedDBList = CustomerTransferUtil.transCustomerSignerAuditedList2DBList(customerSignerAuditedList);
        List<CustomerSignerAuditedDB> customerSignerAuditedDBInDB = customerSignerAuditedDBMapper.getCustomerSignerByContractId(customerSignerAuditedList.get(0).getContractId());
        if (CollectionUtils.isEmpty(customerSignerAuditedDBInDB)) {
            for (CustomerSignerAuditedDB customerSignerAuditedDB : customerSignerAuditedDBList) {
                customerSignerAuditedDBMapper.insertSelective(customerSignerAuditedDB);
            }
        } else {
            for (CustomerSignerAuditedDB customerSignerAuditedDB : customerSignerAuditedDBList) {
                customerSignerAuditedDBMapper.updateByIdSelective(customerSignerAuditedDB);
            }
        }

    }

    @Override
    public Map<Integer, List<CustomerSignerAudited>> getCustomerSignerByContractIdList(List<Integer> contractIdList) {
        if (CollectionUtils.isEmpty(contractIdList)) {
            return Maps.newHashMap();
        }

        List<CustomerSignerAuditedDB> customerSignerAuditedDBList = customerSignerAuditedDBMapper.getCustomerSignerByContractIdList(contractIdList);
        List<CustomerSignerAudited> customerSignerAuditedList = CustomerTransferUtil.transCustomerSignerAuditedDBList2BoList(customerSignerAuditedDBList);
        Map<Integer, List<CustomerSignerAudited>> signerAuditedMap = Maps.newHashMap();
        for (CustomerSignerAudited customerSignerAudited : customerSignerAuditedList) {
            Integer contractId = customerSignerAudited.getContractId();
            List<CustomerSignerAudited> signerAuditedList = signerAuditedMap.get(contractId);
            if (signerAuditedList == null) {
                signerAuditedList = Lists.newArrayList();
                signerAuditedList.add(customerSignerAudited);
                signerAuditedMap.put(contractId, signerAuditedList);
            } else {
                signerAuditedList.add(customerSignerAudited);
            }
        }
        return signerAuditedMap;
    }
}
