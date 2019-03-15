package com.z.merchantsettle.modules.upm.dao;


import com.z.merchantsettle.modules.upm.domain.db.RoleResourceDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface RoleResourceMapper{

    void unbindRoleResource(@Param("roleId") String roleId);

    void unbindRoleResourceSet(@Param("roleId") String roleId, @Param("resourceIdSet") Set<String> resourceIdSet);

    void assignRoleResource(@Param("roleId") String roleId, @Param("resourceIdList") List<String> resourceIdList);

    List<String> getResourceIdByRoleIdList(@Param("idList") List<String> roleIdList);

    List<RoleResourceDB> getByRoleIdList(@Param("idList") List<String> roleIdList);
}
