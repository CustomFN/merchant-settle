package com.z.merchantsettle.modules.poi.service.callback;

public interface WmPoiCallBackService {

    void wmPoiBaseInfoAuditCallBack(Integer wmPoiId, Integer auditStatus, String auditResult);

    void wmPoiQuaAuditCallBack(Integer recordId, Integer wmPoiId, Integer auditStatus, String auditResult);

    void wmPoiBusinessInfoAuditCallBack(Integer wmPoiId, Integer auditStatus, String auditResult);

    void wmPoiDeliveryInfoAuditCallBack(Integer recordId, Integer wmPoiId, Integer auditStatus, String auditResult);
}
