package com.z.merchantsettle.modules.upm.controller;


import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.modules.upm.service.ResourceService;
import com.z.merchantsettle.modules.upm.domain.bo.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system")
public class SystemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemController.class);

    @Autowired
    private ResourceService resourceService;

    @RequestMapping("/getMenuList")
    public Object getMenuList() {
        List<Resource> resourceList = resourceService.getResourceList("");
        return ReturnResult.success(resourceList);
    }
}
