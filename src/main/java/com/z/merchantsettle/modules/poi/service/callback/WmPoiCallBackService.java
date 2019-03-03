package com.z.merchantsettle.modules.poi.service.callback;

public interface WmPoiCallBackService {

    void WmPoiBaseInfoAuditCallBack(Integer wmPoiId, Integer auditStatus, String auditResult, String opUserId);

    void WmPoiQuaAuditCallBack(Integer recordId, Integer wmPoiId, Integer auditStatus, String auditResult, String opUserId);

    void WmPoiBusinessInfoAuditCallBack(Integer wmPoiId, Integer auditStatus, String auditResult, String opUserId);

    void WmPoiDeliveryInfoAuditCallBack(Integer recordId, Integer wmPoiId, Integer auditStatus, String auditResult, String opUserId);
}
