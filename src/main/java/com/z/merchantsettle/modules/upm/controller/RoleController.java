package com.z.merchantsettle.modules.upm.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;

import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.common.ValidationResult;
import com.z.merchantsettle.modules.upm.service.RoleResourceService;
import com.z.merchantsettle.modules.upm.service.RoleService;
import com.z.merchantsettle.modules.upm.domain.bo.Role;
import com.z.merchantsettle.modules.upm.domain.bo.RoleResource;
import com.z.merchantsettle.modules.upm.domain.RoleSearchParam;
import com.z.merchantsettle.utils.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleResourceService roleResourceService;

    @RequestMapping("/list")
    public Object list(RoleSearchParam roleSearchParam,
                       @RequestParam(name = "pageNum", defaultValue = "1")Integer pageNum,
                       @RequestParam(name = "pageSize", defaultValue = "30")Integer pageSize) {

        PageData<Role> pageData = roleService.getRoleList(roleSearchParam, pageNum, pageSize);
        return ReturnResult.success(pageData);
    }

    @PostMapping("/delete")
    public Object delete(@RequestParam(name = "roleId") String roleId) {
        if (StringUtils.isBlank(roleId)) {
            return ReturnResult.fail("参数错误");
        }
        roleService.deleteByRoleId(roleId);
        return ReturnResult.success();
    }

    @PostMapping("/show")
    public Object show(@RequestParam(name = "roleId") String roleId) {
        if (StringUtils.isBlank(roleId)) {
            return ReturnResult.fail("参数错误");
        }

        Role role = roleService.getRoleByRoleId(roleId);
        return ReturnResult.success(role);
    }

    @PostMapping("/save")
    public Object saveOrUpdate(String roleStr) {
        LOGGER.info("saveOrUpdate roleStr = {}", roleStr);
        Role role = JSON.parseObject(roleStr, Role.class);
        LOGGER.info("saveOrUpdate role = {}", JSON.toJSONString(role));

        if (role == null) {
            return ReturnResult.fail("保存参数错误");
        }
        if (StringUtils.isBlank(role.getRoleId())) {
            String roleId = IdUtil.objectId();
            LOGGER.info("生成 roleId = {}", role);
            role.setRoleId(roleId);
        }
        ValidationResult validateResult = ValidationUtil.validate(role);
        if (validateResult.isHasError()) {
            return ReturnResult.fail(validateResult.getErrorMsgStr());
        }
        roleService.saveOrUpdate(role);
        return ReturnResult.success(role);
    }

    @PostMapping("/assignResources")
    public Object assignRoleResources(RoleResource roleResource) {
        if (roleResource ==null || StringUtils.isBlank(roleResource.getRoleId())) {
            return ReturnResult.fail("参数错误");
        }

        roleResourceService.assignRoleResources(roleResource);
        return ReturnResult.success();
    }
}
