package com.z.merchantsettle.modules.customer.controller;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.common.ValidationResult;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.exception.UpmException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerKp;
import com.z.merchantsettle.modules.customer.service.CustomerKpService;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.utils.ValidationUtil;
import com.z.merchantsettle.utils.shiro.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/kp")
public class CustomerKpController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerKpController.class);

    @Autowired
    private CustomerKpService customerKpService;

    @RequestMapping("/save")
    public Object saveOrUpdate(@RequestBody CustomerKp customerKp) {
        LOGGER.info("saveOrUpdate customerKp = {}" , JSON.toJSONString(customerKp));
        if (customerKp == null) {
            return ReturnResult.fail(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        ValidationResult validateResult = ValidationUtil.validate(customerKp);
        if (validateResult.isHasError()) {
            return ReturnResult.fail(validateResult.getErrorMsg());
        }

        try {
            User user = ShiroUtils.getSysUser();
            customerKpService.saveOrUpdate(customerKp, user.getUserId());
            return ReturnResult.success();
        } catch (UpmException | CustomerException e) {
            LOGGER.error("保存客户KP失败", e);
            return ReturnResult.fail(CustomerConstant.CUSTOMER_OP_ERROR, "保存失败");
        }
    }

    @RequestMapping("/show/{customerId}")
    public Object getCustomerKpByCustomerId(@PathVariable(name = "customerId") Integer customerId,
                                            @RequestParam(name = "effective") Integer effective) {
        if (customerId == null || customerId <= 0 || effective < 0) {
            return ReturnResult.fail(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        try {
            CustomerKp customerKp = customerKpService.getCustomerKpByCustomerId(customerId, effective);
            return ReturnResult.success(customerKp);
        } catch (CustomerException e) {
            LOGGER.error("查询客户KP异常 customerId = {}", customerId, e);
            return ReturnResult.fail(CustomerConstant.CUSTOMER_OP_ERROR, "查询异常");
        }
    }
}
