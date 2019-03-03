package com.z.merchantsettle.modules.customer.controller;

import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.domain.ContractRequestParam;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerContract;
import com.z.merchantsettle.modules.customer.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/contract")
@RestController
public class ContractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContractController.class);

    @Autowired
    private ContractService contractService;


    @RequestMapping("/list")
    public Object list(ContractRequestParam contractRequestParam,
                       @RequestParam(defaultValue = "1", name = "pageNum") Integer pageNum,
                       @RequestParam(defaultValue = "30", name = "pageSize") Integer pageSize) {

        if (pageNum == null || pageNum < 1 || pageSize == null || pageSize < 1) {
            return ReturnResult.fail("参数错误");
        }
        return ReturnResult.success(contractService.getContractList(contractRequestParam, pageNum, pageSize));
    }

    @RequestMapping("/show/{contractId}")
    public Object getContractByContractId(@PathVariable(name = "contractId") Integer contractId, Integer effective) {
        if (contractId == null || contractId <= 0 || effective < 0) {
            return ReturnResult.fail(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        try {
            CustomerContract customerContract = contractService.getContractByContractId(contractId, effective);
            return ReturnResult.success(customerContract);
        } catch (CustomerException e) {
            return ReturnResult.fail(e.getCode(), e.getMsg());
        }
    }

}
