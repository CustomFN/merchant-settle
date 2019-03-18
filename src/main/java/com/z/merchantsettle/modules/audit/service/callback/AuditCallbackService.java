package com.z.merchantsettle.modules.audit.service.callback;

import com.z.merchantsettle.exception.AuditException;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.constants.AuditTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditCallbackService {

    @Autowired
    private  CustomerCallbackHandler customerCallbackHandler;
    @Autowired
    private  CustomerKpCallbackHandler customerKpCallbackHandler;
    @Autowired
    private  CustomerContractCallbackHandler customerContractCallbackHandler;
    @Autowired
    private  CustomerSettleCallbackHandler customerSettleCallbackHandler;
    @Autowired
    private  WmPoiBaseInfoCallbackHandler wmPoiBaseInfoCallbackHandler;
    @Autowired
    private  WmPoiQuaCallbackHandler wmPoiQuaCallbackHandler;
    @Autowired
    private  WmPoiDeliveryInfoCallbackHandler wmPoiDeliveryInfoCallbackHandler;
    @Autowired
    private  WmPoiBusinessInfoCallbackHandler wmPoiBusinessInfoCallbackHandler;

    public  AbstractHandler getCallbackHandler(int auditType) {

        if (AuditTypeEnum.CUSTOMER.getCode() == auditType) {
            return customerCallbackHandler;
        } else if (AuditTypeEnum.CUSTOMER_KP.getCode() == auditType) {
            return customerKpCallbackHandler;
        } else if (AuditTypeEnum.CUSTOMER_CONTRACT.getCode() == auditType) {
            return customerContractCallbackHandler;
        } else if (AuditTypeEnum.CUSTOMER_SETTLE.getCode() == auditType) {
            return customerSettleCallbackHandler;
        } else if (AuditTypeEnum.POI_BASE_INFO.getCode() == auditType) {
            return wmPoiBaseInfoCallbackHandler;
        } else if (AuditTypeEnum.POI_QUA_INFO.getCode() == auditType) {
            return wmPoiQuaCallbackHandler;
        } else if (AuditTypeEnum.POI_DELIVERY_INFO.getCode() == auditType) {
            return wmPoiDeliveryInfoCallbackHandler;
        } else if (AuditTypeEnum.POI_BUSINESS_INFO.getCode() == auditType) {
            return wmPoiBusinessInfoCallbackHandler;
        }

        throw new AuditException(AuditConstant.AUDIT_OP_ERROR, "不存在此审核类型");
    }
}
