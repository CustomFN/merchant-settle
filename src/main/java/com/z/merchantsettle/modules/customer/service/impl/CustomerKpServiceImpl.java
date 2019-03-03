package com.z.merchantsettle.modules.customer.service.impl;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.customer.AuditCustomerKp;
import com.z.merchantsettle.modules.audit.service.ApiAuditService;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.dao.CustomerKpDBMapper;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerKp;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerKpAudited;
import com.z.merchantsettle.modules.customer.domain.db.CustomerKpDB;
import com.z.merchantsettle.modules.customer.service.CustomerKpAuditedService;
import com.z.merchantsettle.modules.customer.service.CustomerKpService;
import com.z.merchantsettle.modules.customer.service.CustomerOpLogService;
import com.z.merchantsettle.utils.TransferUtil;
import com.z.merchantsettle.utils.transfer.customer.CustomerTransferUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerKpServiceImpl implements CustomerKpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerKpServiceImpl.class);

    @Autowired
    private CustomerKpDBMapper customerKpDBMapper;

    @Autowired
    private CustomerKpAuditedService customerKpAuditedService;

    @Autowired
    private ApiAuditService apiAuditService;

    @Autowired
    private CustomerOpLogService customerOpLogService;

    @Override
    public void saveOrUpdate(CustomerKp customerKp, String opUser) throws CustomerException {
        if (customerKp == null || StringUtils.isBlank(opUser)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        CustomerKpDB customerKpDB = CustomerTransferUtil.transCustomerKp2DB(customerKp);
        boolean isNew = !(customerKp.getId() != null && customerKp.getId() > 0);
        if (isNew) {
            customerKpDBMapper.updateByIdSelective(customerKpDB);
        } else {
            customerKpDBMapper.insertSelective(customerKpDB);
        }
        commitAudit(customerKp, opUser, isNew);

        customerOpLogService.addLog(customerKp.getCustomerId(), "KP", "保存客户KP，提交审核", opUser);
    }

    private void commitAudit(CustomerKp customerKp, String opUser, boolean isNew) {
        AuditTask auditTask = new AuditTask();
        auditTask.setCustomerId(customerKp.getCustomerId());
        auditTask.setAuditApplicationType(isNew ? AuditConstant.AuditApplicationType.AUDIT_NEW : AuditConstant.AuditApplicationType.AUDIT_UPDATE);
        auditTask.setAuditStatus(AuditConstant.AuditStatus.AUDITING);
        auditTask.setAuditType(AuditConstant.AuditType.CUSTOMER_KP);
        auditTask.setSubmitterId(opUser);

        AuditCustomerKp auditCustomerKp = new AuditCustomerKp();
        TransferUtil.transferAll(customerKp, auditCustomerKp);
        // TODO: 2019/2/3 根据银行id获取银行名称
        auditTask.setAuditData(JSON.toJSONString(auditCustomerKp));
        apiAuditService.commitAudit(auditTask);
    }


    @Override
    public CustomerKp getCustomerKpByCustomerId(Integer customerId, Integer effective) throws CustomerException {
        if (customerId == null || customerId <= 0 || effective < 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        CustomerKp customerKp;
        if (CustomerConstant.EFFECTIVE == effective) {
            CustomerKpAudited customerKpAudited = customerKpAuditedService.getCustomerKpByCustomerId(customerId);
            customerKp = transCustomerKpAudited2CustomerKp(customerKpAudited);
        } else {
            CustomerKpDB customerKpDB = customerKpDBMapper.selectByCustomerId(customerId);
            customerKp = CustomerTransferUtil.transCustomerKpDB2Bo(customerKpDB);
        }
        return customerKp;
    }

    @Override
    public void setupEffectCustomerKp(Integer customerId) throws CustomerException {
        LOGGER.info("setupEffectCustomerKp customerId = {}", customerId);

        CustomerKpDB customerKpDB = customerKpDBMapper.selectByCustomerId(customerId);
        if (customerKpDB == null) {
            return;
        }

        customerKpDB.setCustomerId(customerId);
        customerKpDB.setStatus(CustomerConstant.CustomerStatus.AUDIT_PASS.getCode());
        customerKpDBMapper.updateByCustomerIdSelective(customerKpDB);

        CustomerKp customerKp = CustomerTransferUtil.transCustomerKpDB2Bo(customerKpDB);
        CustomerKpAudited customerKpAudited = transCustomerKp2CustomerKpAudited(customerKp);
        customerKpAuditedService.saveOrUpdate(customerKpAudited);

        customerOpLogService.addLog(customerId, "KP", "设置客户KP生效", "系统()");
    }

    private CustomerKpAudited transCustomerKp2CustomerKpAudited(CustomerKp customerKp) {
        CustomerKpAudited customerKpAudited = new CustomerKpAudited();
        TransferUtil.transferAll(customerKp, customerKpAudited);
        return customerKpAudited;
    }

    private CustomerKp transCustomerKpAudited2CustomerKp(CustomerKpAudited customerKpAudited) {
        if (customerKpAudited == null) {
            return null;
        }

        CustomerKp customerKp = new CustomerKp();
        TransferUtil.transferAll(customerKpAudited, customerKp);
        return customerKp;
    }


}
