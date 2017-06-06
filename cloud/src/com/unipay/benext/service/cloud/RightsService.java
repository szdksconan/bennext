package com.unipay.benext.service.cloud;

import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.model.basic.RequestModel;
import com.unipay.benext.model.cloud.ClientRightsModel;
import com.unipay.benext.model.cloud.ParkSupply;
import com.unipay.benext.model.cloud.Rights;

import java.util.List;

/**
 * Created by Administrator on 2017/2/15 0015.
 */
public interface RightsService {
    List<Rights> getRightsInfoList(JSONObject param);

    /** --------------权益平台 停车场商户相关---------------- */
    public List<ParkSupply> getParkSupplyList(ParkSupply parkSupply);

    /**
     * 根据商户id获取停车场权益信息
     * @param clientId
     * @return
     */
    Rights getRightsInfoForClientId(String clientId);

    /**
     * 获取商户支持的权益类型
     * @param supplyId
     * @return
     */
    List getClientRightsListByFilter(String  supplyId);

    /**
     * 获取停车场信息
     * @param requestModel
     * @return
     */
    List getParkListByFilter(RequestModel requestModel);

    int getParkCountListByFilter(RequestModel requestModel);

    void updatePark(RequestModel requestModel);

    /**
     * 保存商户对应权益信息
     * @param list
     */

    void saveAllClientRightsModel(List<JSONObject> list);
    /**
     * 保存卡片权益信息
     */
    void saveAllRights(List<JSONObject> list);

}
