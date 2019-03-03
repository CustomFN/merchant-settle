package com.z.merchantsettle.modules.upm.dao;


import com.z.merchantsettle.modules.upm.domain.RoleSearchParam;
import com.z.merchantsettle.modules.upm.domain.db.RoleDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleMapper {

    void deleteByRoleId(String roleId);

    void insertSelective(RoleDB roleDB);

    void updateByRoleIdSelective(RoleDB roleDB);

    List<RoleDB> selectList(@Param("roleSearchParam") RoleSearchParam roleSearchParam);

    RoleDB selectByRoleId(String roleId);

    List<RoleDB> getRolesByIdList(@Param("idList") List<String> roleIdList);

    RoleDB getByRoleName(String roleName);
}
