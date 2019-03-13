package com.z.merchantsettle.modules.upm.service;


import com.z.merchantsettle.modules.upm.domain.bo.RoleResource;

import java.util.List;
import java.util.Map;


public interface RoleResourceService {

    void assignRoleResources(RoleResource roleResource);

    List<String> getResourceIdByRoleIdList(List<String> roleIdList);

    Map<String, List<String>> getByRoleIdList(List<String> roleIdList);
}
