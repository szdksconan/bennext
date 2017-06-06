package com.unipay.benext.service.cloud.impl;

import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.mapper.ParkMapper;
import com.unipay.benext.model.cloud.Park;
import com.unipay.benext.service.cloud.ParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/4.
 */
@Service
public class ParkServiceImpl implements ParkService{
    @Autowired
    private ParkMapper parkMapper;

    @Override
    public void saveAllPartInfo(List<JSONObject> list) {
        parkMapper.deleteAllPark();
        List<Park> parkList = new ArrayList<>();
        for (JSONObject obj : list){
            parkList.add(JSONObject.toJavaObject(obj,Park.class));
        }
        parkMapper.saveAllPartInfo(parkList);
    }

    @Override
    public Map getBenefitUrl() {
        return parkMapper.getBenefitUrl();
    }

    @Override
    public void saveBenefitUrl(Map map) {
        parkMapper.saveBenefitUrl(map);
    }
}
