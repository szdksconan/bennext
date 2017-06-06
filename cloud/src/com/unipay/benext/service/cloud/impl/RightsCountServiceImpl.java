package com.unipay.benext.service.cloud.impl;

import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.mapper.RightsCountMapper;
import com.unipay.benext.model.cloud.RightsCount;
import com.unipay.benext.service.cloud.RightsCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/15 0015.
 */
@Service
public class RightsCountServiceImpl implements RightsCountService{
    @Autowired
    private RightsCountMapper rightsCountMapper;

    @Override
    public List<RightsCount> getList() {
        return rightsCountMapper.getList();
    }

    @Override
    public void saveRightsCount(List<JSONObject> list) {
        rightsCountMapper.deleteAllRightsCount();
        List<RightsCount> rightsCountList = new ArrayList<>();
        for (JSONObject obj : list){
            rightsCountList.add(JSONObject.toJavaObject(obj,RightsCount.class));
        }
        rightsCountMapper.saveAllRightsCount(rightsCountList);
    }
}
