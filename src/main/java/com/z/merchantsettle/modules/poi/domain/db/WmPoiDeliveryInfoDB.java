package com.z.merchantsettle.modules.poi.domain.db;

import com.z.merchantsettle.modules.poi.domain.bo.WmPoiProject;
import lombok.Data;

import java.util.List;

@Data
public class WmPoiDeliveryInfoDB {

    private Integer id;
    private Integer wmPoiId;
    private Integer wmDeliveryType;

    private Double poportion;
    private Double minMoney;
    private String wmPoiProjects;

    private Integer status;
    private String auditResult;
    private Long ctime;
    private Long utime;
    private Integer valid;
}
