package com.z.merchantsettle.modules.customer.service;

import java.util.List;

public interface CustomerPoiService {

    List<Integer> getWmPoiIdListByCustomerId(Integer customerId);

    void deleteByCustomerIdOnOff(Integer customerId);

    void save(Integer customerId, Integer wmPoiId);

    void saveAudited(Integer customerId, Integer wmPoiId);
}
