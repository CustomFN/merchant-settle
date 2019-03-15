package com.z.merchantsettle.modules.customer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.dao.CustomerOpLogMapper;
import com.z.merchantsettle.modules.customer.domain.CustomerOpLog;
import com.z.merchantsettle.modules.customer.domain.CustomerOpLogSearchParam;
import com.z.merchantsettle.modules.customer.service.CustomerOpLogService;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.utils.shiro.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerOpLogServiceImpl implements CustomerOpLogService {

    @Autowired
    private CustomerOpLogMapper customerOpLogMapper;

    @Autowired
    private UserUtil userUtil;


    @Override
    public void addLog(Integer customerId, String module, String content, String opUser) {
        CustomerOpLog customerOpLog = new CustomerOpLog();
        customerOpLog.setCustomerId(customerId);
        customerOpLog.setModule(module);
        customerOpLog.setContent(content);
        customerOpLog.setOpUser(opUser);
        customerOpLogMapper.insert(customerOpLog);
    }

    @Override
    public PageData<CustomerOpLog> getLogByCustomerId(CustomerOpLogSearchParam opLogSearchParam,
                                                      Integer pageNum,
                                                      Integer pageSize) throws CustomerException {
        if (opLogSearchParam.getCustomerId() == null || opLogSearchParam.getCustomerId() <= 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<CustomerOpLog> logList = customerOpLogMapper.getLogByCustomerId(opLogSearchParam);
        PageInfo<CustomerOpLog> pageInfo = new PageInfo<>(logList);

        for (CustomerOpLog opLog : logList) {
            User user = userUtil.getUser(opLog.getOpUserId());
            if (user != null) {
                opLog.setOpUser(user.getUserName());
            }
        }

        return new PageData.Builder<CustomerOpLog>()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalSize((int) pageInfo.getTotal())
                .totalPage(pageInfo.getPageSize())
                .data(logList)
                .build();
    }
}
