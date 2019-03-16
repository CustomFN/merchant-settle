package com.z.merchantsettle.modules.audit.service.callback;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.modules.audit.domain.AuditResult;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.customer.service.callback.CustomerCallbackService;
import com.z.merchantsettle.modules.poi.service.callback.WmPoiCallBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WmPoiBaseInfoCallbackHandler extends AbstractHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmPoiBaseInfoCallbackHandler.class);

    @Autowired
    private WmPoiCallBackService wmPoiCallBackService;

    @Override
    public void handleCallback(AuditResult result, AuditTask auditTask) {
        LOGGER.info("WmPoiBaseInfoCallbackHandler auditResult = {}, auditTask = {}", JSON.toJSONString(result), JSON.toJSONString(auditTask));

        Integer auditStatus = result.getAuditStatus();
        String auditResult = result.getResult();
        Integer wmPoiId = auditTask.getPoiId();
        wmPoiCallBackService.wmPoiBaseInfoAuditCallBack(wmPoiId, auditStatus, auditResult);
    }
}
