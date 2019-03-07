package com.z.merchantsettle.modules.upm.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.z.merchantsettle.exception.UpmException;
import com.z.merchantsettle.modules.upm.service.SystemService;
import com.z.merchantsettle.modules.upm.service.UserService;
import com.z.merchantsettle.modules.upm.constants.SystemConstant;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemServiceImpl implements SystemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemServiceImpl.class);

    @Autowired
    private UserService userService;

    @Override
    public User login(String userId, String password) {
        LOGGER.info("SystemServiceImpl login userId = {}, password = {}", userId, password);
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(password)) {
            throw new UpmException(SystemConstant.ErrorCode.PARAM_INPUT_ERROR, "参数错误");
        }

        User user = userService.getUserByUserId(userId);
        if (user == null) {
            throw new UpmException(SystemConstant.ErrorCode.DATA_NOT_EXIST_ERROR, "用户不存在");
        }
        if (!DigestUtil.md5Hex(password).equals(user.getUserPassword())) {
            throw new UpmException(SystemConstant.ErrorCode.USER_PASSWORD_ERROR, "密码错误");
        }
        return user;
    }

    public static void main(String[] args) {
        System.out.println(DigestUtil.md5Hex("admin"));
    }
}
