package com.unipay.benext.mapper;

import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.model.basic.Bill;
import com.unipay.benext.model.basic.ClientRightsModel;
import com.unipay.benext.model.basic.Rights;
import com.unipay.benext.model.basic.RightsCount;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by liuh on 2017/2/13 0013.
 */
@Transactional
public interface RightsMapper {
    public void add(Rights rights);
    public List<Rights> sel(Rights rights);
    void addClientRights(ClientRightsModel clientRightsModel);
    ClientRightsModel getClientRights(ClientRightsModel clientRightsModel);
    void updateClientRights(ClientRightsModel clientRightsModel);
    /**
     * 根据supplyId获取对应的权益类型信息(本地)
     * @param
     */

    public List<ClientRightsModel> getLocalClientRightsListByRightsIds(JSONObject json);

    /**
     * 根据卡号获取获取权益类型信息
     * @param
     * @return
     */
    public List<RightsCount> getRightsCountByRank(JSONObject json);

    Rights getRightsByFilter(JSONObject json);

    void addBill(Bill bill);

    List checkUseRights(String cardBin);

    public List<ClientRightsModel> getLocalClientRightsListByRightsIdsD(JSONObject json);

    Rights  getRightsByFilter(Rights rights);

    void  addRights(Rights rights);

    void  updateRights(Rights rights);

    List<Rights> getRights2();

    int getCount(Rights rights);

    void deleteAllClientRights(String supplyId);
}
