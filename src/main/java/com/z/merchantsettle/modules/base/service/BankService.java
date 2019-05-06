package com.z.merchantsettle.modules.base.service;

import com.z.merchantsettle.modules.base.domain.bo.BankInfo;

import java.util.List;

public interface BankService {

    List<BankInfo> getBanks(Integer cityId);

    List<BankInfo> getSubBanks(Integer bankId);

    BankInfo getByBankId(Integer bankId);

    BankInfo getByBankIdAndBranchId(Integer bankId, String branchId);
}
