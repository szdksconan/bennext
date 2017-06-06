package com.unipay.benext.service.terminal;

import com.unipay.benext.model.park.Bill;

import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 */
public interface BillService {
    public List<Bill> getBill(Bill bill);

    public void updateTagBatchO(List<Long> ids);
}
