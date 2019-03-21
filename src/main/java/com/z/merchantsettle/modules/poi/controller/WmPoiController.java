package com.z.merchantsettle.modules.poi.controller;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.exception.UpmException;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.domain.WmPoiSearchParam;
import com.z.merchantsettle.modules.poi.service.WmPoiService;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.utils.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wmpoi")
public class WmPoiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmPoiController.class);

    @Autowired
    private WmPoiService wmPoiService;

    @RequestMapping("/list")
    public Object list(WmPoiSearchParam wmPoiSearchParam,
                       @RequestParam(defaultValue = "1", name = "pageNum") Integer pageNum,
                       @RequestParam(defaultValue = "30", name = "pageSize") Integer pageSize) {
        LOGGER.info("WmPoiController wmPoiSearchParam = {}, pageNum = {}, pageSize = {}", JSON.toJSONString(wmPoiSearchParam), pageNum, pageSize);
        if (pageNum == null || pageNum < 1 || pageSize == null || pageSize < 1) {
            return ReturnResult.fail("参数错误");
        }
        return ReturnResult.success(wmPoiService.getBaseList(wmPoiSearchParam, pageNum, pageSize));
    }

    @RequestMapping("/distributePrincipal")
    public Object distributePrincipal(@RequestParam("wmPoiId") Integer wmPoiId, @RequestParam("wmPoiPrincipal") String principalId) {
        LOGGER.info("WmPoiController##distributePrincipal wmPoiId = {}, principalId = {}", wmPoiId, principalId);
        if (wmPoiId == null || wmPoiId <= 0 || StringUtils.isBlank(principalId)) {
            return ReturnResult.fail("参数错误");
        }
        try {
            User user = ShiroUtils.getSysUser();
            wmPoiService.distributePrincipal(wmPoiId, principalId, user.getUserId());
            return ReturnResult.success();
        } catch (UpmException | PoiException e) {
            LOGGER.error("分配门店责任人异常", e);
            return ReturnResult.fail(PoiConstant.POI_OP_ERROR, "分配门店责任人异常");
        }
    }

}
