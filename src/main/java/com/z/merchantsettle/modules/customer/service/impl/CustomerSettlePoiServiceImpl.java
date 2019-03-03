package com.z.merchantsettle.modules.customer.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.sun.org.apache.regexp.internal.RE;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.dao.CustomerSettlePoiAuditedDBMapper;
import com.z.merchantsettle.modules.customer.dao.CustomerSettlePoiDBMapper;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettlePoi;
import com.z.merchantsettle.modules.customer.domain.db.CustomerSettlePoiDB;
import com.z.merchantsettle.modules.customer.service.CustomerSettlePoiService;
import com.z.merchantsettle.utils.transfer.customer.CustomerTransferUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CustomerSettlePoiServiceImpl implements CustomerSettlePoiService {

    @Autowired
    private CustomerSettlePoiDBMapper customerSettlePoiDBMapper;

    @Autowired
    private CustomerSettlePoiAuditedDBMapper customerSettlePoiAuditedDBMapper;


    @Override
    public List<Integer> getSettleIdByWmPoiId(Integer wmPoiId) {
        return customerSettlePoiDBMapper.getSettleIdByWmPoiId(wmPoiId);
    }

    @Override
    public List<Integer> getSettleIdByWmPoiIdAudited(Integer wmPoiId) {
        return customerSettlePoiAuditedDBMapper.getSettleIdByWmPoiId(wmPoiId);
    }

    @Override
    public List<CustomerSettlePoi> getBySettleId(Integer settleId) {
        List<CustomerSettlePoiDB> customerSettlePoiDBList = customerSettlePoiDBMapper.getBySettleId(settleId);
        return CustomerTransferUtil.transCustomerSettlePoiDBList2BoList(customerSettlePoiDBList);
    }

    @Override
    public List<CustomerSettlePoi> getBySettleIdAudited(Integer settleId) {
        List<CustomerSettlePoiDB> customerSettlePoiAuditedDBList = customerSettlePoiAuditedDBMapper.getBySettleId(settleId);
        return CustomerTransferUtil.transCustomerSettlePoiDBList2BoList(customerSettlePoiAuditedDBList);
    }

    @Override
    public void saveOrUpdateSettlePoi(Integer settleId, List<Integer> wmPoiIdList) {
        if (settleId == null || settleId <= 0 || CollectionUtils.isEmpty(wmPoiIdList)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        List<CustomerSettlePoiDB> customerSettlePoiDBList = customerSettlePoiDBMapper.getBySettleId(settleId);
        List<Integer> wmPoiIdListInDB = Lists.transform(customerSettlePoiDBList, new Function<CustomerSettlePoiDB, Integer>() {
            @Override
            public Integer apply(CustomerSettlePoiDB input) {
                return input.getWmPoiId();
            }
        });

        Collection<Integer> toDeleteIdList = CollectionUtils.subtract(wmPoiIdListInDB, wmPoiIdList);
        Collection<Integer> toSaveIdList = CollectionUtils.subtract(wmPoiIdList, wmPoiIdListInDB);
        if (CollectionUtils.isNotEmpty(toDeleteIdList)) {
            customerSettlePoiDBMapper.deleteBySettleIdAndPoiIdList(settleId, Lists.newArrayList(toDeleteIdList));
        }
        if (CollectionUtils.isEmpty(toSaveIdList)) {
            return;
        }

        List<CustomerSettlePoiDB> toSaveDBList = getCustomerSettlePoiDBList(settleId, toSaveIdList);
        customerSettlePoiDBMapper.save(toSaveDBList);
    }

    private List<CustomerSettlePoiDB> getCustomerSettlePoiDBList(int settleId, Collection<Integer> toSaveIdList) {
        List<CustomerSettlePoiDB> toSaveDBList = Lists.newArrayList();
        for (Integer wmPoiId : toSaveIdList) {
            CustomerSettlePoiDB customerSettlePoiDB = new CustomerSettlePoiDB();
            customerSettlePoiDB.setSettleId(settleId);
            customerSettlePoiDB.setWmPoiId(wmPoiId);

            toSaveDBList.add(customerSettlePoiDB);
        }
        return toSaveDBList;
    }

    @Override
    public void saveOrUpdateSettlePoiAudited(List<CustomerSettlePoi> customerSettlePoiList) {
        if (CollectionUtils.isEmpty(customerSettlePoiList)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        int settleId = customerSettlePoiList.get(0).getSettleId();
        List<Integer> wmPoiIdList = Lists.transform(customerSettlePoiList, new Function<CustomerSettlePoi, Integer>() {
            @Override
            public Integer apply(CustomerSettlePoi input) {
                return input.getWmPoiId();
            }
        });

        List<CustomerSettlePoiDB> customerSettlePoiDBList = customerSettlePoiAuditedDBMapper.getBySettleId(settleId);
        List<Integer> wmPoiIdListInDB = Lists.transform(customerSettlePoiDBList, new Function<CustomerSettlePoiDB, Integer>() {
            @Override
            public Integer apply(CustomerSettlePoiDB input) {
                return input.getWmPoiId();
            }
        });

        Collection<Integer> toDeleteIdList = CollectionUtils.subtract(wmPoiIdListInDB, wmPoiIdList);
        Collection<Integer> toSaveIdList = CollectionUtils.subtract(wmPoiIdList, wmPoiIdListInDB);
        if (CollectionUtils.isNotEmpty(toDeleteIdList)) {
            customerSettlePoiAuditedDBMapper.deleteBySettleIdAndPoiIdList(settleId, Lists.newArrayList(toDeleteIdList));
        }
        if (CollectionUtils.isEmpty(toSaveIdList)) {
            return;
        }

        List<CustomerSettlePoiDB> toSaveDBList = getCustomerSettlePoiDBList(settleId, toSaveIdList);
        customerSettlePoiAuditedDBMapper.save(toSaveDBList);
    }

    @Override
    public List<CustomerSettlePoi> getByWmPoiIdList(List<Integer> wmPoiIdList) {
        if (CollectionUtils.isEmpty(wmPoiIdList)) {
            return Lists.newArrayList();
        }

        List<CustomerSettlePoiDB> customerSettlePoiDBList = customerSettlePoiDBMapper.getByWmPoiIdList(wmPoiIdList);
        return CustomerTransferUtil.transCustomerSettlePoiDBList2BoList(customerSettlePoiDBList);
    }

}