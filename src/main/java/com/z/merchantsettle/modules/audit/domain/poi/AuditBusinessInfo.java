package com.z.merchantsettle.modules.audit.domain.poi;

import lombok.Data;

@Data
public class AuditBusinessInfo {

    private Integer recordId;

    private Integer wmPoiId;

    private String orderMealDate;

    private String orderMealTime;

    private String orderMealTel;
}
