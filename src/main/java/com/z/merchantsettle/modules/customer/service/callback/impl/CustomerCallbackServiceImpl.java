package com.z.merchantsettle.modules.customer.service.callback.impl;

import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.domain.bo.Customer;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerContract;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerKp;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettle;
import com.z.merchantsettle.modules.customer.service.CustomerContractService;
import com.z.merchantsettle.modules.customer.service.CustomerKpService;
import com.z.merchantsettle.modules.customer.service.CustomerService;
import com.z.merchantsettle.modules.customer.service.CustomerSettleService;
import com.z.merchantsettle.modules.customer.service.callback.CustomerCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerCallbackServiceImpl implements CustomerCallbackService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerCallbackServiceImpl.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerKpService customerKpService;

    @Autowired
    private CustomerContractService customerContractService;

    @Autowired
    private CustomerSettleService customerSettleService;

    private static final int retryTime = 3;

    @Override
    public void customerAuditCallback(Integer customerId, Integer auditStatus, String auditResult) {
        LOGGER.info("customerAuditCallback customerId = {}, auditStatus = {}, auditResult = {}", customerId, auditStatus, auditResult);
        int retry = 0;
        boolean isRetry = true;

        while (isRetry && retry++ <= retryTime) {
            try {
                if (AuditConstant.AuditStatus.AUDIT_PASS == auditStatus) {
                    customerService.setupEffectCustomer(customerId);
                } else {
                    Customer customer = new Customer();
                    customer.setId(customerId);
                    customer.setStatus(CustomerConstant.CustomerStatus.AUDIT_REJECT.getCode());
                    customer.setAuditResult(auditResult);
                    customerService.saveOrUpdate(customer, "审核系统()");
                }
                isRetry = false;
            } catch (CustomerException e) {
                LOGGER.warn("审核任务回调客户失败,重试第 {} 次", retry, e);
            }
        }

    }

    @Override
    public void customerKpAuditCallback(Integer customerId, Integer auditStatus, String auditResult) {
        LOGGER.info("customerKpAuditCallback customerId = {}, auditStatus = {}, auditResult = {}", customerId, auditStatus, auditResult);
        int retry = 0;
        boolean isRetry = true;

        while (isRetry && retry++ <= retryTime) {
            try {
                if (AuditConstant.AuditStatus.AUDIT_PASS == auditStatus) {
                    customerKpService.setupEffectCustomerKp(customerId);
                } else {
                    CustomerKp customerKp = new CustomerKp();
                    customerKp.setCustomerId(customerId);
                    customerKp.setStatus(CustomerConstant.CustomerStatus.AUDIT_REJECT.getCode());
                    customerKp.setAuditResult(auditResult);
                    customerKpService.saveOrUpdate(customerKp, "审核系统()");
                }
                isRetry = false;
            } catch (CustomerException e) {
                LOGGER.warn("审核任务回调客户KP失败,重试第 {} 次", retry, e);
            }
        }
    }

    @Override
    public void customerContractAuditCallback(Integer customerId, Integer contractId, Integer auditStatus, String auditResult) {
        LOGGER.info("customerContractAuditCallback customerId = {}, contractId = {}, auditStatus = {}, auditResult = {}", customerId, contractId, auditStatus, auditResult);
        int retry = 0;
        boolean isRetry = true;

        while (isRetry && retry++ <= retryTime) {
            try {
                if (AuditConstant.AuditStatus.AUDIT_PASS == auditStatus) {
                    customerContractService.setupEffectCustomerContract(customerId, contractId);
                } else {
                    CustomerContract customerContract = new CustomerContract();
                    customerContract.setCustomerId(customerId);
                    customerContract.setStatus(CustomerConstant.CustomerStatus.AUDIT_REJECT.getCode());
                    customerContract.setAuditResult(auditResult);
                    customerContractService.saveOrUpdate(customerContract, "审核系统()");
                }
                isRetry = false;
            } catch (CustomerException e) {
                LOGGER.warn("审核任务回调客户合同失败,重试第 {} 次", retry, e);
            }
        }
    }

    @Override
    public void customerSettleAuditCallback(Integer customerId, Integer settleId, Integer auditStatus, String auditResult) {
        LOGGER.info("customerSettleAuditCallback customerId = {}, settleId = {}, auditStatus = {}, auditResult = {}", customerId, settleId, auditStatus, auditResult);
        int retry = 0;
        boolean isRetry = true;

        while (isRetry && retry++ <= retryTime) {
            try {
                if (AuditConstant.AuditStatus.AUDIT_PASS == auditStatus) {
                    customerSettleService.setupEffectCustomerSettle(customerId, settleId);
                } else {
                    CustomerSettle customerSettle = new CustomerSettle();
                    customerSettle.setCustomerId(customerId);
                    customerSettle.setStatus(CustomerConstant.CustomerStatus.AUDIT_REJECT.getCode());
                    customerSettle.setAuditResult(auditResult);
                    customerSettleService.saveOrUpdate(customerSettle, "审核系统()");
                }
                isRetry = false;
            } catch (CustomerException e) {
                LOGGER.warn("审核任务回调客户结算失败,重试第 {} 次", retry, e);
            }
        }
    }
}
