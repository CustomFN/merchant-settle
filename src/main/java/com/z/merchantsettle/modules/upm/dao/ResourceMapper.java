package com.z.merchantsettle.modules.upm.dao;

import com.z.merchantsettle.modules.upm.domain.db.ResourceDB;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ResourceMapper {


    List<ResourceDB> getByLevelSort(Integer level);

    List<ResourceDB> getByParentResourceIdSort(String parentId);

    void insertSelective(ResourceDB resourceDB);

    void updateByResourceIdSelective(ResourceDB resourceDB);

    void deleteByResourceId(String resourceId);

    ResourceDB selectByResourceId(String resourceId);
}
