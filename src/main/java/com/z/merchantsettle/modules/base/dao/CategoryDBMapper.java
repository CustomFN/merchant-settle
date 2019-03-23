package com.z.merchantsettle.modules.base.dao;

import com.z.merchantsettle.modules.base.domain.bo.CategoryInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDBMapper {

    List<CategoryInfo> getCategories();

}
