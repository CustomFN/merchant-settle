package com.z.merchantsettle.modules.poi.controller;

import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.modules.poi.service.WmPoiSettleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wmpoi/settle")
public class WmPoiSettleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmPoiSettleController.class);

    @Autowired
    private WmPoiSettleService wmPoiSettleService;

    @RequestMapping("/show/{wmPoiId}")
    public Object show(@PathVariable("wmPoiId") Integer wmPoiId) {
        LOGGER.info("WmPoiSettleController show wmPoiId = {}", wmPoiId);
        return ReturnResult.success(wmPoiSettleService.showSettleInfo(wmPoiId));
    }
}
