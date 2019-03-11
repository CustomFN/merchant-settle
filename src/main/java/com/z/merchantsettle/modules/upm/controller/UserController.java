package com.z.merchantsettle.modules.upm.controller;

import com.alibaba.fastjson.JSON;

import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.common.ValidationResult;
import com.z.merchantsettle.modules.upm.service.UserRoleService;
import com.z.merchantsettle.modules.upm.service.UserService;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.modules.upm.domain.bo.UserRole;
import com.z.merchantsettle.modules.upm.domain.UserSearchParam;
import com.z.merchantsettle.utils.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/save")
    public Object saveOrUpdate(User user) {
        LOGGER.info("saveOrUpdate user = {}", JSON.toJSONString(user));

        if (user == null) {
            return ReturnResult.fail("保存参数错误");
        }
        ValidationResult validateResult = ValidationUtil.validate(user);
        if (validateResult.isHasError()) {
            return ReturnResult.fail(validateResult.getErrorMsgStr());
        }

        try {
            userService.saveOrUpdate(user);
            return ReturnResult.success(user);
        } catch (Exception e) {
            LOGGER.error("保存更新用户失败", e);
            return ReturnResult.fail(e.getMessage());
        }
    }

    @PostMapping("/list")
    public Object list(UserSearchParam searchParam,
                       @RequestParam(name = "pageNum", defaultValue = "1")Integer pageNum,
                       @RequestParam(name = "pageSize", defaultValue = "30")Integer pageSize) {
        LOGGER.info("list searchParam = {}, pageNum = {}, pageSize = {}", JSON.toJSONString(searchParam), pageNum, pageSize);

        PageData<User> pageData = userService.getUserList(searchParam, pageNum, pageSize);
        return ReturnResult.success(pageData);
    }

    @PostMapping("/delete")
    public Object delete(@RequestParam(name = "userId") String userId) {
        if (StringUtils.isBlank(userId)) {
            return ReturnResult.fail("参数错误");
        }
        userService.deleteByUserId(userId);
        return ReturnResult.success();
    }

    @PostMapping("/show")
    public Object show(@RequestParam(name = "userId") String userId) {
        if (StringUtils.isBlank(userId)) {
            return ReturnResult.fail("参数错误");
        }

        User user = userService.getUserByUserId(userId);
        return ReturnResult.success(user);
    }

    @PostMapping("/assignRoles")
    public Object assignRoles(UserRole userRole) {
        if (userRole ==null || StringUtils.isBlank(userRole.getUserId())) {
            return ReturnResult.fail("参数错误");
        }

        userRoleService.assignUserRoles(userRole);
        return ReturnResult.success();
    }
}
