package com.z.merchantsettle.modules.customer.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.customer.AuditCustomerContract;
import com.z.merchantsettle.modules.audit.service.ApiAuditService;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.dao.CustomerContractDBMapper;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerContract;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerContractAudited;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSigner;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSignerAudited;
import com.z.merchantsettle.modules.customer.domain.db.CustomerContractDB;
import com.z.merchantsettle.modules.customer.service.*;
import com.z.merchantsettle.utils.TransferUtil;
import com.z.merchantsettle.utils.transfer.customer.CustomerTransferUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerContractServiceImpl implements CustomerContractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerContractServiceImpl.class);

    @Autowired
    private CustomerContractDBMapper customerContractDBMapper;

    @Autowired
    private CustomerContractAuditedService customerContractAuditedService;

    @Autowired
    private CustomerSignerService customerSignerService;

    @Autowired
    private CustomerSignerAuditedService customerSignerAuditedService;

    @Autowired
    private ApiAuditService apiAuditService;

    @Autowired
    private CustomerOpLogService customerOpLogService;

    @Override
    public void saveOrUpdate(CustomerContract customerContract, String opUser) throws CustomerException {
        if (customerContract == null || customerContract.getPartyA() == null ||
                customerContract.getPartyB() == null || StringUtils.isBlank(opUser)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }


        CustomerSigner partyA = customerContract.getPartyA();
        CustomerSigner partyB = customerContract.getPartyB();
        CustomerContractDB customerContractDB = CustomerTransferUtil.transCustomerContract2DB(customerContract);
        boolean isNew = !(customerContract.getId() != null && customerContract.getId() >= 0);
        if (isNew) {
            customerContractDBMapper.updateByIdSelective(customerContractDB);
            customerSignerService.updateByIdSelective(Lists.newArrayList(partyA, partyB));
        } else {
            customerContractDBMapper.insertSelective(customerContractDB);
            customerSignerService.insertSelective(Lists.newArrayList(partyA, partyB));
        }
        commitAudit(customerContract, opUser, isNew);
        customerOpLogService.addLog(customerContract.getCustomerId(), "合同", "保存合同，提交审核", opUser);
    }

    private void commitAudit(CustomerContract customerContract, String opUser, boolean isNew) {
        AuditTask auditTask = new AuditTask();
        auditTask.setCustomerId(customerContract.getCustomerId());
        auditTask.setAuditApplicationType(isNew ? AuditConstant.AuditApplicationType.AUDIT_NEW : AuditConstant.AuditApplicationType.AUDIT_UPDATE);
        auditTask.setAuditStatus(AuditConstant.AuditStatus.AUDITING);
        auditTask.setAuditType(AuditConstant.AuditType.CUSTOMER_KP);
        auditTask.setSubmitterId(opUser);

        AuditCustomerContract auditCustomerContract = new AuditCustomerContract();
        TransferUtil.transferAll(customerContract, auditCustomerContract);

        auditCustomerContract.setContractId(customerContract.getId());
        CustomerSigner partyA = customerContract.getPartyA();
        auditCustomerContract.setPartyA(partyA.getParty());
        auditCustomerContract.setPartyAContactPerson(partyA.getPartyContactPerson());
        auditCustomerContract.setPartyAContactPersonPhone(partyA.getPartyContactPersonPhone());
        auditCustomerContract.setPartyASignTime(partyA.getSignTime());
        CustomerSigner partyB = customerContract.getPartyB();
        auditCustomerContract.setPartyB(partyB.getParty());
        auditCustomerContract.setPartyBContactPerson(partyB.getPartyContactPerson());
        auditCustomerContract.setPartyBContactPersonPhone(partyB.getPartyContactPersonPhone());
        auditCustomerContract.setPartyBSignTime(partyB.getSignTime());

        auditTask.setAuditData(JSON.toJSONString(auditCustomerContract));
        apiAuditService.commitAudit(auditTask);
    }

    @Override
    public CustomerContract getCustomerContractById(Integer contractId, Integer effective) throws CustomerException {
        if (contractId == null || contractId <= 0 || effective < 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        CustomerContract customerContract;
        if (CustomerConstant.EFFECTIVE == effective) {
            CustomerContractAudited customerContractAudited = customerContractAuditedService.getCustomerContractById(contractId);
            customerContract = transContractAudited2Contract(customerContractAudited);

            List<CustomerSignerAudited> signerAuditedList =  customerSignerAuditedService.getCustomerSignerByContractId(contractId);
            for (CustomerSignerAudited customerSignerAudited : signerAuditedList) {
                if (CustomerConstant.SIGNER_LABEL_A.equals(customerSignerAudited.getSignerLabel())) {
                    customerContract.setPartyA(transSignerAudited2Signer(customerSignerAudited));
                } else {
                    customerContract.setPartyB(transSignerAudited2Signer(customerSignerAudited));
                }
            }
        } else {
            CustomerContractDB customerContractDB = customerContractDBMapper.selectById(contractId);
            customerContract = CustomerTransferUtil.transCustomerContractDB2Bo(customerContractDB);

            List<CustomerSigner> signerList =  customerSignerService.getCustomerSignerByContractId(contractId);
            for (CustomerSigner customerSigner : signerList) {
                if (CustomerConstant.SIGNER_LABEL_A.equals(customerSigner.getSignerLabel())) {
                    customerContract.setPartyA(customerSigner);
                } else {
                    customerContract.setPartyB(customerSigner);
                }
            }
        }
        return customerContract;
    }

    private CustomerSigner transSignerAudited2Signer(CustomerSignerAudited customerSignerAudited) {
        if (customerSignerAudited == null) {
            return null;
        }

        CustomerSigner customerSigner = new CustomerSigner();
        TransferUtil.transferAll(customerSignerAudited, customerSigner);
        return customerSigner;
    }

    private CustomerContract transContractAudited2Contract(CustomerContractAudited customerContractAudited) {
        if (customerContractAudited == null) {
            return null;
        }

        CustomerContract customerContract = new CustomerContract();
        TransferUtil.transferAll(customerContractAudited, customerContract);
        return customerContract;
    }

    @Override
    public void setupEffectCustomerContract(Integer customerId, Integer contractId) throws CustomerException {
        LOGGER.info("setupEffectCustomerContract customerId = {}, contractId = {}", customerId, contractId);

        CustomerContractDB customerContractDB = customerContractDBMapper.selectByCustomerId(customerId, contractId);
        if (customerContractDB == null) {
            return;
        }

        customerContractDB.setStatus(CustomerConstant.CustomerStatus.AUDIT_PASS.getCode());
        customerContractDBMapper.updateByIdSelective(customerContractDB);
        List<CustomerSigner> signerList = customerSignerService.getCustomerSignerByContractId(customerContractDB.getId());
        CustomerContract customerContract = CustomerTransferUtil.transCustomerContractDB2Bo(customerContractDB);
        for (CustomerSigner customerSigner : signerList) {
            if (CustomerConstant.SIGNER_LABEL_A.equals(customerSigner.getSignerLabel())) {
                customerContract.setPartyA(customerSigner);
            } else {
                customerContract.setPartyB(customerSigner);
            }
        }

        CustomerContractAudited customerContractAudited = transCustomerContract2CustomerContractAudited(customerContract);
        customerContractAuditedService.saveOrUpdate(customerContractAudited);

        customerOpLogService.addLog(customerId, "合同", "设置合同生效", "系统()");
    }

    private CustomerContractAudited transCustomerContract2CustomerContractAudited(CustomerContract customerContract) {
        CustomerContractAudited customerContractAudited = new CustomerContractAudited();
        TransferUtil.transferAll(customerContract, customerContractAudited);

        CustomerSigner partyA = customerContract.getPartyA();
        CustomerSignerAudited partyAAudited = transCustomerSigner2CustomerSignerAudited(partyA);

        CustomerSigner partyB = customerContract.getPartyB();
        CustomerSignerAudited partyBAudited = transCustomerSigner2CustomerSignerAudited(partyB);

        customerContractAudited.setPartyA(partyAAudited);
        customerContractAudited.setPartyB(partyBAudited);
        return customerContractAudited;
    }

    private CustomerSignerAudited transCustomerSigner2CustomerSignerAudited(CustomerSigner customerSigner) {
        CustomerSignerAudited customerSignerAudited = new CustomerSignerAudited();
        TransferUtil.transferAll(customerSigner, customerSignerAudited);
        return customerSignerAudited;
    }
}
