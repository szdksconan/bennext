package com.unipay.benext.service.cloud;

import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.model.cloud.RightsCount;

import java.util.List;

/**
 * Created by Administrator on 2017/2/15 0015.
 */
public interface RightsCountService {
    public List<RightsCount> getList();

    void saveRightsCount(List<JSONObject> list);
}
