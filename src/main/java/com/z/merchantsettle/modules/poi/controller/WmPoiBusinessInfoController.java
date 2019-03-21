package com.z.merchantsettle.modules.poi.controller;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.common.ValidationResult;
import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.exception.UpmException;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiBaseInfo;
import com.z.merchantsettle.modules.poi.service.WmPoiBusinessInfoService;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.utils.ValidationUtil;
import com.z.merchantsettle.utils.shiro.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wmpoi/businessinfo")
public class WmPoiBusinessInfoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmPoiBusinessInfoController.class);

    @Autowired
    private WmPoiBusinessInfoService wmPoiBusinessInfoService;

    @RequestMapping("/save")
    public Object saveOrUpdate(WmPoiBaseInfo wmPoiBaseInfo) {
        LOGGER.info("WmPoiBusinessInfoController saveOrUpdate = {}", JSON.toJSONString(wmPoiBaseInfo));
        if (wmPoiBaseInfo == null) {
            return ReturnResult.fail("参数错误");
        }

        ValidationResult validateResult = ValidationUtil.validate(wmPoiBaseInfo);
        if (validateResult.isHasError()) {
            return ReturnResult.fail(validateResult.getErrorMsgStr());
        }

        try {
            User user = ShiroUtils.getSysUser();
            wmPoiBaseInfo = wmPoiBusinessInfoService.saveOrUpdate(wmPoiBaseInfo, user.getUserId());
            return ReturnResult.success(wmPoiBaseInfo);
        } catch (UpmException | PoiException e) {
            LOGGER.error("保存门店营业信息异常", e);
            return ReturnResult.fail(PoiConstant.POI_OP_ERROR, "保存门店营业信息异常");
        }
    }

    @RequestMapping("/show/{wmPoiId}")
    public Object getWmPoiBusinessInfoById(@PathVariable(name = "wmPoiId") Integer wmPoiId, @RequestParam(name = "effective") Integer effective) {
        LOGGER.info("getWmPoiBusinessInfoById wmPoiId = {}, effective = {}", wmPoiId, effective);
        if (wmPoiId == null || wmPoiId <= 0 || effective < 0) {
            return ReturnResult.fail(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        try {
            return ReturnResult.success(wmPoiBusinessInfoService.getWmPoiBaseInfoById(wmPoiId, effective));
        } catch (PoiException e) {
            LOGGER.error("查询门店营业信息异常", e);
            return ReturnResult.fail(e.getCode(), e.getMsg());
        }
    }
}
