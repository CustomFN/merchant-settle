package com.z.merchantsettle.modules.upm.dao;

import com.z.merchantsettle.modules.upm.domain.db.UserRoleDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface UserRoleMapper {

    void unbindUserRole(@Param("userId") String userId);

    void unbindUserRoleSet(@Param("userId") String userId, @Param("roleIdSet") Set<String> roleIdSet);

    void assginUserRole(@Param("userId") String userId, @Param("roleIdList") List<String> roleIdList);

    List<String> getRoleIdByUserId(@Param("userId") String userId);

    UserRoleDB getByUserIdAndRoleId(@Param("userId") String userId, @Param("roleId") String roleId);

    List<UserRoleDB> getByUserIdList(@Param("userIdList") List<String> userIdList);
}
