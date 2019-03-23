package com.z.merchantsettle.modules.base.controller;

import cn.hutool.core.util.IdUtil;
import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.modules.base.service.BankService;
import com.z.merchantsettle.modules.base.service.CategoryService;
import com.z.merchantsettle.modules.base.service.GeoService;
import com.z.merchantsettle.utils.qiniu.QiniuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/ui")
public class UiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UiController.class);

    @Autowired
    private BankService bankService;
    @Autowired
    private GeoService geoService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/getCategories")
    public Object getCategories() {
        return ReturnResult.success(categoryService.getCategories());
    }

    @RequestMapping("/getProvinces")
    public Object getProvinces() {
        return ReturnResult.success(geoService.getProvinces());
    }

    @RequestMapping("/getCities")
    public Object getCities(@RequestParam("provinceId") Integer provinceId) {
        return ReturnResult.success(geoService.getCities(provinceId));
    }

    @RequestMapping("/getBanks")
    public Object getBanks(@RequestParam("cityId") Integer cityId) {
        return ReturnResult.success(bankService.getBanks(cityId));
    }

    @RequestMapping("/getSubBanks")
    public Object getSubBanks(@RequestParam("bankId") Integer bankId) {
        return ReturnResult.success(bankService.getSubBanks(bankId));
    }

    @PostMapping("/upload")
    public Object uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ReturnResult.fail("文件不能为空");
        }

        String key = IdUtil.randomUUID();
        try {
            return ReturnResult.success(QiniuUtil.uploadMultipartFile(file, key, false));
        } catch (IOException e) {
            LOGGER.error("上传文件失败", e);
            return ReturnResult.fail("上传失败,请联系管理员");
        }
    }

}
