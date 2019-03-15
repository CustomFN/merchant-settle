package com.z.merchantsettle.modules.upm.controller;

import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.modules.upm.constants.SystemConstant;
import com.z.merchantsettle.utils.shiro.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    public Object login(@RequestParam(name = "userId") String userId,
                        @RequestParam(name = "password") String password,
                        @RequestParam(name = "rememberMe") Boolean rememberMe) {
        LOGGER.info("用户登录 userId = {}, password = {}, rememberMe = {}", userId, password, rememberMe);
        UsernamePasswordToken token = new UsernamePasswordToken(userId, password, rememberMe);
        Subject subjetct = SecurityUtils.getSubject();
        try {
            subjetct.login(token);
        } catch (Exception e) {
            LOGGER.warn("登录失败", e);
            return ReturnResult.fail("账号或密码错误");
        }
        return ReturnResult.success(ShiroUtils.getSysUser());
    }


    @GetMapping("/unauth")
    public Object unauth() {
        return ReturnResult.fail(SystemConstant.UNAUTH_CODE);
    }

    @PostMapping("/logout")
    public Object logout() {
        LOGGER.info("用户登出");
        Subject subjetct = SecurityUtils.getSubject();
        subjetct.logout();
        return ReturnResult.success();
    }

}
