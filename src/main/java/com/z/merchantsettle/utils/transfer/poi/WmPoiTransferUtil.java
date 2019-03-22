package com.z.merchantsettle.utils.transfer.poi;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.constants.WmDeliveryTypeEnum;
import com.z.merchantsettle.modules.poi.domain.bo.*;
import com.z.merchantsettle.modules.poi.domain.db.WmPoiBaseInfoDB;
import com.z.merchantsettle.modules.poi.domain.db.WmPoiDeliveryInfoDB;
import com.z.merchantsettle.modules.poi.domain.db.WmPoiQuaDB;
import com.z.merchantsettle.utils.TransferUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class WmPoiTransferUtil {

    private static final String separator = ",";

    public static WmPoiBaseInfo transWmPoiBaseInfoDB2Bo(WmPoiBaseInfoDB wmPoiBaseInfoDB) {
        if (wmPoiBaseInfoDB == null) {
            return null;
        }

        WmPoiBaseInfo wmPoiBaseInfo = new WmPoiBaseInfo();
        TransferUtil.transferAll(wmPoiBaseInfoDB, wmPoiBaseInfo);

        if (StringUtils.isNotBlank(wmPoiBaseInfoDB.getWmPoiLogo())) {
            String[] pics = StringUtils.split(wmPoiBaseInfoDB.getWmPoiLogo(), separator);
            wmPoiBaseInfo.setWmPoiLogoList(Lists.newArrayList(pics));
        }
        if (StringUtils.isNotBlank(wmPoiBaseInfoDB.getWmPoiEnvironmentPic())) {
            String[] pics = StringUtils.split(wmPoiBaseInfoDB.getWmPoiEnvironmentPic(), separator);
            wmPoiBaseInfo.setWmPoiEnvironmentPicList(Lists.newArrayList(pics));
        }
        if (StringUtils.isNotBlank(wmPoiBaseInfoDB.getOrderMealDate())) {
            String[] pics = StringUtils.split(wmPoiBaseInfoDB.getOrderMealDate(), separator);
            wmPoiBaseInfo.setOrderMealDateList(Lists.newArrayList(pics));
        }
        if (wmPoiBaseInfoDB.getBusinessInfoStatus() != null && wmPoiBaseInfoDB.getBusinessInfoStatus() > 0) {
            wmPoiBaseInfo.setBusinessInfoStatusStr(PoiConstant.PoiModuleStatus.getByCode(wmPoiBaseInfoDB.getBusinessInfoStatus()));
        }
        wmPoiBaseInfo.setStatusStr(PoiConstant.PoiModuleStatus.getByCode(wmPoiBaseInfoDB.getStatus()));
        return wmPoiBaseInfo;
    }

    public static List<WmPoiBaseInfo> transWmPoiBaseInfoDBList2BoList(List<WmPoiBaseInfoDB> wmPoiBaseInfoDBList) {
        if (CollectionUtils.isEmpty(wmPoiBaseInfoDBList)) {
            return Lists.newArrayList();
        }

        List<WmPoiBaseInfo> wmPoiBaseInfoList = Lists.newArrayList();
        for (WmPoiBaseInfoDB wmPoiBaseInfoDB : wmPoiBaseInfoDBList) {
            wmPoiBaseInfoList.add(transWmPoiBaseInfoDB2Bo(wmPoiBaseInfoDB));
        }
        return wmPoiBaseInfoList;
    }

    public static WmPoiBaseInfoDB transWmPoiBaseInfo2DB(WmPoiBaseInfo wmPoiBaseInfo) {
        if (wmPoiBaseInfo == null) {
            return null;
        }

        WmPoiBaseInfoDB wmPoiBaseInfoDB = new WmPoiBaseInfoDB();
        TransferUtil.transferAll(wmPoiBaseInfo, wmPoiBaseInfoDB);

        if (CollectionUtils.isNotEmpty(wmPoiBaseInfo.getWmPoiLogoList())) {
            String pic = StringUtils.join(wmPoiBaseInfo.getWmPoiLogoList(), separator);
            wmPoiBaseInfoDB.setWmPoiLogo(pic);
        }
        if (CollectionUtils.isNotEmpty(wmPoiBaseInfo.getWmPoiEnvironmentPicList())) {
            String pic = StringUtils.join(wmPoiBaseInfo.getWmPoiEnvironmentPicList(), separator);
            wmPoiBaseInfoDB.setWmPoiEnvironmentPic(pic);
        }
        if (CollectionUtils.isNotEmpty(wmPoiBaseInfo.getOrderMealDateList())) {
            String pic = StringUtils.join(wmPoiBaseInfo.getOrderMealDateList(), separator);
            wmPoiBaseInfoDB.setOrderMealDate(pic);
        }
        return wmPoiBaseInfoDB;
    }

    public static WmPoiQuaDB transWmPoiQua2DB(WmPoiQua wmPoiQua) {
        if (wmPoiQua == null) {
            return null;
        }

        WmPoiQuaDB wmPoiQuaDB = new WmPoiQuaDB();
        TransferUtil.transferAll(wmPoiQua, wmPoiQuaDB);

        if (CollectionUtils.isNotEmpty(wmPoiQua.getWmPoiLinkManIDCardPicList())) {
            String pic = StringUtils.join(wmPoiQua.getWmPoiLinkManIDCardPicList(), separator);
            wmPoiQuaDB.setWmPoiLinkManIDCardPic(pic);
        }
        if (CollectionUtils.isNotEmpty(wmPoiQua.getWmPoiBusinessLicencePicList())) {
            String pic = StringUtils.join(wmPoiQua.getWmPoiBusinessLicencePicList(), separator);
            wmPoiQuaDB.setWmPoiBusinessLicencePic(pic);
        }
        return wmPoiQuaDB;
    }

    public static WmPoiQua transWmPoiQuaDB2Bo(WmPoiQuaDB wmPoiQuaDB) {
        if (wmPoiQuaDB == null) {
            return null;
        }

        WmPoiQua wmPoiQua = new WmPoiQua();
        TransferUtil.transferAll(wmPoiQuaDB, wmPoiQua);

        if (StringUtils.isNotBlank(wmPoiQuaDB.getWmPoiLinkManIDCardPic())) {
            String[] pics = StringUtils.split(wmPoiQuaDB.getWmPoiLinkManIDCardPic(), separator);
            wmPoiQua.setWmPoiLinkManIDCardPicList(Lists.newArrayList(pics));
        }
        if (StringUtils.isNotBlank(wmPoiQuaDB.getWmPoiBusinessLicencePic())) {
            String[] pics = StringUtils.split(wmPoiQuaDB.getWmPoiBusinessLicencePic(), separator);
            wmPoiQua.setWmPoiBusinessLicencePicList(Lists.newArrayList(pics));
        }
        wmPoiQua.setStatusStr(PoiConstant.PoiModuleStatus.getByCode(wmPoiQuaDB.getStatus()));
        return wmPoiQua;
    }

    public static List<WmPoiQua> transWmPoiQuaDBList2BoList(List<WmPoiQuaDB> wmPoiQuaDBList) {
        if (CollectionUtils.isEmpty(wmPoiQuaDBList)) {
            return Lists.newArrayList();
        }

        List<WmPoiQua> wmPoiQuaList = Lists.newArrayList();
        for (WmPoiQuaDB wmPoiQuaDB : wmPoiQuaDBList) {
            wmPoiQuaList.add(transWmPoiQuaDB2Bo(wmPoiQuaDB));
        }
        return wmPoiQuaList;
    }



    public static WmPoiDeliveryInfoDB transWmPoiDeliveryInfo2DB(WmPoiDeliveryInfo wmPoiDeliveryInfo) {
        if (wmPoiDeliveryInfo == null) {
            return null;
        }

        WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB = new WmPoiDeliveryInfoDB();
        TransferUtil.transferAll(wmPoiDeliveryInfo, wmPoiDeliveryInfoDB);

        wmPoiDeliveryInfoDB.setWmPoiProjects(JSON.toJSONString(wmPoiDeliveryInfo.getWmPoiProjectList()));
        return wmPoiDeliveryInfoDB;
    }

    public static WmPoiDeliveryInfo transWmPoiDeliveryInfoDB2Bo(WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB) {
        if (wmPoiDeliveryInfoDB == null) {
            return null;
        }

        WmPoiDeliveryInfo wmPoiDeliveryInfo = new WmPoiDeliveryInfo();
        TransferUtil.transferAll(wmPoiDeliveryInfoDB, wmPoiDeliveryInfo);
        wmPoiDeliveryInfo.setWmPoiProjectList(JSON.parseArray(wmPoiDeliveryInfoDB.getWmPoiProjects(),  WmPoiProject.class));

        wmPoiDeliveryInfo.setWmDeliveryTypeStr(WmDeliveryTypeEnum.getByCode(wmPoiDeliveryInfoDB.getWmDeliveryType()));
        wmPoiDeliveryInfo.setStatusStr(PoiConstant.PoiModuleStatus.getByCode(wmPoiDeliveryInfoDB.getStatus()));
        return wmPoiDeliveryInfo;
    }


    public static List<WmPoiDeliveryInfo> transWmPoiDeliveryInfoDBList2BoList(List<WmPoiDeliveryInfoDB> wmPoiDeliveryInfoDBList) {
        if (CollectionUtils.isEmpty(wmPoiDeliveryInfoDBList)) {
            return Lists.newArrayList();
        }

        List<WmPoiDeliveryInfo> wmPoiDeliveryInfoList = Lists.newArrayList();
        for (WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB : wmPoiDeliveryInfoDBList) {
            wmPoiDeliveryInfoList.add(transWmPoiDeliveryInfoDB2Bo(wmPoiDeliveryInfoDB));
        }
        return wmPoiDeliveryInfoList;
    }
}
