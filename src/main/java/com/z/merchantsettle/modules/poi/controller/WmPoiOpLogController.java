package com.z.merchantsettle.modules.poi.controller;

import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.domain.WmPoiOpLogSearchParam;
import com.z.merchantsettle.modules.poi.service.WmPoiOpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wmpoi/log")
public class WmPoiOpLogController {

    @Autowired
    private WmPoiOpLogService wmPoiOpLogService;

    @RequestMapping("/list")
    public Object getLogList(WmPoiOpLogSearchParam opLogSearchParam,
                             @RequestParam(defaultValue = "1", name = "pageNum") Integer pageNum,
                             @RequestParam(defaultValue = "30", name = "pageSize") Integer pageSize) {
        if (pageNum == null || pageNum < 1 || pageSize == null || pageSize < 1) {
            return ReturnResult.fail("参数错误");
        }
        try {
            return ReturnResult.success(wmPoiOpLogService.getLogByWmPoiId(opLogSearchParam, pageNum, pageSize));
        } catch (PoiException e) {
            return ReturnResult.fail(PoiConstant.POI_OP_ERROR, "查询操作日志错误");
        }
    }
}
