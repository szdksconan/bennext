package com.unipay.benext.service.cloud.impl;

import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.mapper.RightsMapper;
import com.unipay.benext.model.basic.RequestModel;
import com.unipay.benext.model.cloud.ClientRightsModel;
import com.unipay.benext.model.cloud.ParkSupply;
import com.unipay.benext.model.cloud.Rights;
import com.unipay.benext.service.cloud.RightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/2/15 0015.
 */
@Service
public class RightsServiceImpl implements RightsService {
    @Autowired
    RightsMapper rightsMapper;

    @Override
    public List<Rights> getRightsInfoList(JSONObject param) {
        return  this.rightsMapper.getRightsInfoList(param);
    }

    /** -----------------------权益平台 停车场商户相关---------------- */
    @Override
    public List<ParkSupply> getParkSupplyList(ParkSupply parkSupply) {
        return rightsMapper.getParkSupplyList(parkSupply);
    }

    /**
     * 根据商户id获取停车场权益信息
     * @param clientId
     * @return
     */
    @Override
    public Rights getRightsInfoForClientId(String clientId) {
        return rightsMapper.getRightsInfoForClientId(clientId);
    }

    @Override
    public List getClientRightsListByFilter(String supplyId) {
        return this.rightsMapper.getClientRightsListByFilter(supplyId);
    }

    @Override
    public List getParkListByFilter(RequestModel requestModel) {
        return this.rightsMapper.getParkListByFilter(requestModel);
    }

    @Override
    public int getParkCountListByFilter(RequestModel requestModel) {
        return this.rightsMapper.getParkCountListByFilter(requestModel);
    }

    @Override
    public void updatePark(RequestModel requestModel) {
         this.rightsMapper.updatePark(requestModel);
    }

    @Override
    public void saveAllClientRightsModel(List<JSONObject> list) {
        rightsMapper.deleteAllClientRightsModel();
        List<ClientRightsModel> clientRightsList = new ArrayList<>();
        for (JSONObject obj : list){
           clientRightsList.add(JSONObject.toJavaObject(obj,ClientRightsModel.class));
        }
        rightsMapper.saveAllClientRightsModel(clientRightsList);
    }

    @Override
    public void saveAllRights(List<JSONObject> list) {
        //rightsMapper.deleteAllRights();

        List<Rights> RightsList = new ArrayList<>();
        try {
            for (JSONObject obj : list){
                Rights rights = JSONObject.toJavaObject(obj,Rights.class);
                //判断记录是否已存在
                //存在或不存在
                Rights oldRights = this.rightsMapper.getRightsByFilter(rights);
                if(oldRights != null){
                    oldRights.setPayRule(rights.getPayRule());
                    oldRights.setRightsCount(rights.getRightsCount());
                    oldRights.setUpdateTime(new Date());
                    this.rightsMapper.updateRights(oldRights);
                }
                else{
                    rights.setUpdateTime(new Date());
                    this.rightsMapper.addRights(rights);
                }
                JSONObject outJson = new JSONObject();
//                outJson.put("incId",rights.getIncId());
//                outJson.put("tag","1");
         //       String result = HttpUtil.HttpPost(PropertyUtils.getPropertyValue("benefit.updateRightsInc"),outJson);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
