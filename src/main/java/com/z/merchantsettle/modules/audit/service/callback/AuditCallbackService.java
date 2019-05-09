package com.z.merchantsettle.modules.audit.service.callback;

import com.google.common.collect.Maps;
import com.z.merchantsettle.exception.AuditException;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.constants.AuditTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuditCallbackService {

    private static Map<Integer, AbstractHandler> handlerMap = Maps.newConcurrentMap();

    static {
        handlerMap.put(AuditTypeEnum.CUSTOMER.getCode(), new CustomerCallbackHandler());
        handlerMap.put(AuditTypeEnum.CUSTOMER_KP.getCode(), new CustomerKpCallbackHandler());
        handlerMap.put(AuditTypeEnum.CUSTOMER_CONTRACT.getCode(), new CustomerContractCallbackHandler());
        handlerMap.put(AuditTypeEnum.CUSTOMER_SETTLE.getCode(), new CustomerSettleCallbackHandler());
        handlerMap.put(AuditTypeEnum.POI_BASE_INFO.getCode(), new WmPoiBaseInfoCallbackHandler());
        handlerMap.put(AuditTypeEnum.POI_QUA_INFO.getCode(), new WmPoiQuaCallbackHandler());
        handlerMap.put(AuditTypeEnum.POI_DELIVERY_INFO.getCode(), new WmPoiDeliveryInfoCallbackHandler());
        handlerMap.put(AuditTypeEnum.POI_BUSINESS_INFO.getCode(), new WmPoiBusinessInfoCallbackHandler());
    }

    public  AbstractHandler getCallbackHandler(int auditType) {

        AbstractHandler handler = handlerMap.get(auditType);
        if (handler != null) {
            return handler;
        }

        throw new AuditException(AuditConstant.AUDIT_OP_ERROR, "不存在此审核类型");
    }
}
