package com.unipay.benext.mapper;

import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.model.cloud.CardBin;

import java.util.List;

/**
 * Created by Administrator on 2017/2/18.
 */
public interface CardBinMapper {
    List<CardBin> getCarBinInfo(JSONObject obj);

    void saveCardBinList(List<CardBin> list);

    void deleteCardBin();
}
