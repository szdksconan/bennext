package com.unipay.benext.service.basic;

import com.unipay.benext.framework.tool.Json;
import com.unipay.benext.model.basic.Bill;
import com.unipay.benext.model.basic.ClientRightsModel;
import com.unipay.benext.model.basic.Rights;
import com.unipay.benext.model.basic.RightsCount;

import java.util.List;

/**
 * Created by WEI on 2017/2/16.
 */
public interface RightsService {
    public Json addRightsList();

    /**
     * 根据supplyId获取对应的权益类型信息
     * @param supplyId
     */

    public void addClientRightsListBySupplyId(String supplyId);

    /**
     * 根据rightsIds获取对应的权益类型信息(本地)
     * @param rights
     */

    public List<ClientRightsModel> getLocalClientRightsListByRightsIds(List list);

    /**
     * 根据rank获取权益类型信息
     * @param rank
     * @return
     */
    public List<RightsCount> getRightsCountByRank(String rank);

    /**
     * 根据rightsIds获取对应的权益类型信息(本地)按
     * @param list
     */
    public List<ClientRightsModel> getLocalClientRightsListSortByRightsIds(List list,String order);

    /**
     * 根据条件查询权益信息
     */
    Rights getRightsByFilter(String rightsId, String carBin);

    void addBill(Bill bill);

    /**
     * 判断该卡当天是否已使用权益
     */
    boolean checkUseRights(String cardBin);

    /**
     * 根据rightsIds获取对应的权益类型信息(本地)
     * @param rights
     */

    public List<ClientRightsModel> getLocalClientRightsListByRightsIdsD(List list);

    List<Rights> match();
}
