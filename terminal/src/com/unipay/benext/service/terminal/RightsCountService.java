package com.unipay.benext.service.terminal;

import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.model.entity.RightsCount;

import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 */
public interface RightsCountService {
    public void save(List<RightsCount> rightsCountList);
    public void save2(List<JSONObject> rightsCountList);
}
