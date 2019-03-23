package com.z.merchantsettle.modules.base.dao;

import com.z.merchantsettle.modules.base.domain.bo.CityInfo;
import com.z.merchantsettle.modules.base.domain.bo.ProvinceInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeoDBMapper {

    List<ProvinceInfo> getProvinces();

    List<CityInfo> getCities(@Param("provinceId") Integer provinceId);
}
