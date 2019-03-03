package com.z.merchantsettle.utils.transfer.poi;

import com.google.common.collect.Lists;
import com.z.merchantsettle.modules.poi.domain.bo.*;
import com.z.merchantsettle.modules.poi.domain.db.WmPoiBaseInfoDB;
import com.z.merchantsettle.modules.poi.domain.db.WmPoiDeliveryInfoDB;
import com.z.merchantsettle.modules.poi.domain.db.WmPoiQuaDB;
import com.z.merchantsettle.utils.TransferUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class WmPoiTransferUtil {

    public static WmPoiBaseInfo transWmPoiBaseInfoDB2Bo(WmPoiBaseInfoDB wmPoiBaseInfoDB) {
        if (wmPoiBaseInfoDB == null) {
            return null;
        }

        WmPoiBaseInfo wmPoiBaseInfo = new WmPoiBaseInfo();
        TransferUtil.transferAll(wmPoiBaseInfoDB, wmPoiBaseInfo);
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
        return wmPoiBaseInfoDB;
    }

    public static WmPoiQuaDB transWmPoiQua2DB(WmPoiQua wmPoiQua) {
        if (wmPoiQua == null) {
            return null;
        }

        WmPoiQuaDB wmPoiQuaDB = new WmPoiQuaDB();
        TransferUtil.transferAll(wmPoiQua, wmPoiQuaDB);
        return wmPoiQuaDB;
    }

    public static WmPoiQua transWmPoiQuaDB2Bo(WmPoiQuaDB wmPoiQuaDB) {
        if (wmPoiQuaDB == null) {
            return null;
        }

        WmPoiQua wmPoiQua = new WmPoiQua();
        TransferUtil.transferAll(wmPoiQuaDB, wmPoiQua);
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

    public static WmPoiQuaAudited transWmPoiQuaDB2Audited(WmPoiQuaDB wmPoiQuaDB) {
        if (wmPoiQuaDB == null) {
            return null;
        }

        WmPoiQuaAudited wmPoiQuaAudited = new WmPoiQuaAudited();
        TransferUtil.transferAll(wmPoiQuaDB, wmPoiQuaAudited);
        return wmPoiQuaAudited;
    }

    public static WmPoiQuaDB transWmPoiQuaAudited2DB(WmPoiQuaAudited wmPoiQuaAudited) {
        if (wmPoiQuaAudited == null) {
            return null;
        }

        WmPoiQuaDB wmPoiQuaDB =  new WmPoiQuaDB();
        TransferUtil.transferAll(wmPoiQuaAudited, wmPoiQuaDB);
        return wmPoiQuaDB;
    }

    public static WmPoiDeliveryInfoDB transWmPoiDeliveryInfo2DB(WmPoiDeliveryInfo wmPoiDeliveryInfo) {
        if (wmPoiDeliveryInfo == null) {
            return null;
        }

        WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB = new WmPoiDeliveryInfoDB();
        TransferUtil.transferAll(wmPoiDeliveryInfo, wmPoiDeliveryInfoDB);
        return wmPoiDeliveryInfoDB;
    }

    public static WmPoiDeliveryInfo transWmPoiDeliveryInfoDB2Bo(WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB) {
        if (wmPoiDeliveryInfoDB == null) {
            return null;
        }

        WmPoiDeliveryInfo wmPoiDeliveryInfo = new WmPoiDeliveryInfo();
        TransferUtil.transferAll(wmPoiDeliveryInfoDB, wmPoiDeliveryInfo);
        return wmPoiDeliveryInfo;
    }

    public static WmPoiDeliveryInfoAudited transWmPoiDeliveryInfoDB2Audited(WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB) {
        if (wmPoiDeliveryInfoDB == null) {
            return null;
        }

        WmPoiDeliveryInfoAudited wmPoiDeliveryInfoAudited = new WmPoiDeliveryInfoAudited();
        TransferUtil.transferAll(wmPoiDeliveryInfoDB, wmPoiDeliveryInfoAudited);
        return wmPoiDeliveryInfoAudited;
    }

    public static WmPoiDeliveryInfoDB transWmPoiDeliveryInfoAudited2DB(WmPoiDeliveryInfoAudited wmPoiDeliveryInfoAudited) {
        if (wmPoiDeliveryInfoAudited == null) {
            return null;
        }

        WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB = new WmPoiDeliveryInfoDB();
        TransferUtil.transferAll(wmPoiDeliveryInfoAudited, wmPoiDeliveryInfoDB);
        return wmPoiDeliveryInfoDB;
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
