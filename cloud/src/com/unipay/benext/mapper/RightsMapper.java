package com.unipay.benext.mapper;

import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.model.basic.RequestModel;
import com.unipay.benext.model.cloud.ClientRightsModel;
import com.unipay.benext.model.cloud.ParkSupply;
import com.unipay.benext.model.cloud.Rights;

import java.util.List;

/**
 * Created by Administrator on 2017/2/11.
 */
public interface RightsMapper {
    public List<Rights> getRightsInfoList(JSONObject param);

    /** 停车场商户相关*/
    public List<ParkSupply> getParkSupplyList(ParkSupply parkSupply);

    /**
     * 根据商户id获取停车场权益信息
     * @param clientId
     * @return
     */
    public Rights getRightsInfoForClientId(String clientId);

    List<ClientRightsModel> getClientRightsListByFilter(RequestModel requestModel);

    int getClientRightsCountByFilter(RequestModel requestModel);

    List<ClientRightsModel> getClientRightsListByFilter(String supplyId);

    List getParkListByFilter(RequestModel requestModel) ;

    int getParkCountListByFilter(RequestModel requestModel) ;

    void updatePark(RequestModel requestModel);

    void deleteAllClientRightsModel();

    void saveAllClientRightsModel(List<ClientRightsModel> list);

    void deleteAllRights();

    void saveAllRights(List<Rights> list);

    Rights  getRightsByFilter(Rights rights);

    void  addRights(Rights rights);

    void  updateRights(Rights rights);

    List<Rights> getRights2();
}
