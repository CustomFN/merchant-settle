package com.z.merchantsettle.utils.transfer.upm;

import com.google.common.collect.Lists;

import com.z.merchantsettle.modules.upm.domain.bo.Resource;
import com.z.merchantsettle.modules.upm.domain.db.ResourceDB;
import com.z.merchantsettle.utils.TransferUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class ResourceTransferUtil {

    public static Resource transResourceDB2Bo(ResourceDB resourceDB) {
        if (resourceDB == null) {
            return null;
        }

        Resource resource = new Resource();
        TransferUtil.transferAll(resourceDB, resource);
        return resource;

    }

    public static List<Resource> transResourceDBList2BoList(List<ResourceDB> resourceDBList) {
        if (CollectionUtils.isEmpty(resourceDBList)) {
            return Lists.newArrayList();
        }

        List<Resource> resourceList = Lists.newArrayList();
        for (ResourceDB resourceDB : resourceDBList) {
            resourceList.add(transResourceDB2Bo(resourceDB));
        }
        return resourceList;
    }

    public static ResourceDB transResourceBo2DB(Resource resource) {
        if (resource == null) {
            return null;
        }

        ResourceDB resourceDB = new ResourceDB();
        TransferUtil.transferAll(resource, resourceDB);
        return resourceDB;
    }

    public static List<ResourceDB> transResourceBoList2DBList(List<Resource> resourceList) {
        if (CollectionUtils.isEmpty(resourceList)) {
            return Lists.newArrayList();
        }

        List<ResourceDB> resourceDBList = Lists.newArrayList();
        for (Resource resource : resourceList) {
            resourceDBList.add(transResourceBo2DB(resource));
        }
        return resourceDBList;
    }
}
