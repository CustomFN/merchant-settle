package com.z.merchantsettle.modules.base.dao;

import com.z.merchantsettle.modules.base.domain.bo.BankInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankInfoDBMapper {

    List<BankInfo> getBanks(@Param("cityId") Integer cityId);

    List<BankInfo> getSubBanks(@Param("bankId") Integer bankId);

    BankInfo getByBankIdAndBranchId(@Param("bankId") Integer bankId, @Param("branchId") String branchId);

    BankInfo getByBankId(@Param("bankId") Integer bankId);
}
