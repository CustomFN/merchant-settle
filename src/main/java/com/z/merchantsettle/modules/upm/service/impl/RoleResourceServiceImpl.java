package com.z.merchantsettle.modules.upm.service.impl;


import com.google.common.collect.Lists;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.z.merchantsettle.modules.upm.dao.RoleResourceMapper;
import com.z.merchantsettle.modules.upm.domain.db.RoleResourceDB;
import com.z.merchantsettle.modules.upm.service.RoleResourceService;
import com.z.merchantsettle.modules.upm.domain.bo.RoleResource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class RoleResourceServiceImpl implements RoleResourceService {

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @Override
    @Transactional
    public void assignRoleResources(RoleResource roleResource) {
        String roleId = roleResource.getRoleId();
        Set<String> resourceIdSet = Sets.newHashSet(roleResource.getResourceIdList());
        Set<String> resourceIdSetInDB = Sets.newHashSet(
                roleResourceMapper.getResourceIdByRoleIdList(Lists.newArrayList(roleId)));

        Set<String> deleteIdSet = Sets.difference(resourceIdSetInDB, resourceIdSet);
        Set<String> addIdSet = Sets.difference(resourceIdSet, resourceIdSetInDB);

        if (CollectionUtils.isNotEmpty(deleteIdSet)) {
            roleResourceMapper.unbindRoleResourceSet(roleId, deleteIdSet);
        }
        if (CollectionUtils.isNotEmpty(addIdSet)) {
            roleResourceMapper.assignRoleResource(roleId, Lists.newArrayList(addIdSet));
        }
    }

    @Override
    public List<String> getResourceIdByRoleIdList(List<String> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }

        return roleResourceMapper.getResourceIdByRoleIdList(roleIdList);
    }

    @Override
    public Map<String, List<String>> getByRoleIdList(List<String> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Maps.newHashMap();
        }

        List<RoleResourceDB> roleResourceDBList = roleResourceMapper.getByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(roleResourceDBList)) {
            return Maps.newHashMap();
        }

        Map<String, List<String>> map = Maps.newHashMap();
        for (RoleResourceDB roleResourceDB : roleResourceDBList) {
            List<String> list = map.get(roleResourceDB.getRoleId());
            if (CollectionUtils.isEmpty(list)) {
                list = Lists.newArrayList(roleResourceDB.getResourceId());
                map.put(roleResourceDB.getRoleId(), list);
            } else {
                list.add(roleResourceDB.getResourceId());
            }
        }
        return map;
    }
}
