package com.z.merchantsettle.modules.base.service.impl;


import com.z.merchantsettle.modules.base.dao.CategoryDBMapper;
import com.z.merchantsettle.modules.base.domain.bo.CategoryInfo;
import com.z.merchantsettle.modules.base.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDBMapper categoryDBMapper;

    @Override
    public List<CategoryInfo> getCategories() {
        return categoryDBMapper.getCategories();
    }

    @Override
    public CategoryInfo getById(Integer categoryId) {
        return categoryDBMapper.getById(categoryId);
    }
}
