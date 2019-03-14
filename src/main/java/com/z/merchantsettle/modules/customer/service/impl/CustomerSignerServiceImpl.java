package com.z.merchantsettle.modules.customer.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.dao.CustomerSignerDBMapper;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSigner;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSignerAudited;
import com.z.merchantsettle.modules.customer.domain.db.CustomerSignerAuditedDB;
import com.z.merchantsettle.modules.customer.domain.db.CustomerSignerDB;
import com.z.merchantsettle.modules.customer.service.CustomerOpLogService;
import com.z.merchantsettle.modules.customer.service.CustomerSignerService;
import com.z.merchantsettle.utils.transfer.customer.CustomerTransferUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerSignerServiceImpl implements CustomerSignerService {

    @Autowired
    private CustomerSignerDBMapper customerSignerDBMapper;

    @Autowired
    private CustomerOpLogService customerOpLogService;


    @Override
    public void updateByIdSelective(List<CustomerSigner> customerSignerList) throws CustomerException {
        if (CollectionUtils.isEmpty(customerSignerList)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "签约人信息参数异常");
        }

        List<CustomerSignerDB> customerSignerDBList = CustomerTransferUtil.transCustomerSignerList2DBList(customerSignerList);
        for (CustomerSignerDB customerSignerDB : customerSignerDBList) {
            customerSignerDBMapper.updateByIdSelective(customerSignerDB);
        }

    }

    @Override
    public void insertSelective(List<CustomerSigner> customerSignerList) throws CustomerException {
        if (CollectionUtils.isEmpty(customerSignerList)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "签约人信息参数异常");
        }

        List<CustomerSignerDB> customerSignerDBList = CustomerTransferUtil.transCustomerSignerList2DBList(customerSignerList);
        for (CustomerSignerDB customerSignerDB : customerSignerDBList) {
            customerSignerDBMapper.insertSelective(customerSignerDB);
        }
    }

    @Override
    public List<CustomerSigner> getCustomerSignerByContractId(Integer contractId) throws CustomerException {
        if (contractId == null || contractId <= 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        List<CustomerSignerDB> customerSignerDBList = customerSignerDBMapper.getCustomerSignerByContractId(contractId);
        return CustomerTransferUtil.transCustomerSignerDBList2BOList(customerSignerDBList);
    }

    @Override
    public Map<Integer, List<CustomerSigner>> getCustomerSignerByContractIdList(List<Integer> contractIdList) {
        if (CollectionUtils.isEmpty(contractIdList)) {
            return Maps.newHashMap();
        }

        List<CustomerSignerDB> customerSignerDBList = customerSignerDBMapper.getCustomerSignerByContractIdList(contractIdList);
        List<CustomerSigner> customerSignerList = CustomerTransferUtil.transCustomerSignerDBList2BoList(customerSignerDBList);
        Map<Integer, List<CustomerSigner>> signerMap = Maps.newHashMap();
        for (CustomerSigner customerSigner : customerSignerList) {
            Integer contractId = customerSigner.getContractId();
            List<CustomerSigner> signerAuditedList = signerMap.get(contractId);
            if (signerAuditedList == null) {
                signerAuditedList = Lists.newArrayList();
                signerAuditedList.add(customerSigner);
                signerMap.put(contractId, signerAuditedList);
            } else {
                signerAuditedList.add(customerSigner);
            }
        }
        return signerMap;
    }
}
