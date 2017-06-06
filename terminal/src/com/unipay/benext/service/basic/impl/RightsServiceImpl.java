package com.unipay.benext.service.basic.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.framework.tool.Json;
import com.unipay.benext.mapper.RightsMapper;
import com.unipay.benext.model.basic.Bill;
import com.unipay.benext.model.basic.ClientRightsModel;
import com.unipay.benext.model.basic.Rights;
import com.unipay.benext.model.basic.RightsCount;
import com.unipay.benext.service.basic.RightsService;
import com.unipay.benext.service.park.ParkService;
import com.unipay.benext.utils.HttpUtil;
import com.unipay.benext.utils.JsonOperate;
import com.unipay.benext.utils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by WEI on 2017/2/16.
 */
@Service
public class RightsServiceImpl implements RightsService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    RightsMapper rightsMapper;
    @Autowired
    private ParkService parkService;

    @Override
    public Json addRightsList() {
        Json json = new Json();
        try {
            log.warn("=========同步用户权益信息开始");
            final String jsonFile = this.getClass().getResource("/").getPath()+"config.json";
            Map param = new HashMap<>();
            JSONObject obj = JsonOperate.readJson(jsonFile);
            if (obj!=null){
                String lastUpdateTime = obj.getString("rightsLastUpdateTime");//终端最近一次同步时间
                JSONObject _j = new JSONObject();
                _j.put("lastUpdateTime",lastUpdateTime);
                if (lastUpdateTime==null||"".equals(lastUpdateTime)){
                    _j.put("type","2");//全部更新
                }else {
                    _j.put("type","1");//增量更新
                }
                param.put("param",_j.toJSONString());
                List<Map> urlList = parkService.getCloudSet();
                if (urlList!=null&&urlList.size()>0){
                    String result = HttpUtil.HttpPost(urlList.get(0).get("cloudUrl").toString()+PropertyUtils.getPropertyValue("cloud.getRightsInfoList"),param);
                    if (null!=result&&!"".equals(result)){
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        if (jsonObject!=null&&jsonObject.getBoolean("tag")){
                            lastUpdateTime = jsonObject.get("lastUpdateTime").toString();
                            List<Rights> list = JSONArray.parseArray(JSONObject.toJSONString(jsonObject.get("rights")),Rights.class );
                            if (list!=null&&list.size()>0){
                                for (Rights rights : list){
                                    //判断记录是否已存在
                                    //存在或不存在
                                    Rights oldRights = this.rightsMapper.getRightsByFilter(rights);
                                    if(oldRights != null){
                                        oldRights.setRightsType(rights.getRightsType());
                                        oldRights.setRightsMaxCount(rights.getRightsMaxCount());
                                        oldRights.setPayRule(rights.getPayRule());
                                        oldRights.setRightsCount(rights.getRightsCount());
                                        oldRights.setUpdateTime(new Date());
                                        this.rightsMapper.updateRights(oldRights);
                                    }
                                    else{
                                        rights.setUpdateTime(new Date());
                                        this.rightsMapper.addRights(rights);
                                    }
                                }
                            }
                            obj.put("rightsLastUpdateTime",lastUpdateTime);
                            JsonOperate.writeFile(jsonFile,obj.toJSONString());
                        }
                    }
                }else {
                    log.warn("获取云平台接口url失败==========");
                }
                json.setSuccess(true);
                json.setMsg("同步成功！");
                log.warn("=========同步用户权益信息结束");
            }
        }catch (Exception e){
            json.setSuccess(false);
            json.setMsg("同步失败！");
            e.printStackTrace();
            log.warn("=========同步用户权益信息异常",e);
        }
        return json;
        //return result;

    }


    @Override
    public void addClientRightsListBySupplyId(String supplyId) {
        log.warn("同步商户权益信息开始==========");
        List<Map> urlList = parkService.getCloudSet();
        if (urlList!=null&&urlList.size()>0){
            String url = urlList.get(0).get("cloudUrl").toString()+PropertyUtils.getPropertyValue("cloud.client_rights");
            JSONObject obj =  new JSONObject();
            obj.put("supplyId",supplyId);
            try {
                this.rightsMapper.deleteAllClientRights(supplyId);
                String result = HttpUtil.HttpPost(url,obj);
                List<ClientRightsModel> list = JSONArray.parseArray(result,ClientRightsModel.class);
                for(ClientRightsModel clientRightsModel : list){
                    ClientRightsModel oldClientRightsModel = this.rightsMapper.getClientRights(clientRightsModel);
                    if(oldClientRightsModel != null)
                        this.rightsMapper.updateClientRights(clientRightsModel);
                    else
                        this.rightsMapper.addClientRights(clientRightsModel);
                }
                log.warn("同步商户权益信息结束==========");
            }
            catch (Exception e){
                e.printStackTrace();
                log.warn("同步商户权益信息异常==========",e);
            }
        }else {
            log.warn("获取云平台接口url失败==========");
        }
    }

    @Override
    public List<ClientRightsModel> getLocalClientRightsListByRightsIds(List list) {
        JSONObject json = new JSONObject();
        json.put("list",list);
        return this.rightsMapper.getLocalClientRightsListByRightsIds(json);
    }

    @Override
    public List<RightsCount> getRightsCountByRank(String rank) {
        JSONObject json = new JSONObject();
        json.put("rank", rank);
        return this.rightsMapper.getRightsCountByRank(json);
    }

    @Override
    public List<ClientRightsModel> getLocalClientRightsListSortByRightsIds(List list, String order) {
        JSONObject json = new JSONObject();
        json.put("list",list);
        json.put("orderSql",order);
        return this.rightsMapper.getLocalClientRightsListByRightsIds(json);
    }

    @Override
    public Rights getRightsByFilter(String rightsId, String cardBin) {
        JSONObject json = new JSONObject();
        json.put("rightsType",rightsId);
        json.put("cardNo",cardBin);
        return this.rightsMapper.getRightsByFilter(json);
    }

    @Override
    public void addBill(Bill bill) {
        this.rightsMapper.addBill(bill);
    }

    @Override
    public boolean checkUseRights(String cardBin) {
        List list = this.rightsMapper.checkUseRights(cardBin);
        if(list != null && list.size() >0)
            return  false;
        else
            return  true;
    }

    @Override
    public List<ClientRightsModel> getLocalClientRightsListByRightsIdsD(List list) {
        JSONObject json = new JSONObject();
        json.put("list",list);
        return this.rightsMapper.getLocalClientRightsListByRightsIdsD(json);
    }

    @Override
    public List<Rights> match() {
        List<Rights> list =  this.rightsMapper.getRights2();
        int j = 0;
        List<Rights> matchList = new ArrayList();
        for(Rights rights : list){
            int count = this.rightsMapper.getCount(rights);
            if(count > 1){
                j++;
                matchList.add(rights);
            }
        }
        return  matchList;
    }
}
