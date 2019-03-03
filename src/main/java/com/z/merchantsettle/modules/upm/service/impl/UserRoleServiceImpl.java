package com.z.merchantsettle.modules.upm.service.impl;


import com.google.common.collect.Lists;

import com.z.merchantsettle.modules.upm.domain.db.UserRoleDB;
import com.z.merchantsettle.modules.upm.service.UserRoleService;
import com.z.merchantsettle.modules.upm.dao.UserRoleMapper;
import com.z.merchantsettle.modules.upm.domain.bo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    @Transactional
    public void assignUserRoles(UserRole userRole) {
        List<String> roleIdList = userRole.getRoleIdList();
        List<UserRoleDB> userRoleDBList = Lists.newArrayList();
        for (String roleId : roleIdList) {
            UserRoleDB userRoleDB = new UserRoleDB();
            userRoleDB.setUserId(userRole.getUserId());
            userRoleDB.setRoleId(roleId);
            userRoleDBList.add(userRoleDB);
        }
        userRoleMapper.unbindUserRole(userRole.getUserId());
        userRoleMapper.assginUserRole(userRoleDBList);
    }

    @Override
    public List<String> getRoleIdByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return Lists.newArrayList();
        }
        return userRoleMapper.getRoleIdByUserId(userId);
    }

    @Override
    public UserRole getByUserIdAndRoleId(String userId, String roleId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(roleId)) {
            return null;
        }

        UserRoleDB userRoleDB = userRoleMapper.getByUserIdAndRoleId(userId, roleId);
        if (userRoleDB == null) {
            return null;
        }

        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleIdList(Lists.newArrayList(userRoleDB.getRoleId()));
        return userRole;
    }

}
