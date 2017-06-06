package com.unipay.benext.mapper;


import com.unipay.benext.model.park.Bill;

import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */
public interface BillMapper {
    /**
     * 获取流水信息
     */
    public List<Bill> getBill(Bill bill);

    void updateTagBatchO(List<Long> ids);
}
