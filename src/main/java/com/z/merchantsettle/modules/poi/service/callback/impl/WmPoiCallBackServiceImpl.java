package com.z.merchantsettle.modules.poi.service.callback.impl;

import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiBaseInfo;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiDeliveryInfo;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiQua;
import com.z.merchantsettle.modules.poi.service.WmPoiBaseInfoService;
import com.z.merchantsettle.modules.poi.service.WmPoiBusinessInfoService;
import com.z.merchantsettle.modules.poi.service.WmPoiDeliveryInfoService;
import com.z.merchantsettle.modules.poi.service.WmPoiQuaService;
import com.z.merchantsettle.modules.poi.service.callback.WmPoiCallBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WmPoiCallBackServiceImpl implements WmPoiCallBackService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmPoiCallBackServiceImpl.class);

    @Autowired
    private WmPoiBaseInfoService wmPoiBaseInfoService;

    @Autowired
    private WmPoiQuaService wmPoiQuaService;

    @Autowired
    private WmPoiBusinessInfoService wmPoiBusinessInfoService;

    @Autowired
    private WmPoiDeliveryInfoService wmPoiDeliveryInfoService;

    private static final int retryTime = 3;

    @Override
    public void wmPoiBaseInfoAuditCallBack(Integer wmPoiId, Integer auditStatus, String auditResult) {
        LOGGER.info("WmPoiBaseInfoAuditCallBack wmPoiId = {}, auditStatus = {}, auditResult = {}", wmPoiId, auditStatus, auditResult);
        int retry = 0;
        boolean isRetry = true;

        while (isRetry && retry++ <= retryTime) {
            try {
                if (AuditConstant.AuditStatus.AUDIT_PASS == auditStatus) {
                    wmPoiBaseInfoService.setupEffectWmPoiBaseInfo(wmPoiId);
                } else {
                    WmPoiBaseInfo wmPoiBaseInfo = new WmPoiBaseInfo();
                    wmPoiBaseInfo.setId(wmPoiId);
                    wmPoiBaseInfo.setStatus(PoiConstant.PoiModuleStatus.AUDIT_REJECT.getCode());
                    wmPoiBaseInfo.setAuditResult(auditResult);
                    wmPoiBaseInfoService.saveOrUpdate(wmPoiBaseInfo, "审核系统()");
                }
                isRetry = false;
            } catch (PoiException e) {
                LOGGER.warn("审核任务回调门店基本信息失败,重试第 {} 次", retry, e);
            }
        }
    }

    @Override
    public void wmPoiQuaAuditCallBack(Integer recordId, Integer wmPoiId, Integer auditStatus, String auditResult) {
        LOGGER.info("WmPoiQuaAuditCallBack recordId = {}, wmPoiId = {}, auditStatus = {}, auditResult = {}", recordId, wmPoiId, auditStatus, auditResult);
        int retry = 0;
        boolean isRetry = true;

        while (isRetry && retry++ <= retryTime) {
            try {
                if (AuditConstant.AuditStatus.AUDIT_PASS == auditStatus) {
                    wmPoiQuaService.setupEffectWmPoiQua(wmPoiId);
                } else {
                    WmPoiQua wmPoiQua = new WmPoiQua();
                    wmPoiQua.setId(recordId);
                    wmPoiQua.setWmPoiId(wmPoiId);
                    wmPoiQua.setStatus(PoiConstant.PoiModuleStatus.AUDIT_REJECT.getCode());
                    wmPoiQua.setAuditResult(auditResult);
                    wmPoiQuaService.saveOrUpdate(wmPoiQua, "审核系统()");
                }
                isRetry = false;
            } catch (PoiException e) {
                LOGGER.warn("审核任务回调门店资质信息失败,重试第 {} 次", retry, e);
            }
        }
    }

    @Override
    public void wmPoiBusinessInfoAuditCallBack(Integer wmPoiId, Integer auditStatus, String auditResult) {
        LOGGER.info("WmPoiBusinessInfoAuditCallBack wmPoiId = {}, auditStatus = {}, auditResult = {}", wmPoiId, auditStatus, auditResult);
        int retry = 0;
        boolean isRetry = true;

        while (isRetry && retry++ <= retryTime) {
            try {
                if (AuditConstant.AuditStatus.AUDIT_PASS == auditStatus) {
                    wmPoiBusinessInfoService.setupEffectWmPoiBusinessInfo(wmPoiId);
                } else {
                    WmPoiBaseInfo wmPoiBaseInfo = new WmPoiBaseInfo();
                    wmPoiBaseInfo.setId(wmPoiId);
                    wmPoiBaseInfo.setBusinessInfoStatus(PoiConstant.PoiModuleStatus.AUDIT_REJECT.getCode());
                    wmPoiBaseInfo.setBusinessInfoAuditResult(auditResult);
                    wmPoiBusinessInfoService.saveOrUpdate(wmPoiBaseInfo, "审核系统()");
                }
                isRetry = false;
            } catch (PoiException e) {
                LOGGER.warn("审核任务回调门店营业信息失败,重试第 {} 次", retry, e);
            }
        }
    }

    @Override
    public void wmPoiDeliveryInfoAuditCallBack(Integer recordId, Integer wmPoiId, Integer auditStatus, String auditResult) {
        LOGGER.info("WmPoiDeliveryInfoAuditCallBack recordId = {}, wmPoiId = {}, auditStatus = {}, auditResult = {}", recordId, wmPoiId, auditStatus, auditResult);
        int retry = 0;
        boolean isRetry = true;

        while (isRetry && retry++ <= retryTime) {
            try {
                if (AuditConstant.AuditStatus.AUDIT_PASS == auditStatus) {
                    wmPoiDeliveryInfoService.setupEffectWmPoiDeliveryInfo(wmPoiId);
                } else {
                    WmPoiDeliveryInfo wmPoiDeliveryInfo = new WmPoiDeliveryInfo();
                    wmPoiDeliveryInfo.setId(recordId);
                    wmPoiDeliveryInfo.setWmPoiId(wmPoiId);
                    wmPoiDeliveryInfo.setStatus(PoiConstant.PoiModuleStatus.AUDIT_REJECT.getCode());
                    wmPoiDeliveryInfo.setAuditResult(auditResult);
                    wmPoiDeliveryInfoService.saveOrUpdate(wmPoiDeliveryInfo, "审核系统()");
                }
                isRetry = false;
            } catch (PoiException e) {
                LOGGER.warn("审核任务回调门店配送信息失败,重试第 {} 次", retry, e);
            }
        }
    }
}
