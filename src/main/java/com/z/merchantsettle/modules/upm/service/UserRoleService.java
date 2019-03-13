package com.z.merchantsettle.modules.upm.service;


import com.z.merchantsettle.modules.upm.domain.bo.UserRole;
import com.z.merchantsettle.modules.upm.domain.db.UserDB;

import java.util.List;
import java.util.Map;


public interface UserRoleService {

    void assignUserRoles(UserRole userRole);

    List<String> getRoleIdByUserId( String userId);

    UserRole getByUserIdAndRoleId(String userId, String roleId);

    Map<String, List<String>> getByUserIdList(List<String> userIdList);
}
