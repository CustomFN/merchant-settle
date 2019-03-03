package com.z.merchantsettle.modules.upm.service;



import com.z.merchantsettle.modules.upm.domain.bo.Resource;

import java.util.List;


public interface ResourceService {

    List<Resource> getByLevelSort(Integer level);

    List<Resource> getByParentResourceIdSort(String parentId);

    List<Resource> getResourceList(String resourceName);

    void saveOrUpdate(Resource resource);

    void deleteByResourceId(String resourceId);
}
