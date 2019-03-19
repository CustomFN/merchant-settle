package com.z.merchantsettle.modules.poi.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.PhysicalPoiException;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.dao.PhysicalPoiDBMapper;
import com.z.merchantsettle.modules.poi.domain.PhysicalPoiReqParam;
import com.z.merchantsettle.modules.poi.domain.bo.PhysicalPoi;
import com.z.merchantsettle.modules.poi.domain.db.PhysicalPoiDB;
import com.z.merchantsettle.modules.poi.service.PhysicalPoiService;
import com.z.merchantsettle.utils.transfer.poi.PhysicalPoiTransferUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhysicalPoiServiceImpl implements PhysicalPoiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhysicalPoiServiceImpl.class);

    @Autowired
    private PhysicalPoiDBMapper physicalPoiDBMapper;


    @Override
    public PageData<PhysicalPoi> getList(String userId, PhysicalPoiReqParam physicalPoiReqParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PhysicalPoiDB> physicalPoiDBList =  physicalPoiDBMapper.selectList(userId, physicalPoiReqParam);
        LOGGER.info("PhysicalPoiServiceImpl physicalPoiDBList = {}", JSON.toJSONString(physicalPoiDBList));
        PageInfo<PhysicalPoiDB> pageInfo = new PageInfo<>(physicalPoiDBList);

        List<PhysicalPoi> physicalPoiList = PhysicalPoiTransferUtil.transPhysicalPoiDBList2BoList(physicalPoiDBList);
        return new PageData.Builder<PhysicalPoi>()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalSize((int) pageInfo.getTotal())
                .totalPage(pageInfo.getPages())
                .data(physicalPoiList)
                .build();
    }

    @Override
    public void claimPhysicalPoi(String userId, Integer physicalPoiId) {
        LOGGER.info("claimPhysicalPoi userId = {}, physicalPoiId = {}", userId, physicalPoiId);

        PhysicalPoiDB physicalPoiDB = new PhysicalPoiDB();
        physicalPoiDB.setId(physicalPoiId);
        physicalPoiDB.setPhysicalPoiPrincipal(userId);
        physicalPoiDB.setClaimed(1);
        physicalPoiDBMapper.updateSelective(physicalPoiDB);
    }

    @Override
    public void save(PhysicalPoi physicalPoi, String opUserId) {
        if (physicalPoi == null || StringUtils.isBlank(opUserId)) {
            throw new PhysicalPoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        PhysicalPoiDB physicalPoiDB = PhysicalPoiTransferUtil.transPhysicalPoi2DB(physicalPoi);
        physicalPoiDBMapper.insertSelective(physicalPoiDB);
    }
}
