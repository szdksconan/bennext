package com.unipay.benext.mapper;

import com.unipay.benext.model.cloud.Bill;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/13.
 */
public interface BillMapper {
    public void save(List<Bill> billList)throws Exception;
    /**
     * 获取流水信息
     */
    public List<Bill> getBill(Map map);
    public int getBillCount(Map map);

    void updateTagBatchO(List<String> ids);
}
