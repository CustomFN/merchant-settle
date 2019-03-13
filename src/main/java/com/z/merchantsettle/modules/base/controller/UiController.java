package com.z.merchantsettle.modules.base.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.modules.base.service.BankService;
import com.z.merchantsettle.utils.qiniu.QiniuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/ui")
public class UiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UiController.class);

    @Autowired
    private BankService bankService;

    @RequestMapping("/getBanks")
    public Object getBanks() {
        return ReturnResult.success(bankService.getBanks());
    }

    @RequestMapping("/getSubBanks/{bankId}")
    public Object getSubBanks(@PathVariable(name = "bankId") Integer bankId) {
        return ReturnResult.success(bankService.getSubBanks(bankId));
    }

    @RequestMapping("/getGeoInfo")
    public Object getGeoInfo() {
        Resource provincesRes = new ClassPathResource("json/provinces.json");
        Resource citiesRes = new ClassPathResource("json/cities.json");
        Resource areasRes = new ClassPathResource("json/areas.json");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("provinces", JSON.parse(IoUtil.read(provincesRes.getInputStream(), Charset.forName("UTF-8"))));
            jsonObject.put("cities", JSON.parse(IoUtil.read(citiesRes.getInputStream(), Charset.forName("UTF-8"))));
            jsonObject.put("areas", JSON.parse(IoUtil.read(areasRes.getInputStream(), Charset.forName("UTF-8"))));

            return ReturnResult.success(jsonObject);
        } catch (IOException e) {
            return ReturnResult.fail();
        }
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

    public static void main(String[] args) {
        UiController uiController = new UiController();
        uiController.getGeoInfo();
    }
}
