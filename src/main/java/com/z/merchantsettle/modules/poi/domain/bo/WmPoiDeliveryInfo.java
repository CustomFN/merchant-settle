package com.z.merchantsettle.modules.poi.domain.bo;

import lombok.Data;

import java.util.List;

@Data
public class WmPoiDeliveryInfo {

    private Integer id;
    private Integer wmPoiId;
    private Integer wmDeliveryType;

    private Double poportion;
    private Double minMoney;

    private List<WmPoiProject> wmPoiProjectList;

    private Integer status;
    private String auditResult;
}