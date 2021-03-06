package com.z.merchantsettle.modules.audit.service.callback;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.modules.audit.domain.AuditResult;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.customer.service.callback.CustomerCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerCallbackHandler extends AbstractHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerCallbackHandler.class);

    @Autowired
    private CustomerCallbackService customerCallbackService;

    @Override
    public void handleCallback(AuditResult result, AuditTask auditTask) {
        LOGGER.info("CustomerCallbackHandler auditResult = {}, auditTask = {}", JSON.toJSONString(result), JSON.toJSONString(auditTask));

        Integer auditStatus = result.getAuditStatus();
        String auditResult = result.getResult();
        Integer customerId = auditTask.getCustomerId();
        customerCallbackService.customerAuditCallback(customerId, auditStatus, auditResult);
    }
}
