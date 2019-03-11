package com.z.merchantsettle.modules.upm.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;

import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.common.ValidationResult;
import com.z.merchantsettle.modules.upm.service.ResourceService;
import com.z.merchantsettle.modules.upm.domain.bo.Resource;
import com.z.merchantsettle.utils.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/resource")
public class ResourceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private ResourceService resourceService;

    @RequestMapping("/list")
    public Object list(@RequestParam(required = false, defaultValue = "") String resourceName) {
        List<Resource> resourceList = resourceService.getResourceList(resourceName);
        return ReturnResult.success(resourceList);
    }

    @PostMapping("/save")
    public Object saveOrUpdate(String resourceStr) {
        LOGGER.info("saveOrUpdate resourceStr = {}", resourceStr);
        Resource resource = JSON.parseObject(resourceStr, Resource.class);
        LOGGER.info("saveOrUpdate resourceStr = {}", JSON.toJSONString(resource));

        if (resource == null) {
            return ReturnResult.fail("保存参数错误");
        }
        if (StringUtils.isBlank(resource.getResourceId())) {
            String resourceId = IdUtil.objectId();
            resource.setResourceId(resourceId);
        }
        ValidationResult validateResult = ValidationUtil.validate(resource);
        if (validateResult.isHasError()) {
            return ReturnResult.fail(validateResult.getErrorMsgStr());
        }
        resourceService.saveOrUpdate(resource);
        return ReturnResult.success(resource);
    }

    @PostMapping("/delete")
    public Object delete(@RequestParam(name = "resourceId") String resourceId) {
        if (StringUtils.isBlank(resourceId)) {
            return ReturnResult.fail("参数错误");
        }
        resourceService.deleteByResourceId(resourceId);
        return ReturnResult.success();
    }
}
