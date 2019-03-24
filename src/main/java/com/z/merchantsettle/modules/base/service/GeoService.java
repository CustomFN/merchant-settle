package com.z.merchantsettle.modules.base.service;

import com.z.merchantsettle.modules.base.domain.bo.CityInfo;
import com.z.merchantsettle.modules.base.domain.bo.ProvinceInfo;

import java.util.List;

public interface GeoService {

    List<ProvinceInfo> getProvinces();

    List<CityInfo> getCities(Integer provinceId);

    CityInfo getByProvinceIdAndCityId(Integer provinceId, Integer cityId);
}
