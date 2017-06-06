package com.unipay.benext.mapper;

import com.unipay.benext.model.entity.CardBin;

import java.util.List;

/**
 * Created by Administrator on 2017/2/18.
 */
public interface CardBinMapper {
    public void saveCardBin(List<CardBin> cardBinList);
    public List<CardBin> getLoclCardBinList();
    public CardBin getLoclCardBinByCardNo(String cardNo);
}
