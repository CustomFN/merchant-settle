package com.z.merchantsettle.modules.upm.service.impl;


import com.z.merchantsettle.modules.upm.dao.ResourceMapper;
import com.z.merchantsettle.modules.upm.domain.bo.Resource;
import com.z.merchantsettle.modules.upm.domain.db.ResourceDB;
import com.z.merchantsettle.modules.upm.service.ResourceService;
import com.z.merchantsettle.modules.upm.constants.SystemConstant;
import com.z.merchantsettle.utils.transfer.upm.ResourceTransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;


    @Override
    public List<Resource> getByLevelSort(Integer level) {
        List<ResourceDB> resourceDBList = resourceMapper.getByLevelSort(level);
        return ResourceTransferUtil.transResourceDBList2BoList(resourceDBList);
    }

    @Override
    public List<Resource> getByParentResourceIdSort(String parentId) {
        List<ResourceDB> resourceDBList = resourceMapper.getByParentResourceIdSort(parentId);
        return ResourceTransferUtil.transResourceDBList2BoList(resourceDBList);
    }

    @Override
    public List<Resource> getResourceList(String resourceName) {
        List<Resource> topResourceList = getByLevelSort(SystemConstant.TOP_LEVEL);
        for (Resource topResource : topResourceList) {
            List<Resource> secondResourceList = getByParentResourceIdSort(topResource.getResourceId());
            topResource.setChildResource(secondResourceList);
            for (Resource secondResource : secondResourceList) {
                List<Resource> thirdResourceList = getByParentResourceIdSort(secondResource.getResourceId());
                secondResource.setChildResource(thirdResourceList);
            }
        }
        return topResourceList;
    }

    @Override
    public void saveOrUpdate(Resource resource) {
        ResourceDB resourceDB = ResourceTransferUtil.transResourceBo2DB(resource);
        ResourceDB resourceDBInDB = resourceMapper.selectByResourceId(resource.getResourceId());
        if (resourceDBInDB == null) {
            resourceMapper.insertSelective(resourceDB);
        } else {
            resourceMapper.updateByResourceIdSelective(resourceDB);
        }
    }

    @Override
    public void deleteByResourceId(String resourceId) {
        resourceMapper.deleteByResourceId(resourceId);
    }
}
