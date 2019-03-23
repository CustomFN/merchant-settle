package com.z.merchantsettle.modules.base.service.impl;

import com.z.merchantsettle.modules.base.dao.BankInfoDBMapper;
import com.z.merchantsettle.modules.base.domain.bo.BankInfo;
import com.z.merchantsettle.modules.base.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private BankInfoDBMapper bankInfoDBMapper;

    @Override
    public List<BankInfo> getBanks(Integer cityId) {
        return bankInfoDBMapper.getBanks(cityId);
    }

    @Override
    public List<BankInfo> getSubBanks(Integer bankId) {
        return bankInfoDBMapper.getSubBanks(bankId);
    }
}
