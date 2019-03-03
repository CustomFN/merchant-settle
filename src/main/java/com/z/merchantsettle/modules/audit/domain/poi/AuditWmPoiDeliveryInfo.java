package com.z.merchantsettle.modules.audit.domain.poi;

import lombok.Data;

import java.util.List;

@Data
public class AuditWmPoiDeliveryInfo {

    private Integer recordId;
    private Integer wmPoiId;
    private Integer wmDeliveryType;

    private Double poportion;
    private Double minMoney;

    private List<AuditWmPoiProject> wmPoiProjectList;
}
