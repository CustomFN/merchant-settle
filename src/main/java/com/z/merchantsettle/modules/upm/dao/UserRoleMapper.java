package com.z.merchantsettle.modules.upm.dao;

import com.z.merchantsettle.modules.upm.domain.db.UserRoleDB;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRoleMapper {

    void unbindUserRole(String userId);

    void assginUserRole(List<UserRoleDB> userRoleDBList);

    List<String> getRoleIdByUserId(String userId);

    UserRoleDB getByUserIdAndRoleId(String userId, String roleId);
}
