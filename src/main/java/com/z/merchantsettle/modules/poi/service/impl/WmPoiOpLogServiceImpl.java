package com.z.merchantsettle.modules.poi.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.dao.WmPoiOpLogMapper;
import com.z.merchantsettle.modules.poi.domain.WmPoiOpLog;
import com.z.merchantsettle.modules.poi.domain.WmPoiOpLogSearchParam;
import com.z.merchantsettle.modules.poi.service.WmPoiOpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WmPoiOpLogServiceImpl implements WmPoiOpLogService {

    @Autowired
    private WmPoiOpLogMapper wmPoiOpLogMapper;

    @Override
    public void addLog(Integer wmPoiId, String module, String content, String opUser) {
        WmPoiOpLog wmPoiOpLog = new WmPoiOpLog();
        wmPoiOpLog.setWmPoiId(wmPoiId);
        wmPoiOpLog.setModule(module);
        wmPoiOpLog.setContent(content);
        wmPoiOpLog.setOpUser(opUser);
        wmPoiOpLogMapper.insert(wmPoiOpLog);
    }

    @Override
    public PageData<WmPoiOpLog> getLogByWmPoiId(WmPoiOpLogSearchParam opLogSearchParam, Integer pageNum, Integer pageSize) throws PoiException {
        if (opLogSearchParam.getWmPoiId() == null || opLogSearchParam.getWmPoiId() <= 0) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<WmPoiOpLog> logList = wmPoiOpLogMapper.getLogByWmPoiId(opLogSearchParam);
        PageInfo<WmPoiOpLog> pageInfo = new PageInfo<>(logList);
        return new PageData.Builder<WmPoiOpLog>()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalSize((int) pageInfo.getTotal())
                .totalPage(pageInfo.getPageSize())
                .data(logList)
                .build();
    }
}
