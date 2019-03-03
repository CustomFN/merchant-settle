package com.z.merchantsettle.modules.customer.controller;


import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.common.ValidationResult;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.exception.UpmException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.domain.CustomerSearchParam;
import com.z.merchantsettle.modules.customer.domain.bo.Customer;
import com.z.merchantsettle.modules.customer.service.CustomerAuditedService;
import com.z.merchantsettle.modules.customer.service.CustomerService;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.modules.upm.service.UserService;
import com.z.merchantsettle.utils.ValidationUtil;
import com.z.merchantsettle.utils.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerAuditedService customerAuditedService;

    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    public Object list(CustomerSearchParam customerSearchParam,
                       @RequestParam(defaultValue = "1", name = "pageNum") Integer pageNum,
                       @RequestParam(defaultValue = "30", name = "pageSize") Integer pageSize) {
        if (pageNum == null || pageNum < 1 || pageSize == null || pageSize < 1) {
            return ReturnResult.fail("参数错误");
        }
        return ReturnResult.success(customerAuditedService.getCustomerList(customerSearchParam, pageNum, pageSize));
    }

    @RequestMapping("/delete/{customerId}")
    public Object deleteByCustomerId(@PathVariable(name = "customerId") Integer customerId) {
        if (customerId == null || customerId <= 0) {
            return ReturnResult.fail(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        try {
            User user = ShiroUtils.getSysUser();
            boolean hasRole = userService.hasRole(user.getUserId(), "客户删除管理员");
            if (!hasRole) {
                return ReturnResult.fail(CustomerConstant.NO_PERMISSION, "无权限操作");
            }
            customerAuditedService.deleteByCustomerId(customerId, user.getUserId());
        } catch (UpmException | CustomerException e) {
            return ReturnResult.fail(CustomerConstant.CUSTOMER_OP_ERROR, "客户删除失败");
        }
        return ReturnResult.success();
    }

    @RequestMapping("/show/{customerId}")
    public Object getByCustomerId(@PathVariable(name = "customerId") Integer customerId,
                                  @RequestParam(name = "effective") Integer effective) {
        if (customerId == null || customerId <= 0 || effective < 0) {
            return ReturnResult.fail(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        try {
            Customer customer = customerService.getByCustomerId(customerId, effective);
            return ReturnResult.success(customer);
        } catch (CustomerException e) {
            LOGGER.error("查询客户信息异常", e);
            return ReturnResult.fail(CustomerConstant.CUSTOMER_OP_ERROR, "查询客户信息异常");
        }
    }

    @RequestMapping("/distributePrincipal")
    public Object distributePrincipal(Integer customerId, String userId) {
        if (customerId == null || customerId <= 0 || StringUtils.isBlank(userId)) {
            return ReturnResult.fail(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        try {
            User user = ShiroUtils.getSysUser();
            customerService.distributePrincipal(customerId, userId, user.getUserId());
            return ReturnResult.success();
        } catch (UpmException | CustomerException e) {
            return ReturnResult.fail(CustomerConstant.CUSTOMER_OP_ERROR, "分配责任人失败");
        }
    }

    @RequestMapping("/save")
    public Object saveOrUpdate(@RequestBody Customer customer) {
        LOGGER.info("saveOrUpdate customer = {}", JSON.toJSONString(customer));
        if (customer == null) {
            return ReturnResult.fail(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        ValidationResult validateResult = ValidationUtil.validate(customer);
        if (validateResult.isHasError()) {
            return ReturnResult.fail(validateResult.getErrorMsg());
        }

        try {
            User user = ShiroUtils.getSysUser();
            customerService.saveOrUpdate(customer, user.getUserId());
            return ReturnResult.success();
        } catch (UpmException | CustomerException e) {
            LOGGER.error("保存客户失败", e);
            return ReturnResult.fail(CustomerConstant.CUSTOMER_OP_ERROR ,"保存失败");
        }
    }
}
