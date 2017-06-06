package com.unipay.benext.service.cloud;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/4.
 */
public interface ParkService {
    void saveAllPartInfo(List<JSONObject> list);

    Map getBenefitUrl();

    void saveBenefitUrl(Map map);

}
