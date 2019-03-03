package com.z.merchantsettle.modules.upm.service;


import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.modules.upm.domain.RoleSearchParam;
import com.z.merchantsettle.modules.upm.domain.bo.Role;
import com.z.merchantsettle.modules.upm.domain.bo.UserRole;

import java.util.List;


public interface RoleService {

    PageData<Role> getRoleList(RoleSearchParam roleSearchParam, Integer pageNum, Integer pageSize);

    void deleteByRoleId(String roleId);

    Role getRoleByRoleId(String roleId);

    void saveOrUpdate(Role role);

    List<Role> getRolesByIdList(List<String> roleIdList);

    Role getByRoleName(String roleName);
}
