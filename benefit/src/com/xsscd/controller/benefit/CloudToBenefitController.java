package com.xsscd.controller.benefit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.xsscd.entity.benefit.Bill;
import com.xsscd.service.benefit.CloudToBenefitService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/24.
 */
@ControllerBind(controllerKey = "interface/cloudToBenefit")
public class CloudToBenefitController extends Controller {
    private static final String headUrl = "interface/cloudToBenefit";

    /**
     * cardBin数据拉取接口
     */
    @ActionKey(headUrl+"/getCardBinList_no")
    public void getCardBinList(){
        CloudToBenefitService.service.findCardBinList(this);
    }

    /**
     * 卡级别对应权益类型拉取接口
     */
    @ActionKey(headUrl+"/getRightsCount_no")
    public void getRightsCount(){
        CloudToBenefitService.service.findRightsCountList(this);
    }

    /**
     * 停车场信息拉取接口
     */
    @ActionKey(headUrl+"/getSupplyerInfoList_no")
    public void getSupplyerInfoList(){
        CloudToBenefitService.service.getSupplyerInfoList(this);
    }

    /**
     * 商户对应权益类型拉取接口(全量)
     */
    @ActionKey(headUrl+"/getSupplyerRightsList_no")
    public void getSupplyerRightsList(){
        CloudToBenefitService.service.getSupplyerRightsList(this);
    }

    /**
     * 卡对应权益拉取
     */
    @ActionKey(headUrl+"/getCardRightsList_no")
    public void getCardRightsList(){
        CloudToBenefitService.service.getCardRightsList(this);
    }

    /**
     * 权益使用接口
     */
    @ActionKey(headUrl+"/useCardRights_no")
    public void useCardRights(){
        JSONArray array = JSONArray.parseArray(this.getPara("arrayList"));
        if (null!=array&&array.size()>0){
            List<Bill> list = new ArrayList<>();
            for (int i=0;i<array.size();i++){
                list.add(JSONObject.toJavaObject(array.getJSONObject(i),Bill.class));
            }
            CloudToBenefitService.service.useCardRights(this,list);
        }
    }

    /**
     * 增量
     */
    @ActionKey(headUrl+"/getCardRightsIncList_no")
    public void getCardRightsIncList_no(){
        try {
            String  UpdateTime = this.getPara("lastUpdateTime");
            CloudToBenefitService.service.getCardRightsIncList_no(this,UpdateTime);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}
