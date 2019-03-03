package com.z.merchantsettle.modules.customer.controller;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.common.ValidationResult;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.exception.UpmException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettle;
import com.z.merchantsettle.modules.customer.service.CustomerSettleService;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.utils.ValidationUtil;
import com.z.merchantsettle.utils.shiro.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/settle")
public class CustomerSettleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerSettleController.class);

    @Autowired
    private CustomerSettleService customerSettleService;

    @RequestMapping("/save")
    public Object saveOrUpdate(@RequestBody CustomerSettle customerSettle) {
        LOGGER.info("CustomerSettleController saveOrUpdate customerSettle = {}", JSON.toJSONString(customerSettle));
        if (customerSettle == null) {
            return ReturnResult.fail(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        ValidationResult validateResult = ValidationUtil.validate(customerSettle);
        if (validateResult.isHasError()) {
            return ReturnResult.fail(validateResult.getErrorMsg());
        }

        try {
            User user = ShiroUtils.getSysUser();
            customerSettleService.saveOrUpdate(customerSettle, user.getUserId());
            return ReturnResult.success();
        } catch (UpmException | CustomerException e) {
            LOGGER.error("保存客户结算异常", e);
            return ReturnResult.fail(CustomerConstant.CUSTOMER_OP_ERROR, "保存客户结算异常");
        }
    }

    @RequestMapping("/show/{settleId}")
    public Object getCustomerSettleBySettleId(@PathVariable(name = "settleId") Integer settleId,
                                              @RequestParam(name = "effective") Integer effective) {

        try {
            customerSettleService.getCustomerSettleBySettleId(settleId, effective);
            return ReturnResult.success();
        } catch (Exception e) {
            LOGGER.error("查询客户结算信息异常", e);
            return ReturnResult.fail(CustomerConstant.CUSTOMER_OP_ERROR, "查询客户结算信息异常");
        }
    }

    @RequestMapping("/list")
    public Object getCustomerSettleList(String settleOrPoiId,
                                        @RequestParam(name = "effective") Integer effective,
                                        @RequestParam(defaultValue = "1", name = "pageNum") Integer pageNum,
                                        @RequestParam(defaultValue = "10", name = "pageSize") Integer pageSize) {
        if (pageNum == null || pageNum < 1 || pageSize == null || pageSize < 1) {
            return ReturnResult.fail("参数错误");
        }
        try {
            return ReturnResult.success(customerSettleService.getCustomerSettleList(settleOrPoiId, effective, pageNum, pageSize));
        } catch (Exception e) {
            LOGGER.error("结算信息列表获取异常", e);
            return ReturnResult.fail(CustomerConstant.CUSTOMER_OP_ERROR,"结算信息列表获取异常");
        }
    }

    @RequestMapping("/delete/{settleId}")
    public Object deleteBySettleId(@PathVariable(name = "settleId") Integer settleId) {
        if (settleId == null || settleId <= 0) {
            return ReturnResult.fail( "参数错误");
        }

        customerSettleService.deleteBySettleId(settleId);
        return ReturnResult.success();
    }
}
