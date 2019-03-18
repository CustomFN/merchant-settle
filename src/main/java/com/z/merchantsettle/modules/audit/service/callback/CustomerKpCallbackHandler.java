package com.z.merchantsettle.modules.audit.service.callback;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.modules.audit.domain.AuditResult;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.customer.AuditCustomerKp;
import com.z.merchantsettle.modules.customer.service.callback.CustomerCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerKpCallbackHandler extends AbstractHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerKpCallbackHandler.class);

    @Autowired
    private CustomerCallbackService customerCallbackService;

    @Override
    public void handleCallback(AuditResult result, AuditTask auditTask) {
        LOGGER.info("CustomerKpCallbackHandler auditResult = {}, auditTask = {}", JSON.toJSONString(result), JSON.toJSONString(auditTask));

        Integer auditStatus = result.getAuditStatus();
        String auditResult = result.getResult();
        Integer customerId = auditTask.getCustomerId();

        String auditData = auditTask.getAuditData();
        AuditCustomerKp auditCustomerKp = JSON.parseObject(auditData, AuditCustomerKp.class);
        Integer kpId = auditCustomerKp.getKpId();
        customerCallbackService.customerKpAuditCallback(customerId, kpId, auditStatus, auditResult);
    }
}
