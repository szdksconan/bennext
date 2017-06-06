package com.unipay.benext.service.cloud.impl;

import com.unipay.benext.mapper.BillMapper;
import com.unipay.benext.model.cloud.Bill;
import com.unipay.benext.service.cloud.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/13.
 */
@Service
public class BillServiceImpl implements BillService{
    @Autowired
    private BillMapper billMapper;

    public synchronized void save(List<Bill> billList) throws Exception {
        this.billMapper.save(billList);
    }

    /**
     * 获取流水信息
     */
    @Override
    public List<Bill> getBill(Map map) {
        return billMapper.getBill(map);
    }

    @Override
    public int getBillCount(Map map) {
        return billMapper.getBillCount(map);
    }

    @Override
    public void updateTagBatchO(List<String> ids) {
        billMapper.updateTagBatchO(ids);
    }
}
