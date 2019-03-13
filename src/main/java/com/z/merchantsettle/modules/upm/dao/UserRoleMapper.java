package com.z.merchantsettle.modules.upm.dao;

import com.z.merchantsettle.modules.upm.domain.db.UserRoleDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRoleMapper {

    void unbindUserRole(String userId);

    void assginUserRole(@Param("userId") String userId, @Param("roleIdList") List<String> roleIdList);

    List<String> getRoleIdByUserId(String userId);

    UserRoleDB getByUserIdAndRoleId(String userId, String roleId);

    List<UserRoleDB> getByUserIdList(@Param("userIdList") List<String> userIdList);
}
