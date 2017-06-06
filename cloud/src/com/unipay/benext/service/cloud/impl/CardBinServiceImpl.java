package com.unipay.benext.service.cloud.impl;

import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.mapper.CardBinMapper;
import com.unipay.benext.model.cloud.CardBin;
import com.unipay.benext.service.cloud.CardBinService;
import com.unipay.benext.utils.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/18.
 */
@Service
public class CardBinServiceImpl implements CardBinService{
    @Autowired
    private CardBinMapper cardBinMapper;

    @Override
    public List<CardBin> getCarBinInfo(JSONObject obj) {
        return cardBinMapper.getCarBinInfo(obj);
    }

    @Override
    public void saveCardBinList(List<JSONObject> list) {
        cardBinMapper.deleteCardBin();
        List<CardBin> cardBinList = new ArrayList<>();
        for (JSONObject obj : list){
            try {
                CardBin cardBin = JSONObject.toJavaObject(obj,CardBin.class);
                if (Common.isNull(cardBin.getCardRank())) cardBin.setCardRank(-1);
                if (Common.isNull(cardBin.getCardStart())) cardBin.setCardStart(-1);
                if (Common.isNull(cardBin.getRangLength())) cardBin.setRangLength(-1);
                cardBinList.add(cardBin);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        cardBinMapper.saveCardBinList(cardBinList);
    }
}
