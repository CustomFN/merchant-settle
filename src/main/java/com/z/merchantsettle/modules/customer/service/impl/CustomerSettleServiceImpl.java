package com.z.merchantsettle.modules.customer.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.customer.AuditCustomerSettle;
import com.z.merchantsettle.modules.audit.service.ApiAuditService;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.dao.CustomerSettleDBMapper;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettle;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettleAudited;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettleBaseInfo;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettlePoi;
import com.z.merchantsettle.modules.customer.domain.db.CustomerSettleDB;
import com.z.merchantsettle.modules.customer.service.CustomerOpLogService;
import com.z.merchantsettle.modules.customer.service.CustomerSettleAuditedService;
import com.z.merchantsettle.modules.customer.service.CustomerSettlePoiService;
import com.z.merchantsettle.modules.customer.service.CustomerSettleService;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiBaseInfo;
import com.z.merchantsettle.modules.poi.service.WmPoiBaseInfoService;
import com.z.merchantsettle.utils.TransferUtil;
import com.z.merchantsettle.utils.transfer.customer.CustomerTransferUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerSettleServiceImpl implements CustomerSettleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerSettleServiceImpl.class);

    @Autowired
    private CustomerSettleDBMapper customerSettleDBMapper;

    @Autowired
    private CustomerOpLogService customerOpLogService;

    @Autowired
    private ApiAuditService apiAuditService;

    @Autowired
    private CustomerSettleAuditedService customerSettleAuditedService;

    @Autowired
    private CustomerSettlePoiService customerSettlePoiService;

    @Autowired
    private WmPoiBaseInfoService wmPoiBaseInfoService;


    @Override
    public void saveOrUpdate(CustomerSettle customerSettle, String opUserId) {
        if (customerSettle == null || StringUtils.isBlank(opUserId) || CollectionUtils.isEmpty(customerSettle.getWmPoiIdList())) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        CustomerSettleDB customerSettleDB = CustomerTransferUtil.transCustomerSettle2DB(customerSettle);
        boolean isNew = !(customerSettleDB.getId() != null && customerSettleDB.getId() > 0);
        if (isNew) {
            customerSettleDBMapper.insertSelective(customerSettleDB);
        } else {
            customerSettleDBMapper.updateByIdSelective(customerSettleDB);
        }
        customerSettlePoiService.saveOrUpdateSettlePoi(customerSettleDB.getId(), customerSettle.getWmPoiIdList());
        commitAudit(customerSettle, opUserId, isNew);

        customerOpLogService.addLog(customerSettle.getCustomerId(), "结算", "保存客户结算，提交审核", opUserId);
    }

    private void commitAudit(CustomerSettle customerSettle, String opUserId, boolean isNew) {
        AuditTask auditTask = new AuditTask();
        auditTask.setCustomerId(customerSettle.getCustomerId());
        auditTask.setAuditApplicationType(isNew ? AuditConstant.AuditApplicationType.AUDIT_NEW : AuditConstant.AuditApplicationType.AUDIT_UPDATE);
        auditTask.setAuditStatus(AuditConstant.AuditStatus.AUDITING);
        auditTask.setAuditType(AuditConstant.AuditType.CUSTOMER_SETTLE);
        auditTask.setSubmitterId(opUserId);

        AuditCustomerSettle auditCustomerSettle = new AuditCustomerSettle();
        TransferUtil.transferAll(customerSettle, auditCustomerSettle);

        auditCustomerSettle.setSettleId(customerSettle.getId());
        List<WmPoiBaseInfo> wmPoiBaseInfoList = wmPoiBaseInfoService.getByIdList(customerSettle.getWmPoiIdList());
        List<String> wmPoiNameList = Lists.transform(wmPoiBaseInfoList, new Function<WmPoiBaseInfo, String>() {
            @Override
            public String apply(WmPoiBaseInfo input) {
                return input.getWmPoiName();
            }
        });
        auditCustomerSettle.setWmPoiName(wmPoiNameList);

        auditTask.setAuditData(JSON.toJSONString(auditCustomerSettle));
        apiAuditService.commitAudit(auditTask);
    }

    @Override
    public CustomerSettle getCustomerSettleBySettleId(Integer settleId, Integer effective) {
        if (settleId == null || settleId <= 0 || effective < 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        CustomerSettle customerSettle;
        if (CustomerConstant.EFFECTIVE == effective) {
            CustomerSettleAudited customerSettleAudited = customerSettleAuditedService.getCustomerSettleBySettleId(settleId);
            customerSettle = transCustomerSettleAudited2CustomerSettle(customerSettleAudited);
        } else {
            CustomerSettleDB customerSettleDB = customerSettleDBMapper.selectById(settleId);
            customerSettle = CustomerTransferUtil.transCustomerSettleDB2Bo(customerSettleDB);
        }
        return customerSettle;
    }

    private CustomerSettle transCustomerSettleAudited2CustomerSettle(CustomerSettleAudited customerSettleAudited) {
        if (customerSettleAudited == null) {
            return null;
        }

        CustomerSettle customerSettle = new CustomerSettle();
        TransferUtil.transferAll(customerSettleAudited, customerSettle);
        return customerSettle;
    }

    @Override
    public PageData<CustomerSettleBaseInfo> getCustomerSettleList(String settleOrPoiId, Integer effective, Integer pageNum, Integer pageSize) {
        if (effective < 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        PageData<CustomerSettleBaseInfo> pageData;
        if (CustomerConstant.EFFECTIVE == effective) {
            pageData = customerSettleAuditedService.getCustomerSettleList(settleOrPoiId, pageNum, pageSize);
        } else {
            pageData = getCustomerSettleList(settleOrPoiId, pageNum, pageSize);
        }
        return pageData;
    }

    private PageData<CustomerSettleBaseInfo> getCustomerSettleList(String settleOrPoiId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<Integer> settleIdList = Lists.newArrayList();
        if (StringUtils.isNotBlank(settleOrPoiId)) {
            List<Integer> settleIds = customerSettlePoiService.getSettleIdByWmPoiId(Integer.valueOf(settleOrPoiId));
            settleIdList.addAll(settleIds);
        }
        List<CustomerSettleDB> settleDBList = customerSettleDBMapper.getSettleList(settleIdList);

        PageInfo<CustomerSettleDB> pageInfo = new PageInfo<>(settleDBList);

        List<CustomerSettleBaseInfo> settleList = Lists.newArrayList();
        for (CustomerSettleDB customerSettleDB : settleDBList) {
            CustomerSettleBaseInfo customerSettleBaseInfo = new CustomerSettleBaseInfo();
            customerSettleBaseInfo.setId(customerSettleDB.getId());
            customerSettleBaseInfo.setFinancialOfficer(customerSettleDB.getFinancialOfficer());
            customerSettleBaseInfo.setSettleAccName(customerSettleDB.getSettleAccName());
            customerSettleBaseInfo.setSettleAccNo(customerSettleDB.getSettleAccNo());

            List<CustomerSettlePoi> settlePoiDBList = customerSettlePoiService.getBySettleId(customerSettleDB.getId());
            customerSettleBaseInfo.setSettlePoiRelNum(settlePoiDBList.size());

            settleList.add(customerSettleBaseInfo);
        }

        return new PageData.Builder<CustomerSettleBaseInfo>()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalSize((int) pageInfo.getTotal())
                .totalPage(pageInfo.getPageSize())
                .data(settleList)
                .build();
    }

    @Override
    public void deleteBySettleId(Integer settleId) {
        if (settleId == null || settleId <= 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        customerSettleDBMapper.deleteById(settleId);
    }

    @Override
    public void setupEffectCustomerSettle(Integer customerId, Integer settleId) {
        LOGGER.info("setupEffectCustomerSettle customerId = {}, settleId = {}", customerId, settleId);

        CustomerSettleDB customerSettleDB = customerSettleDBMapper.selectById(settleId);
        if(customerSettleDB == null) {
            return;
        }
        customerSettleDB.setStatus(CustomerConstant.CustomerStatus.AUDIT_PASS.getCode());
        customerSettleDBMapper.updateByIdSelective(customerSettleDB);

        CustomerSettle customerSettle = CustomerTransferUtil.transCustomerSettleDB2Bo(customerSettleDB);
        CustomerSettleAudited customerSettleAudited = transCustomerSettle2Audited(customerSettle);
        customerSettleAuditedService.saveOrUpdate(customerSettleAudited);

        List<CustomerSettlePoi> customerSettlePoiList = customerSettlePoiService.getBySettleId(settleId);
        customerSettlePoiService.saveOrUpdateSettlePoiAudited(customerSettlePoiList);

        customerOpLogService.addLog(customerId, "结算", "设置客户结算生效", "系统()");
    }

    @Override
    public List<CustomerSettle> getBySettleIdList(List<Integer> customerSettleIdList) {
        if (CollectionUtils.isEmpty(customerSettleIdList)) {
            return Lists.newArrayList();
        }

        List<CustomerSettleDB> customerSettleDBList = customerSettleDBMapper.getSettleList(customerSettleIdList);
        return CustomerTransferUtil.transCustomerSettleDBList2BoList(customerSettleDBList);
    }

    private CustomerSettleAudited transCustomerSettle2Audited(CustomerSettle customerSettle) {
        if (customerSettle == null) {
            return null;
        }

        CustomerSettleAudited customerSettleAudited = new CustomerSettleAudited();
        TransferUtil.transferAll(customerSettle, customerSettleAudited);
        return customerSettleAudited;
    }
}
