package com.z.merchantsettle.modules.audit.service.callback;

import com.z.merchantsettle.exception.AuditException;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.constants.AuditTypeEnum;

public class AuditCallbackService {

    public static AbstractHandler getCallbackHandler(int auditType) {

        if (AuditTypeEnum.CUSTOMER.getCode() == auditType) {
            return new CustomerCallbackHandler();
        } else if (AuditTypeEnum.CUSTOMER_KP.getCode() == auditType) {
            return new CustomerKpCallbackHandler();
        } else if (AuditTypeEnum.CUSTOMER_CONTRACT.getCode() == auditType) {
            return new CustomerContractCallbackHandler();
        } else if (AuditTypeEnum.CUSTOMER_SETTLE.getCode() == auditType) {
            return new CustomerSettleCallbackHandler();
        } else if (AuditTypeEnum.POI_BASE_INFO.getCode() == auditType) {
            return new WmPoiBaseInfoCallbackHandler();
        } else if (AuditTypeEnum.POI_QUA_INFO.getCode() == auditType) {
            return new WmPoiQuaCallbackHandler();
        } else if (AuditTypeEnum.POI_DELIVERY_INFO.getCode() == auditType) {
            return new WmPoiDeliveryInfoCallbackHandler();
        } else if (AuditTypeEnum.POI_BUSINESS_INFO.getCode() == auditType) {
            return new WmPoiBusinessInfoCallbackHandler();
        }

        throw new AuditException(AuditConstant.AUDIT_OP_ERROR, "不存在此审核类型");
    }
}
