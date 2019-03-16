package com.z.merchantsettle.modules.audit.service.callback;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.modules.audit.domain.AuditResult;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.customer.AuditCustomerContract;
import com.z.merchantsettle.modules.customer.service.callback.CustomerCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerContractCallbackHandler extends AbstractHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerContractCallbackHandler.class);

    @Autowired
    private CustomerCallbackService customerCallbackService;

    @Override
    public void handleCallback(AuditResult result, AuditTask auditTask) {
        LOGGER.info("CustomerContractCallbackHandler auditResult = {}, auditTask = {}", JSON.toJSONString(result), JSON.toJSONString(auditTask));

        Integer auditStatus = result.getAuditStatus();
        String auditResult = result.getResult();
        Integer customerId = auditTask.getCustomerId();

        String auditData = auditTask.getAuditData();
        AuditCustomerContract auditCustomerContract = JSON.parseObject(auditData, AuditCustomerContract.class);
        Integer contractId = auditCustomerContract.getContractId();
        customerCallbackService.customerContractAuditCallback(customerId, contractId, auditStatus, auditResult);
    }
}
