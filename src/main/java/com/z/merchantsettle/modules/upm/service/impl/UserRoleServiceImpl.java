package com.z.merchantsettle.modules.upm.service.impl;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.z.merchantsettle.modules.upm.domain.db.UserRoleDB;
import com.z.merchantsettle.modules.upm.service.UserRoleService;
import com.z.merchantsettle.modules.upm.dao.UserRoleMapper;
import com.z.merchantsettle.modules.upm.domain.bo.UserRole;
import com.z.merchantsettle.utils.TransferUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class UserRoleServiceImpl implements UserRoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRoleServiceImpl.class);

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    @Transactional
    public void assignUserRoles(UserRole userRole) {
        String userId = userRole.getUserId();
        Set<String> roleIdSet = Sets.newHashSet(userRole.getRoleIdList());
        Set<String> roleIdSetInDB = Sets.newHashSet(userRoleMapper.getRoleIdByUserId(userId));

        Set<String> addIdList = Sets.difference(roleIdSet, roleIdSetInDB);
        Set<String> deleteIdList = Sets.difference(roleIdSetInDB, roleIdSet);

        if (CollectionUtils.isNotEmpty(deleteIdList)) {
            userRoleMapper.unbindUserRoleSet(userId, deleteIdList);
        }
        if (CollectionUtils.isNotEmpty(addIdList)) {
            userRoleMapper.assginUserRole(userId, Lists.newArrayList(addIdList));
        }
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

    @Override
    public Map<String, List<String>> getByUserIdList(List<String> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return Maps.newHashMap();
        }
        List<UserRoleDB> userRoleDBList = userRoleMapper.getByUserIdList(userIdList);

        Map<String, List<String>> map = Maps.newHashMap();
        for (UserRoleDB userRoleDB : userRoleDBList) {
            List<String> list = map.get(userRoleDB.getUserId());
            if (CollectionUtils.isEmpty(list)) {
                list = Lists.newArrayList(userRoleDB.getRoleId());
                map.put(userRoleDB.getUserId(), list);
            } else {
                list.add(userRoleDB.getRoleId());
            }
        }
        return map;
    }
}
