package com.z.merchantsettle.modules.poi.controller;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.common.ValidationResult;
import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.exception.UpmException;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiQua;
import com.z.merchantsettle.modules.poi.service.WmPoiQuaService;
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
@RequestMapping("/wmpoi/qua")
public class WmPoiQuaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmPoiQuaController.class);

    @Autowired
    private WmPoiQuaService wmPoiQuaService;

    @RequestMapping("/save")
    public Object saveOrUpdate(@RequestBody WmPoiQua wmPoiQua) {
        LOGGER.info("WmPoiQuaController saveOrUpdate = {}", JSON.toJSONString(wmPoiQua));
        if (wmPoiQua == null) {
            return ReturnResult.fail("参数错误");
        }

        ValidationResult validateResult = ValidationUtil.validate(wmPoiQua);
        if (validateResult.isHasError()) {
            return ReturnResult.fail(validateResult.getErrorMsgStr());
        }

        try {
            User user = ShiroUtils.getSysUser();
            wmPoiQuaService.saveOrUpdate(wmPoiQua, user.getUserId());
            return ReturnResult.success();
        } catch (UpmException | PoiException e) {
            LOGGER.error("保存门店资质信息异常", e);
            return ReturnResult.fail(PoiConstant.POI_OP_ERROR, "保存门店资质信息异常");
        }
    }

    @RequestMapping("/show/{wmPoiId}")
    public Object getWmPoiQuaById(@RequestParam(name = "wmPoiId") Integer wmPoiId, @RequestParam(name = "effective") Integer effective) {
        LOGGER.info("getWmPoiQuaById wmPoiId = {}, effective = {}", wmPoiId, effective);
        if (wmPoiId == null || wmPoiId <= 0 || effective < 0) {
            return ReturnResult.fail(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        try {
            return ReturnResult.success(wmPoiQuaService.getWmPoiQuaByWmPoiId(wmPoiId, effective));
        } catch (PoiException e) {
            LOGGER.error("查询门店资质信息异常", e);
            return ReturnResult.fail(e.getCode(), e.getMsg());
        }
    }
}
