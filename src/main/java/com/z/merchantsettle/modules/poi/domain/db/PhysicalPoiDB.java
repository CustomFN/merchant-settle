package com.z.merchantsettle.modules.poi.domain.db;

import lombok.Data;

@Data
public class PhysicalPoiDB {

    private Integer id;
    private Integer physicalCityId;
    private Integer physicalRegionId;
    private String physicalPoiName;
    private String physicalPoiPhone;
    private Integer physicalPoiCategory;
    private String physicalPoiAddress;
    private String physicalPoiLongitude;
    private String physicalPoiLatitude;
    private String physicalPoiPrincipal;
    private Integer status;
    private Long ctime;
    private Integer valid;
    private Integer claimed;

}
