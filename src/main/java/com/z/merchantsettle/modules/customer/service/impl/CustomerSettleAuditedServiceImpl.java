package com.z.merchantsettle.modules.customer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.dao.CustomerSettleAuditedDBMapper;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettleAudited;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettleBaseInfo;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettlePoi;
import com.z.merchantsettle.modules.customer.domain.db.CustomerSettleAuditedDB;
import com.z.merchantsettle.modules.customer.service.CustomerSettleAuditedService;
import com.z.merchantsettle.modules.customer.service.CustomerSettlePoiService;
import com.z.merchantsettle.utils.transfer.customer.CustomerTransferUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerSettleAuditedServiceImpl implements CustomerSettleAuditedService {

    @Autowired
    private CustomerSettleAuditedDBMapper customerSettleAuditedDBMapper;

    @Autowired
    private CustomerSettlePoiService customerSettlePoiService;

    @Override
    public CustomerSettleAudited getCustomerSettleBySettleId(Integer settleId) {
        if (settleId == null || settleId <= 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        CustomerSettleAuditedDB customerSettleAuditedDB = customerSettleAuditedDBMapper.selectById(settleId);
        return CustomerTransferUtil.transCustomerSettleAuditedDB2Bo(customerSettleAuditedDB);
    }

    @Override
    public PageData<CustomerSettleBaseInfo> getCustomerSettleList(Integer customerId, String settleOrPoiId, Integer pageNum, Integer pageSize) {
        List<Integer> settleIdList = Lists.newArrayList();
        if (StringUtils.isNotBlank(settleOrPoiId)) {
            List<Integer> settleIds = customerSettlePoiService.getSettleIdByWmPoiIdAudited(Integer.valueOf(settleOrPoiId));
            settleIdList.addAll(settleIds);
        }

        PageHelper.startPage(pageNum, pageSize);
        List<CustomerSettleAuditedDB> settleDBList = customerSettleAuditedDBMapper.getSettleList(customerId, settleIdList);
        PageInfo<CustomerSettleAuditedDB> pageInfo = new PageInfo<>(settleDBList);

        List<CustomerSettleBaseInfo> settleList = Lists.newArrayList();
        for (CustomerSettleAuditedDB customerSettleDB : settleDBList) {
            CustomerSettleBaseInfo customerSettleBaseInfo = new CustomerSettleBaseInfo();
            customerSettleBaseInfo.setId(customerSettleDB.getId());
            customerSettleBaseInfo.setFinancialOfficer(customerSettleDB.getFinancialOfficer());
            customerSettleBaseInfo.setSettleAccName(customerSettleDB.getSettleAccName());
            customerSettleBaseInfo.setSettleAccNo(customerSettleDB.getSettleAccNo());

            List<CustomerSettlePoi> settlePoiDBList = customerSettlePoiService.getBySettleIdAudited(customerSettleDB.getId());
            customerSettleBaseInfo.setSettlePoiRelNum(settlePoiDBList.size());

            settleList.add(customerSettleBaseInfo);
        }

        return new PageData.Builder<CustomerSettleBaseInfo>()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalSize((int) pageInfo.getTotal())
                .totalPage(pageInfo.getPages())
                .data(settleList)
                .build();
    }

    @Override
    public void saveOrUpdate(CustomerSettleAudited customerSettleAudited) {
        if (customerSettleAudited == null) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "保存更新线上结算异常");
        }

        CustomerSettleAuditedDB customerSettleAuditedDB = CustomerTransferUtil.transCustomerSettleAudited2DB(customerSettleAudited);
        if (customerSettleAuditedDB.getId() != null && customerSettleAuditedDB.getId() > 0) {
            customerSettleAuditedDBMapper.updateByIdSelective(customerSettleAuditedDB);
        } else {
            customerSettleAuditedDBMapper.insertSelective(customerSettleAuditedDB);
        }
    }
}
