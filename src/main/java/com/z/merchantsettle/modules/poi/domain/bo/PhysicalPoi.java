package com.z.merchantsettle.modules.poi.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PhysicalPoi {

    private Integer id;
    private Integer physicalCityId;
    private Integer physicalRegionId;

    @NotBlank(message = "物理门店名称不能为空")
    private String physicalPoiName;

    @NotBlank(message = "物理门店电话号码不能为空")
    private String physicalPoiPhone;
    private Integer physicalPoiCategory;

    @NotBlank(message = "物理门店地址不能为空")
    private String physicalPoiAddress;
    private String physicalPoiLongitude;
    private String physicalPoiLatitude;
    private String physicalPoiPrincipal;
    private Integer status;
    private Long ctime;
    private Integer valid;
    private Integer claimed;

}
