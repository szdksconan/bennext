package com.unipay.benext.service.cloud;

import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.model.cloud.CardBin;

import java.util.List;

/**
 * Created by Administrator on 2017/2/18.
 */
public interface CardBinService {
    List<CardBin> getCarBinInfo(JSONObject obj);

    void saveCardBinList(List<JSONObject> list);
}
