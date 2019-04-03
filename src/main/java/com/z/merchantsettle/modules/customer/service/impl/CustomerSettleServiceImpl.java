package com.z.merchantsettle.modules.customer.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.z.merchantsettle.common.CertificatesTypeEnum;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.audit.constants.AuditApplicationTypeEnum;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.constants.AuditTypeEnum;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.customer.AuditCustomerSettle;
import com.z.merchantsettle.modules.audit.service.ApiAuditService;
import com.z.merchantsettle.modules.base.domain.bo.BankInfo;
import com.z.merchantsettle.modules.base.domain.bo.CityInfo;
import com.z.merchantsettle.modules.base.service.BankService;
import com.z.merchantsettle.modules.base.service.GeoService;
import com.z.merchantsettle.modules.customer.constants.*;
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
import com.z.merchantsettle.mq.MsgOpType;
import com.z.merchantsettle.mq.customer.CustomerSender;
import com.z.merchantsettle.utils.DiffUtil;
import com.z.merchantsettle.utils.TransferUtil;
import com.z.merchantsettle.utils.aliyun.AliyunUtil;
import com.z.merchantsettle.utils.transfer.customer.CustomerTransferUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    private CustomerSender customerSender;

    @Autowired
    private GeoService geoService;

    @Autowired
    private BankService bankService;

    private static final String CUSTOMER_SETTLE_TOPIC = "customer_settle_topic";

    @Override
    @Transactional
    public CustomerSettle saveOrUpdate(CustomerSettle customerSettle, String opUserId) {
        LOGGER.info("saveOrUpdate customerSettle = {}, opUserId = {}", JSON.toJSONString(customerSettle), opUserId);
        if (customerSettle == null || StringUtils.isBlank(opUserId) || StringUtils.isBlank(customerSettle.getWmPoiIds())) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }
        if (false) {
            checkCustomerSettle(customerSettle);
        }

        CustomerSettleDB customerSettleDB = CustomerTransferUtil.transCustomerSettle2DB(customerSettle);
        boolean isNew = (customerSettle.getId() == null || customerSettle.getId() <= 0);
        if (isNew) {
            customerSettleDB.setStatus(CustomerConstant.CustomerStatus.AUDITING.getCode());
            customerSettleDBMapper.insertSelective(customerSettleDB);

            customerSettle = CustomerTransferUtil.transCustomerSettleDB2Bo(customerSettleDB);
            String diff = DiffUtil.diff(null, customerSettle);
            customerOpLogService.addLog(customerSettle.getCustomerId(), "结算", diff, opUserId);
        } else {
            CustomerSettleDB customerSettleDBInDB = customerSettleDBMapper.selectById(customerSettle.getId());
            customerSettleDBMapper.updateByIdSelective(customerSettleDB);

            String diff = DiffUtil.diff(customerSettleDBInDB, customerSettle);
            customerOpLogService.addLog(customerSettle.getCustomerId(), "结算", diff, opUserId);
        }
        List<Integer> wmPoiIdList = Lists.transform(Lists.newArrayList(StringUtils.split(customerSettle.getWmPoiIds(), ",")), new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.parseInt(input);
            }
        });
        customerSettlePoiService.saveOrUpdateSettlePoi(customerSettleDB.getId(), wmPoiIdList);
        commitAudit(customerSettle, opUserId, isNew);
        customerOpLogService.addLog(customerSettle.getCustomerId(), "结算", "保存客户结算，提交审核", opUserId);
        return customerSettle;
    }

    private void checkCustomerSettle(CustomerSettle customerSettle) {
        boolean validResult = true;
        if (CertificatesTypeEnum.ID_CARD.getCode() == customerSettle.getFinancialOfficerCertificatesType()) {
            validResult = AliyunUtil.iDCardValid(customerSettle.getFinancialOfficerCertificatesNum(), customerSettle.getSettleAccName());
        }
        if (!validResult) {
            throw new CustomerException(CustomerConstant.CUSTOMER_VALID_ERROR, "客户结算负责人信息验证失败,请重新确认!");
        }

        validResult = AliyunUtil.bankCardValid(customerSettle.getSettleAccNo(), customerSettle.getFinancialOfficerCertificatesNum(), customerSettle.getSettleAccName());
        if (!validResult) {
            throw new CustomerException(CustomerConstant.CUSTOMER_VALID_ERROR, "客户结算银行信息验证失败,请重新确认!");
        }
    }

    private void commitAudit(CustomerSettle customerSettle, String opUserId, boolean isNew) {
        AuditTask auditTask = new AuditTask();
        auditTask.setCustomerId(customerSettle.getCustomerId());
        auditTask.setAuditApplicationType(isNew ? AuditApplicationTypeEnum.AUDIT_NEW.getCode() : AuditApplicationTypeEnum.AUDIT_UPDATE.getCode());
        auditTask.setAuditStatus(AuditConstant.AuditStatus.AUDITING);
        auditTask.setAuditType(AuditTypeEnum.CUSTOMER_SETTLE.getCode());
        auditTask.setSubmitterId(opUserId);

        AuditCustomerSettle auditCustomerSettle = new AuditCustomerSettle();
        TransferUtil.transferAll(customerSettle, auditCustomerSettle);

        auditCustomerSettle.setSettleId(customerSettle.getId());
        auditCustomerSettle.setSettleAccTypeStr(SettleAccTypeEnum.getByCode(customerSettle.getSettleAccType()));
        auditCustomerSettle.setFinancialOfficerCertificatesTypeStr(CertificatesTypeEnum.getByCode(customerSettle.getFinancialOfficerCertificatesType()));
        auditCustomerSettle.setSettleTypeStr(SettleTypeEnum.getByCode(customerSettle.getSettleType()));
        auditCustomerSettle.setSettleCycleStr(SettleCycleEnum.getByCode(customerSettle.getSettleCycle()));

        CityInfo cityInfo = geoService.getByProvinceIdAndCityId(customerSettle.getProvince(), customerSettle.getCity());
        BankInfo bankInfo = bankService.getByBankIdAndBranchId(customerSettle.getBankId(), customerSettle.getBranchId());
        StringBuilder bankName = new StringBuilder();
        if (cityInfo != null) {
            bankName.append(cityInfo.getProvinceName()).append("-").append(cityInfo.getCityName()).append("-");
        }
        if (bankInfo != null) {
            bankName.append(bankInfo.getBankName()).append("-").append(bankInfo.getSubBankName());
        }
        auditCustomerSettle.setBankName(bankName.toString());
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
    public PageData<CustomerSettleBaseInfo> getCustomerSettleList(Integer customerId, String settleOrPoiId, Integer effective, Integer pageNum, Integer pageSize) {
        if (customerId == null || customerId <= 0 || effective < 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        PageData<CustomerSettleBaseInfo> pageData;
        if (CustomerConstant.EFFECTIVE == effective) {
            pageData = customerSettleAuditedService.getCustomerSettleList(customerId, settleOrPoiId, pageNum, pageSize);
        } else {
            pageData = getCustomerSettleList(customerId, settleOrPoiId, pageNum, pageSize);
        }
        return pageData;
    }

    private PageData<CustomerSettleBaseInfo> getCustomerSettleList(Integer customerId, String settleOrPoiId, Integer pageNum, Integer pageSize) {
        List<Integer> settleIdList = Lists.newArrayList();
        if (StringUtils.isNotBlank(settleOrPoiId)) {
            List<Integer> settleIds = customerSettlePoiService.getSettleIdByWmPoiId(Integer.valueOf(settleOrPoiId));
            settleIdList.addAll(settleIds);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<CustomerSettleDB> settleDBList = customerSettleDBMapper.getSettleListByCustomerId(customerId, settleIdList);

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
    @Transactional
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

    @Override
    public void deleteByCustomerId(Integer customerId, String opUserId) throws CustomerException {
        LOGGER.info("CustomerSettleServiceImpl deleteByCustomerId customerId = {}, opUser = {}", customerId, opUserId);
        if (customerId == null || customerId <= 0 ) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        customerSettleDBMapper.deleteByCustomerId(customerId);
        customerSettleAuditedService.deleteByCustomerId(customerId);

        customerSender.send(CUSTOMER_SETTLE_TOPIC, customerId, MsgOpType.DELETE);
    }

    @Override
    public void updateByIdForAudit(CustomerSettle customerSettle, String opUserId) {
        LOGGER.info("updateByIdForAudit customerSettle = {}, opUserId = {}", JSON.toJSONString(customerSettle), opUserId);
        if (customerSettle == null || StringUtils.isBlank(opUserId)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        CustomerSettleDB customerSettleDB = customerSettleDBMapper.selectById(customerSettle.getId());
        if (customerSettleDB == null) {
            throw new CustomerException(CustomerConstant.CUSTOMER_OP_ERROR, "更新客户结算审核状态异常");
        }
        customerSettleDB.setStatus(customerSettle.getStatus());
        customerSettleDB.setAuditResult(customerSettle.getAuditResult());
        customerSettleDBMapper.updateByIdSelective(customerSettleDB);
        String log = "审核结果:审核驳回:" + customerSettleDB.getAuditResult();
        customerOpLogService.addLog(customerSettleDB.getCustomerId(), "结算", log, "系统()");
    }
}
