package com.z.merchantsettle.modules.poi.controller;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.common.ValidationResult;
import com.z.merchantsettle.exception.PhysicalPoiException;
import com.z.merchantsettle.exception.UpmException;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.domain.PhysicalPoiReqParam;
import com.z.merchantsettle.modules.poi.domain.bo.PhysicalPoi;
import com.z.merchantsettle.modules.poi.service.PhysicalPoiService;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.utils.ValidationUtil;
import com.z.merchantsettle.utils.shiro.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/physicalpoi")
public class PhysicalPoiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhysicalPoiController.class);

    @Autowired
    private PhysicalPoiService physicalPoiService;

    @RequestMapping("/listall")
    public Object listAll(PhysicalPoiReqParam physicalPoiReqParam,
                       @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(name = "pageSize", defaultValue = "30") Integer pageSize) {
        LOGGER.info("PhysicalPoiController listAll = {}, pageNum = {}, pageSize = {}", JSON.toJSONString(physicalPoiReqParam), pageNum, pageSize);
        if (pageNum == null || pageNum < 1 || pageSize == null || pageSize < 1) {
            return ReturnResult.fail("参数错误");
        }
        return ReturnResult.success(physicalPoiService.getList("", physicalPoiReqParam, pageNum, pageSize));
    }

    @RequestMapping("/claim/{physicalPoiId}")
    public Object claimPhysicalPoi(@PathVariable(name = "physicalPoiId") Integer physicalPoiId) {
        if (physicalPoiId == null || physicalPoiId <= 0) {
            return ReturnResult.fail("参数错误");
        }

        User user = ShiroUtils.getSysUser();
        if (user == null) {
            return ReturnResult.fail("系统异常");
        }

        physicalPoiService.claimPhysicalPoi(user.getUserId(), physicalPoiId);
        return ReturnResult.success();
    }

    @RequestMapping("/list")
    public Object list(PhysicalPoiReqParam physicalPoiReqParam,
                       @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(name = "pageSize", defaultValue = "30") Integer pageSize) {
        LOGGER.info("PhysicalPoiController list = {}, pageNum = {}, pageSize = {}", JSON.toJSONString(physicalPoiReqParam), pageNum, pageSize);
        if (pageNum == null || pageNum < 1 || pageSize == null || pageSize < 1) {
            return ReturnResult.fail("参数错误");
        }

        User user = ShiroUtils.getSysUser();
        if (user == null) {
            return ReturnResult.fail("系统异常");
        }

        return ReturnResult.success(physicalPoiService.getList(user.getUserId(), physicalPoiReqParam, pageNum, pageSize));
    }

    @RequestMapping("/save")
    public Object saveOrUpdate(PhysicalPoi physicalPoi) {
        LOGGER.info("PhysicalPoiController saveOrUpdate = {}", JSON.toJSONString(physicalPoi));
        if (physicalPoi == null) {
            return ReturnResult.fail("参数错误");
        }

        ValidationResult validateResult = ValidationUtil.validate(physicalPoi);
        if (validateResult.isHasError()) {
            return ReturnResult.fail(validateResult.getErrorMsgStr());
        }


        try {
            User user = ShiroUtils.getSysUser();
            physicalPoiService.save(physicalPoi, user.getUserId());
            return ReturnResult.success();
        } catch (UpmException | PhysicalPoiException e) {
            LOGGER.error("保存物理门店失败", e);
            return ReturnResult.fail(PoiConstant.POI_PARAM_ERROR ,"保存失败");
        }
    }
}
