package com.z.merchantsettle.modules.customer.dao;

import com.z.merchantsettle.modules.customer.domain.db.CustomerKpAuditedDB;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerKpAuditedDBMapper {

    CustomerKpAuditedDB selectByCustomerId(Integer id);

    void deleteById(Integer id);

    void insertSelective(CustomerKpAuditedDB customerKpAuditedDB);

    void updateByIdSelective(CustomerKpAuditedDB customerKpAuditedDB);
}
