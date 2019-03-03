package com.z.merchantsettle.modules.customer.service.callback;


public interface CustomerCallbackService {

    void customerAuditCallback(Integer customerId, Integer auditStatus, String auditResult);

    void customerKpAuditCallback(Integer customerId, Integer auditStatus, String auditResult);

    void customerContractAuditCallback(Integer customerId, Integer contractId, Integer auditStatus, String auditResult);

    void customerSettleAuditCallback(Integer customerId, Integer settleId, Integer auditStatus, String auditResult);
}
