package com.z.merchantsettle.modules.upm.service.impl;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.modules.upm.dao.RoleMapper;
import com.z.merchantsettle.modules.upm.domain.RoleSearchParam;
import com.z.merchantsettle.modules.upm.domain.bo.Role;
import com.z.merchantsettle.modules.upm.domain.db.RoleDB;
import com.z.merchantsettle.modules.upm.service.RoleService;
import com.z.merchantsettle.utils.transfer.upm.RoleTransferUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageData<Role> getRoleList(RoleSearchParam roleSearchParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<RoleDB> roleDBList = roleMapper.selectList(roleSearchParam);

        List<Role> roleList = RoleTransferUtil.transRoleDBList2BoList(roleDBList);
        PageInfo<Role> pageInfo = new PageInfo<>(roleList);
        return new PageData.Builder<Role>()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalSize((int) pageInfo.getTotal())
                .totalPage(pageInfo.getPageSize())
                .data(roleList)
                .build();
    }

    @Override
    public void deleteByRoleId(String roleId) {
        roleMapper.deleteByRoleId(roleId);
    }

    @Override
    public Role getRoleByRoleId(String roleId) {
        if (StringUtils.isBlank(roleId)) {
            return null;
        }

        RoleDB roleDB = roleMapper.selectByRoleId(roleId);
        return RoleTransferUtil.transRoleDB2Bo(roleDB);
    }

    @Override
    public void saveOrUpdate(Role role) {
        RoleDB roleDB = RoleTransferUtil.transRoleBo2DB(role);

        RoleDB roleDBInDB = roleMapper.selectByRoleId(role.getRoleId());
        if (roleDBInDB == null) {
            roleMapper.insertSelective(roleDB);
        } else {
            roleMapper.updateByRoleIdSelective(roleDB);
        }
    }

    @Override
    public List<Role> getRolesByIdList(List<String> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }

        return RoleTransferUtil.transRoleDBList2BoList(roleMapper.getRolesByIdList(roleIdList));
    }

    @Override
    public Role getByRoleName(String roleName) {
        if (StringUtils.isBlank(roleName)) {
            return null;
        }

        RoleDB roleDB = roleMapper.getByRoleName(roleName);
        return RoleTransferUtil.transRoleDB2Bo(roleDB);
    }
}
