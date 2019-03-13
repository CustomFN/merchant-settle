package com.z.merchantsettle.modules.upm.dao;


import com.z.merchantsettle.modules.upm.domain.db.RoleResourceDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleResourceMapper{

    void unbindRoleResource(String roleId);

    void assignRoleResource(@Param("roleId") String roleId, @Param("resourceIdList") List<String> resourceIdList);

    List<String> getResourceIdByRoleIdList(@Param("idList") List<String> roleIdList);

    List<RoleResourceDB> getByRoleIdList(@Param("idList") List<String> roleIdList);
}
