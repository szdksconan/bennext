package com.unipay.benext.service.terminal.impl;

import com.unipay.benext.mapper.CardBinMapper;
import com.unipay.benext.model.entity.CardBin;
import com.unipay.benext.service.terminal.CardBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/2/18.
 */
@Service
public class CardBinServiceImpl implements CardBinService{
    @Autowired
    private CardBinMapper cardBinMapper;

    public void saveCardBin(List<CardBin> cardBinList){
        cardBinMapper.saveCardBin(cardBinList);
    }

    public List<CardBin> getLoclCardBinList(){
        return  this.cardBinMapper.getLoclCardBinList();
    }

    @Override
    public CardBin getLoclCardBinByCardNo(String cardNo) {
        return this.cardBinMapper.getLoclCardBinByCardNo(cardNo);
    }
}
