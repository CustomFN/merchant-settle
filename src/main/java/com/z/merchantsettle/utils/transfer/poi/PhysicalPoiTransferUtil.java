package com.z.merchantsettle.utils.transfer.poi;

import com.google.common.collect.Lists;
import com.z.merchantsettle.modules.poi.domain.bo.PhysicalPoi;
import com.z.merchantsettle.modules.poi.domain.db.PhysicalPoiDB;
import com.z.merchantsettle.utils.TransferUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class PhysicalPoiTransferUtil {

    public static PhysicalPoi transPhysicalPoiDB2Bo(PhysicalPoiDB physicalPoiDB) {
        if (physicalPoiDB == null) {
            return null;
        }

        PhysicalPoi physicalPoi = new PhysicalPoi();
        TransferUtil.transferAll(physicalPoiDB, physicalPoi);
        return physicalPoi;
    }

    public static List<PhysicalPoi> transPhysicalPoiDBList2BoList(List<PhysicalPoiDB> physicalPoiDBList) {
        if (CollectionUtils.isEmpty(physicalPoiDBList)) {
            return Lists.newArrayList();
        }

        List<PhysicalPoi> physicalPoiList = Lists.newArrayList();
        for (PhysicalPoiDB physicalPoiDB : physicalPoiDBList) {
            physicalPoiList.add(transPhysicalPoiDB2Bo(physicalPoiDB));
        }
        return physicalPoiList;
    }

    public static PhysicalPoiDB transPhysicalPoi2DB(PhysicalPoi physicalPoi) {
        if (physicalPoi == null) {
            return null;
        }

        PhysicalPoiDB physicalPoiDB = new PhysicalPoiDB();
        TransferUtil.transferAll(physicalPoi, physicalPoiDB);
        return physicalPoiDB;
    }
}
