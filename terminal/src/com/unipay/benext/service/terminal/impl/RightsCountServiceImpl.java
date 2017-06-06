package com.unipay.benext.service.terminal.impl;

import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.mapper.RightsCountMapper;
import com.unipay.benext.model.entity.RightsCount;
import com.unipay.benext.service.terminal.RightsCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 */
@Service
public class RightsCountServiceImpl implements RightsCountService{
    @Autowired
    private RightsCountMapper rightsCountMapper;

    @Override
    public void save(List<RightsCount> rightsCountList) {
        rightsCountMapper.delAll();
        rightsCountMapper.save(rightsCountList);
    }

    @Override
    public void save2(List<JSONObject> rightsCountList) {
        for(JSONObject jsonObject:rightsCountList){
            RightsCount rightsCount = JSONObject.parseObject(jsonObject.toJSONString(),RightsCount.class);
            RightsCount  oldrightsCount = this.rightsCountMapper.getRightsCountByFilter(rightsCount);
            if(oldrightsCount != null)
                this.rightsCountMapper.updateRightsCount(rightsCount);
            else
                rightsCountMapper.add(rightsCount) ;
        }
    }
}
