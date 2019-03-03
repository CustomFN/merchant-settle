package com.z.merchantsettle.modules.customer.controller;


import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.common.ValidationResult;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.exception.UpmException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerContract;
import com.z.merchantsettle.modules.customer.service.CustomerContractAuditedService;
import com.z.merchantsettle.modules.customer.service.CustomerContractService;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.utils.ValidationUtil;
import com.z.merchantsettle.utils.shiro.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/contract")
public class CustomerContractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerKpController.class);

    @Autowired
    private CustomerContractService customerContractService;

    @Autowired
    private CustomerContractAuditedService customerContractAuditedService;


    @RequestMapping("/save")
    public Object saveOrUpdate(@RequestBody CustomerContract customerContract) {
        LOGGER.info("saveOrUpdate customerContract = {}" , JSON.toJSONString(customerContract));
        if (customerContract == null || customerContract.getPartyA() == null || customerContract.getPartyB() == null) {
            return ReturnResult.fail(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        ValidationResult validateResult = ValidationUtil.validate(customerContract);
        if (validateResult.isHasError()) {
            return ReturnResult.fail(validateResult.getErrorMsg());
        }

        try {
            User user = ShiroUtils.getSysUser();
            customerContractService.saveOrUpdate(customerContract, user.getUserId());
            return ReturnResult.success();
        } catch (UpmException | CustomerException e) {
            LOGGER.error("保存客户合同失败", e);
            return ReturnResult.fail(CustomerConstant.CUSTOMER_OP_ERROR, "保存失败");
        }
    }

    @RequestMapping("/show/{contractId}")
    public Object getCustomerContractById(@PathVariable(name = "contractId") Integer contractId,
                                          @RequestParam(name = "effective") Integer effective) {
        if (contractId == null || contractId <= 0 || effective < 0) {
            return ReturnResult.fail(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        try {
            CustomerContract customerContract = customerContractService.getCustomerContractById(contractId, effective);
            return ReturnResult.success(customerContract);
        } catch (CustomerException e) {
            LOGGER.error("查询客户合同异常 contractId = {}", contractId, e);
            return ReturnResult.fail(CustomerConstant.CUSTOMER_OP_ERROR, "查询合同异常");
        }
    }

    @RequestMapping("/list")
    public Object getCustomerContractList(@RequestParam("customerId") Integer customerId) {
        if (customerId == null || customerId <= 0) {
            return ReturnResult.fail(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        try {
            return ReturnResult.success(customerContractAuditedService.getCustomerContractList(customerId));
        } catch (CustomerException e) {
            LOGGER.error("获取客户合同列表失败 customerId = {}", customerId, e);
            return ReturnResult.fail(CustomerConstant.CUSTOMER_OP_ERROR, "查询合同列表异常");
        }
    }
}
