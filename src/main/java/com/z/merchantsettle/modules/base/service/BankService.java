package com.z.merchantsettle.modules.base.service;

import com.z.merchantsettle.modules.base.domain.bo.BankInfo;

import java.util.List;

public interface BankService {

    List<BankInfo> getBanks();

    List<BankInfo> getSubBanks(Integer bankId);

}
