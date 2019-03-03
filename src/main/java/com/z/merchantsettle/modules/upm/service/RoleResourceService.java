package com.z.merchantsettle.modules.upm.service;


import com.z.merchantsettle.modules.upm.domain.bo.RoleResource;

import java.util.List;


public interface RoleResourceService {

    void assignRoleResources(RoleResource roleResource);

    List<String> getResourceIdByRoleIdList(List<String> roleIdList);
}
