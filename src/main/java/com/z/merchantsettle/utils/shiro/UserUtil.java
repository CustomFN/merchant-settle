package com.z.merchantsettle.utils.shiro;

import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.modules.upm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserUtil {

    @Autowired
    private UserService userService;

    public User getUser(String userId) {
        return userService.getUserByUserId(userId);
    }
}
