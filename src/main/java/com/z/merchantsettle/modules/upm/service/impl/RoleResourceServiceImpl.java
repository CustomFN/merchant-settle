package com.z.merchantsettle.modules.upm.service.impl;


import com.google.common.collect.Lists;

import com.z.merchantsettle.modules.upm.dao.RoleResourceMapper;
import com.z.merchantsettle.modules.upm.domain.db.RoleResourceDB;
import com.z.merchantsettle.modules.upm.service.RoleResourceService;
import com.z.merchantsettle.modules.upm.domain.bo.RoleResource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class RoleResourceServiceImpl implements RoleResourceService {

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @Override
    @Transactional
    public void assignRoleResources(RoleResource roleResource) {
        List<String> resourceIdList = roleResource.getResourceIdList();
        List<RoleResourceDB> roleResourceDBList = Lists.newArrayList();
        for (String resourceId : resourceIdList) {
            RoleResourceDB roleResourceDB = new RoleResourceDB();
            roleResourceDB.setRoleId(roleResource.getRoleId());
            roleResourceDB.setResourceId(resourceId);
            roleResourceDBList.add(roleResourceDB);
        }
        roleResourceMapper.unbindRoleResource(roleResource.getRoleId());
        roleResourceMapper.assignRoleResource(roleResourceDBList);
    }

    @Override
    public List<String> getResourceIdByRoleIdList(List<String> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }

        return roleResourceMapper.getResourceIdByRoleIdList(roleIdList);
    }
}
