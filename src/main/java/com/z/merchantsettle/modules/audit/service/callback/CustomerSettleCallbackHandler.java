package com.z.merchantsettle.modules.audit.service.callback;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.modules.audit.domain.AuditResult;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.customer.AuditCustomerSettle;
import com.z.merchantsettle.modules.customer.service.callback.CustomerCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerSettleCallbackHandler extends AbstractHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerSettleCallbackHandler.class);

    @Autowired
    private CustomerCallbackService customerCallbackService;

    @Override
    public void handleCallback(AuditResult result, AuditTask auditTask) {
        LOGGER.info("CustomerSettleCallbackHandler auditResult = {}, auditTask = {}", JSON.toJSONString(result), JSON.toJSONString(auditTask));

        Integer auditStatus = result.getAuditStatus();
        String auditResult = result.getResult();
        Integer customerId = auditTask.getCustomerId();

        String auditData = auditTask.getAuditData();
        AuditCustomerSettle auditCustomerSettle = JSON.parseObject(auditData, AuditCustomerSettle.class);
        Integer settleId = auditCustomerSettle.getSettleId();
        customerCallbackService.customerSettleAuditCallback(customerId, settleId, auditStatus, auditResult);
    }
}
