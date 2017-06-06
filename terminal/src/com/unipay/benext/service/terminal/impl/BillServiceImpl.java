package com.unipay.benext.service.terminal.impl;

import com.unipay.benext.mapper.BillMapper;
import com.unipay.benext.model.park.Bill;
import com.unipay.benext.service.terminal.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 */
@Service
public class BillServiceImpl implements BillService{
    @Autowired
    private BillMapper billMapper;

    @Override
    public List<Bill> getBill(Bill bill) {
        return billMapper.getBill(bill);
    }

    @Override
    public void updateTagBatchO(List<Long> ids) {
        billMapper.updateTagBatchO(ids);
    }
}
