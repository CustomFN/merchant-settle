package com.z.merchantsettle.modules.poi.controller;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.common.ValidationResult;
import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.exception.UpmException;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiDeliveryInfo;
import com.z.merchantsettle.modules.poi.service.WmPoiDeliveryInfoService;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.utils.ValidationUtil;
import com.z.merchantsettle.utils.shiro.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wmpoi/delivery")
public class WmPoiDeliveryInfoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmPoiDeliveryInfoController.class);

    @Autowired
    private WmPoiDeliveryInfoService wmPoiDeliveryInfoService;


    @RequestMapping("/save")
    public Object saveOrUpdate(@RequestBody WmPoiDeliveryInfo wmPoiDeliveryInfo) {
        LOGGER.info("WmPoiDeliveryController saveOrUpdate = {}", JSON.toJSONString(wmPoiDeliveryInfo));
        if (wmPoiDeliveryInfo == null) {
            return ReturnResult.fail("参数错误");
        }

        ValidationResult validateResult = ValidationUtil.validate(wmPoiDeliveryInfo);
        if (validateResult.isHasError()) {
            return ReturnResult.fail(validateResult.getErrorMsgStr());
        }

        try {
            User user = ShiroUtils.getSysUser();
            wmPoiDeliveryInfoService.saveOrUpdate(wmPoiDeliveryInfo, user.getUserId());
            return ReturnResult.success();
        } catch (UpmException | PoiException e) {
            LOGGER.error("保存门店配送信息异常", e);
            return ReturnResult.fail(PoiConstant.POI_OP_ERROR, "保存门店配送信息异常");
        }
    }

    @RequestMapping("/show/{wmPoiId}")
    public Object getWmPoiDeliveryInfoById(@RequestParam(name = "wmPoiId") Integer wmPoiId, @RequestParam(name = "effective") Integer effective) {
        LOGGER.info("getWmPoiDeliveryInfoById wmPoiId = {}, effective = {}", wmPoiId, effective);
        if (wmPoiId == null || wmPoiId <= 0 || effective < 0) {
            return ReturnResult.fail(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        try {
            return ReturnResult.success(wmPoiDeliveryInfoService.getWmPoiDeliveryInfoById(wmPoiId, effective));
        } catch (PoiException e) {
            LOGGER.error("查询门店配送信息异常", e);
            return ReturnResult.fail(e.getCode(), e.getMsg());
        }
    }
}
