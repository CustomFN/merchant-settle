package com.z.merchantsettle.modules.base.service.impl;

import com.z.merchantsettle.modules.base.dao.GeoDBMapper;
import com.z.merchantsettle.modules.base.domain.bo.CityInfo;
import com.z.merchantsettle.modules.base.domain.bo.ProvinceInfo;
import com.z.merchantsettle.modules.base.service.GeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeoServiceImpl implements GeoService {

    @Autowired
    private GeoDBMapper geoDBMapper;

    @Override
    public List<ProvinceInfo> getProvinces() {
        return geoDBMapper.getProvinces();
    }

    @Override
    public List<CityInfo> getCities(Integer provinceId) {
        return geoDBMapper.getCities(provinceId);
    }

    @Override
    public CityInfo getByProvinceIdAndCityId(Integer provinceId, Integer cityId) {
        return geoDBMapper.getByProvinceIdAndCityId(provinceId, cityId);
    }
}
