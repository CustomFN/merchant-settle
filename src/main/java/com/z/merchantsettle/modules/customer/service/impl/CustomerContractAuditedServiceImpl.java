package com.z.merchantsettle.modules.customer.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.dao.CustomerContractAuditedDBMapper;
import com.z.merchantsettle.modules.customer.domain.ContractRequestParam;
import com.z.merchantsettle.modules.customer.domain.bo.ContractBaseInfo;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerBaseInfo;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerContractAudited;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSignerAudited;
import com.z.merchantsettle.modules.customer.domain.db.CustomerContractAuditedDB;
import com.z.merchantsettle.modules.customer.service.CustomerContractAuditedService;
import com.z.merchantsettle.modules.customer.service.CustomerSignerAuditedService;
import com.z.merchantsettle.utils.transfer.customer.CustomerTransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerContractAuditedServiceImpl implements CustomerContractAuditedService {

    @Autowired
    private CustomerContractAuditedDBMapper customerContractAuditedDBMapper;

    @Autowired
    private CustomerSignerAuditedService customerSignerAuditedService;

    @Override
    public List<CustomerContractAudited> getCustomerContractList(Integer customerId) throws CustomerException {
        if (customerId == null || customerId <= 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }
        List<CustomerContractAuditedDB> customerContractAuditedDBList =  customerContractAuditedDBMapper.getByCustomerId(customerId);
        return CustomerTransferUtil.transCustomerContractAuditedDBList2BoList(customerContractAuditedDBList);
    }

    @Override
    public CustomerContractAudited getCustomerContractById(Integer contractId) throws CustomerException {
        if (contractId == null || contractId <= 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }
        CustomerContractAuditedDB customerContractAuditedDB = customerContractAuditedDBMapper.selectById(contractId);
        return CustomerTransferUtil.transCustomerContractAuditedDB2Bo(customerContractAuditedDB);
    }

    @Override
    public void saveOrUpdate(CustomerContractAudited customerContractAudited) throws CustomerException {
        if (customerContractAudited == null) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "保存更新线上合同异常");
        }

        CustomerContractAuditedDB customerContractAuditedDB = CustomerTransferUtil.transCustomerContractAudited2DB(customerContractAudited);
        CustomerSignerAudited partyA = customerContractAudited.getPartyA();
        CustomerSignerAudited partyB = customerContractAudited.getPartyB();
        List<CustomerSignerAudited> customerSignerAuditedList = Lists.newArrayList(partyA, partyB);
        if (customerContractAudited.getId() != null) {
            customerContractAuditedDBMapper.updateByIdSelective(customerContractAuditedDB);
            customerSignerAuditedService.saveOrUpdate(customerSignerAuditedList);
        } else {
            customerContractAuditedDBMapper.insertSelective(customerContractAuditedDB);
            customerSignerAuditedService.saveOrUpdate(customerSignerAuditedList);
        }
    }

    @Override
    public PageData<ContractBaseInfo> getContractBaseInfoList(ContractRequestParam contractRequestParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CustomerContractAuditedDB> contractList = customerContractAuditedDBMapper.getContractList(contractRequestParam);
        PageInfo<CustomerContractAuditedDB> pageInfo = new PageInfo<>(contractList);

        List<Integer> contractIdList = Lists.transform(contractList, new Function<CustomerContractAuditedDB, Integer>() {
            @Override
            public Integer apply(CustomerContractAuditedDB input) {
                return input.getId();
            }
        });
        Map<Integer, List<CustomerSignerAudited>> customerSignerMap = customerSignerAuditedService.getCustomerSignerByContractIdList(contractIdList);

        List<ContractBaseInfo> contractBaseInfoList = Lists.newArrayList();
        for (CustomerContractAuditedDB customerContractAuditedDB : contractList) {
            ContractBaseInfo contractBaseInfo = new ContractBaseInfo();
            contractBaseInfo.setContractId(customerContractAuditedDB.getId());
            contractBaseInfo.setContractNum(customerContractAuditedDB.getCustomerContractNum());
            contractBaseInfo.setAuditStatus(CustomerConstant.CustomerStatus.getByCode(customerContractAuditedDB.getStatus()));
            contractBaseInfo.setContractType("纸质合同");
            contractBaseInfo.setContractValidTime(DateUtil.secondToTime(customerContractAuditedDB.getContractEndTime().intValue()));

            List<CustomerSignerAudited> customerSignerAuditedList = customerSignerMap.get(customerContractAuditedDB.getId());
            for (CustomerSignerAudited customerSignerAudited : customerSignerAuditedList) {
                if (CustomerConstant.SIGNER_LABEL_A.equals(customerSignerAudited.getSignerLabel())) {
                    contractBaseInfo.setPartyAName(customerSignerAudited.getParty());
                    contractBaseInfo.setPartyASignerName(customerSignerAudited.getPartyContactPerson());
                } else {
                    contractBaseInfo.setPartyBName(customerSignerAudited.getParty());
                    contractBaseInfo.setPrincipal(customerSignerAudited.getPartyContactPerson());
                    contractBaseInfo.setSignerTime(DateUtil.secondToTime(customerSignerAudited.getSignTime().intValue()));
                }
            }

            contractBaseInfoList.add(contractBaseInfo);
        }
        return new PageData.Builder<ContractBaseInfo>()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalSize((int) pageInfo.getTotal())
                .totalPage(pageInfo.getPages())
                .data(contractBaseInfoList)
                .build();
    }

}
