package com.unipay.benext.mapper;

import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.model.entity.RightsCount;

import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 */
public interface RightsCountMapper {
    public void save(List<RightsCount> rightsCountList);

    public void delAll();

    public RightsCount getRightsCountByFilter(RightsCount rightsCount);

    public void updateRightsCount(RightsCount rightsCount);

    public void add(RightsCount rightsCount);
}
