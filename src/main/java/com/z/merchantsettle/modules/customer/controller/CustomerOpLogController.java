package com.z.merchantsettle.modules.customer.controller;

import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.domain.CustomerOpLogSearchParam;
import com.z.merchantsettle.modules.customer.service.CustomerOpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer/log")
public class CustomerOpLogController {

    @Autowired
    private CustomerOpLogService customerOpLogService;

    @RequestMapping("/list")
    public Object getLogList(CustomerOpLogSearchParam opLogSearchParam,
                             @RequestParam(defaultValue = "1", name = "pageNum") Integer pageNum,
                             @RequestParam(defaultValue = "30", name = "pageSize") Integer pageSize) {
        if (pageNum == null || pageNum < 1 || pageSize == null || pageSize < 1) {
            return ReturnResult.fail("参数错误");
        }
        try {
            return ReturnResult.success(customerOpLogService.getLogByCustomerId(opLogSearchParam, pageNum, pageSize));
        } catch (CustomerException e) {
            return ReturnResult.fail(CustomerConstant.CUSTOMER_OP_ERROR, "查询操作日志错误");
        }
    }

}
