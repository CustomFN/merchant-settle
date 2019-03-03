package com.z.merchantsettle.modules.upm.service;


import com.z.merchantsettle.modules.upm.domain.bo.UserRole;

import java.util.List;


public interface UserRoleService {

    void assignUserRoles(UserRole userRole);

    List<String> getRoleIdByUserId( String userId);

    UserRole getByUserIdAndRoleId(String userId, String roleId);
}
