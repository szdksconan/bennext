package com.unipay.benext.service.terminal;

import com.unipay.benext.model.entity.CardBin;

import java.util.List;

/**
 * Created by Administrator on 2017/2/18.
 */
public interface CardBinService {
    public void saveCardBin(List<CardBin> cardBinList);
    //获取本地carBin
    List<CardBin> getLoclCardBinList();
    //获取符合的卡cardBin
    CardBin getLoclCardBinByCardNo(String cardNo);
}
