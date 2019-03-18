package com.z.merchantsettle.modules.customer.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.audit.constants.AuditApplicationTypeEnum;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.constants.AuditTypeEnum;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.customer.AuditCustomerContract;
import com.z.merchantsettle.modules.audit.service.ApiAuditService;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.dao.CustomerContractDBMapper;
import com.z.merchantsettle.modules.customer.domain.ContractRequestParam;
import com.z.merchantsettle.modules.customer.domain.bo.*;
import com.z.merchantsettle.modules.customer.domain.db.CustomerContractAuditedDB;
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
import java.util.Map;

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
    public PageData<ContractBaseInfo> getCustomerContractList(Integer customerId) throws CustomerException {
        if (customerId == null || customerId <= 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }
        ContractRequestParam param = new ContractRequestParam();
        param.setCustomerId(customerId);
        return getContractBaseInfoList(param, 1, 10);
    }

    @Override
    public CustomerContract saveOrUpdate(CustomerContract customerContract, String opUser) throws CustomerException {
        if (customerContract == null || customerContract.getPartyA() == null ||
                customerContract.getPartyB() == null || StringUtils.isBlank(opUser)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }


        CustomerSigner partyA = customerContract.getPartyA();
        CustomerSigner partyB = customerContract.getPartyB();
        CustomerContractDB customerContractDB = CustomerTransferUtil.transCustomerContract2DB(customerContract);
        boolean isNew = (customerContract.getId() == null || customerContract.getId() <= 0);
        if (isNew) {
            customerContractDB.setStatus(CustomerConstant.CustomerStatus.AUDITING.getCode());
            customerContractDBMapper.insertSelective(customerContractDB);

            partyA.setContractId(customerContractDB.getId());
            partyB.setContractId(customerContractDB.getId());
            customerSignerService.insertSelective(Lists.newArrayList(partyA, partyB));

            customerContract = CustomerTransferUtil.transCustomerContractDB2Bo(customerContractDB);
            customerContract.setPartyA(partyA);
            customerContract.setPartyB(partyB);
        } else {
            customerContractDBMapper.updateByIdSelective(customerContractDB);
            customerSignerService.updateByIdSelective(Lists.newArrayList(partyA, partyB));
        }
        commitAudit(customerContract, opUser, isNew);
        customerOpLogService.addLog(customerContract.getCustomerId(), "合同", "保存合同，提交审核", opUser);
        return customerContract;
    }

    private void commitAudit(CustomerContract customerContract, String opUser, boolean isNew) {
        AuditTask auditTask = new AuditTask();
        auditTask.setCustomerId(customerContract.getCustomerId());
        auditTask.setAuditApplicationType(isNew ? AuditApplicationTypeEnum.AUDIT_NEW.getCode() : AuditApplicationTypeEnum.AUDIT_UPDATE.getCode());
        auditTask.setAuditStatus(AuditConstant.AuditStatus.AUDITING);
        auditTask.setAuditType(AuditTypeEnum.CUSTOMER_CONTRACT.getCode());
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

    @Override
    public PageData<ContractBaseInfo> getContractBaseInfoList(ContractRequestParam contractRequestParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CustomerContractDB> contractList = customerContractDBMapper.getContractList(contractRequestParam);
        PageInfo<CustomerContractDB> pageInfo = new PageInfo<>(contractList);

        List<Integer> contractIdList = Lists.transform(contractList, new Function<CustomerContractDB, Integer>() {
            @Override
            public Integer apply(CustomerContractDB input) {
                return input.getId();
            }
        });
        Map<Integer, List<CustomerSigner>> customerSignerMap = customerSignerService.getCustomerSignerByContractIdList(contractIdList);

        List<ContractBaseInfo> contractBaseInfoList = Lists.newArrayList();
        for (CustomerContractDB customerContractDB : contractList) {
            ContractBaseInfo contractBaseInfo = new ContractBaseInfo();
            contractBaseInfo.setContractId(customerContractDB.getId());
            contractBaseInfo.setContractNum(customerContractDB.getCustomerContractNum());
            contractBaseInfo.setAuditStatus(CustomerConstant.CustomerStatus.getByCode(customerContractDB.getStatus()));
            contractBaseInfo.setContractType("纸质合同");
            contractBaseInfo.setContractValidTime(customerContractDB.getContractEndTime());

            List<CustomerSigner> customerSignerList = customerSignerMap.get(customerContractDB.getId());
            for (CustomerSigner customerSigner : customerSignerList) {
                if (CustomerConstant.SIGNER_LABEL_A.equals(customerSigner.getSignerLabel())) {
                    contractBaseInfo.setPartyAName(customerSigner.getParty());
                    contractBaseInfo.setPartyASignerName(customerSigner.getPartyContactPerson());
                } else {
                    contractBaseInfo.setPartyBSignerName(customerSigner.getPartyContactPerson());
                    contractBaseInfo.setPrincipal(customerSigner.getPartyContactPerson());
                    contractBaseInfo.setSignerTime(customerSigner.getSignTime());
                }
            }

            contractBaseInfoList.add(contractBaseInfo);
        }
        return new PageData.Builder<ContractBaseInfo>()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalSize((int) pageInfo.getTotal())
                .totalPage(pageInfo.getPages())
                .data(contractBaseInfoList)
                .build();
    }

    @Override
    public void deleteByCustomerId(Integer customerId, String opUserId) throws CustomerException {
        LOGGER.info("CustomerContractServiceImpl deleteByCustomerId customerId = {}, opUser = {}", customerId, opUserId);
        if (customerId == null || customerId <= 0 ) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        customerContractDBMapper.deleteByCustomerId(customerId);
        customerContractAuditedService.deleteByCustomerId(customerId);
    }

    @Override
    public void updateByIdForAudit(CustomerContract customerContract, String opUserId) {
        LOGGER.info("updateByIdForAudit customerContract = {}, opUserId = {}", JSON.toJSONString(customerContract), opUserId);
        if (customerContract == null || customerContract.getPartyA() == null ||
                customerContract.getPartyB() == null || StringUtils.isBlank(opUserId)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        CustomerContractDB customerContractDB = customerContractDBMapper.selectById(customerContract.getId());
        if (customerContractDB == null) {
            throw new CustomerException(CustomerConstant.CUSTOMER_OP_ERROR, "更新客户合同审核状态异常");
        }
        customerContractDB.setStatus(customerContract.getStatus());
        customerContractDB.setAuditResult(customerContract.getAuditResult());
        customerContractDBMapper.updateByIdSelective(customerContractDB);
        String log = "审核结果:审核驳回:" + customerContractDB.getAuditResult();
        customerOpLogService.addLog(customerContractDB.getCustomerId(), "合同", log, "系统()");
    }
}
