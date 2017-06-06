package com.unipay.benext.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.model.cloud.Bill;
import com.unipay.benext.service.cloud.*;
import com.unipay.benext.utils.HttpUtil;
import com.unipay.benext.utils.JsonOperate;
import com.unipay.benext.utils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/4.
 */
public class CloudTask {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    CardBinService cardBinService;
    @Autowired
    RightsCountService rightsCountService;
    @Autowired
    ParkService parkService;
    @Autowired
    RightsService rightsService;
    @Autowired
    BillService billService;

    /**
     * 数据中心定时全量同步权益平台的cardbininfo信息
     */
    public void takeCardBinList(){
        try {
            Map url = parkService.getBenefitUrl();
            if (url!=null){
                String result = HttpUtil.HttpPost(url.get("benefitUrl")+PropertyUtils.getPropertyValue("benefit.card_bin"),null);
                if (null!=result&&!"".equals(result)) {
                    JSONObject obj = JSONObject.parseObject(result);
                    if (obj!=null&&obj.getBoolean("isSuccess")){
                        List<JSONObject> list = JSONArray.toJavaObject(obj.getJSONArray("itemList"),List.class);
                        if (list!=null&&list.size()>0){
                            cardBinService.saveCardBinList(list);
                        }
                    }
                }
            }else {
                log.warn("获取权益平台url失败========");
            }
        }catch (Exception e){
            log.error("数据中心定时全量同步权益平台的cardbininfo信息出错",e);
        }
    }

    /**
     * 数据中心定时全量同步权益平台rightsCount信息
     */
    public void takeRightsCount(){
        try {
            Map url = parkService.getBenefitUrl();
            if (url!=null){
                String result = HttpUtil.HttpPost(url.get("benefitUrl")+PropertyUtils.getPropertyValue("benefit.rights_count"),null);
                if (null!=result&&!"".equals(result)) {
                    JSONObject obj = JSONObject.parseObject(result);
                    if (obj!=null&&obj.getBoolean("isSuccess")){
                        List<JSONObject> list = JSONArray.toJavaObject(obj.getJSONArray("itemList"),List.class);
                        if (list!=null&&list.size()>0){
                            rightsCountService.saveRightsCount(list);
                        }
                    }
                }
            }else {
                log.warn("获取权益平台url失败========");
            }
        }catch (Exception e){
            log.error("数据中心定时全量同步权益平台rightsCount信息出错",e);
        }
    }

    /**
     * 数据中心定时全量同步权益平台停车场信息
     */
    public void takeSupplyerInfo(){
        try {
            Map url = parkService.getBenefitUrl();
            if (url!=null){
                String result = HttpUtil.HttpPost(url.get("benefitUrl")+PropertyUtils.getPropertyValue("benefit.supplyer"),null);
                if (null!=result&&!"".equals(result)) {
                    JSONObject obj = JSONObject.parseObject(result);
                    if (obj!=null&&obj.getBoolean("isSuccess")){
                        List<JSONObject> list = JSONArray.toJavaObject(obj.getJSONArray("itemList"),List.class);
                        if (list!=null&&list.size()>0){
                            parkService.saveAllPartInfo(list);
                        }
                    }
                }
            }else {
                log.warn("获取权益平台url失败========");
            }
        }catch (Exception e){
            log.error("数据中心定时全量同步权益平台停车场信息出错",e);
        }
    }

    /**
     * 数据中心定时全量同步权益平台商户对应权益信息
     */
    public void takeAllClientRightsModel(){
        try {
            Map url = parkService.getBenefitUrl();
            if (url!=null){
                String result = HttpUtil.HttpPost(url.get("benefitUrl")+PropertyUtils.getPropertyValue("benefit.clientright"),null);
                if (null!=result&&!"".equals(result)) {
                    JSONObject obj = JSONObject.parseObject(result);
                    if (obj!=null&&obj.getBoolean("isSuccess")){
                        List<JSONObject> list = JSONArray.toJavaObject(obj.getJSONArray("itemList"),List.class);
                        if (list!=null&&list.size()>0){
                            List outList = new ArrayList();
                            for(JSONObject json : list){
                                JSONObject outJson = new JSONObject();
                                String display = json.getString("display");
                                if(display.indexOf("2小时") > -1){
                                    outJson.put("effectType",1);
                                    outJson.put("deductionHours",2);
                                }
                                else if(display.indexOf("48小时") > -1){
                                    outJson.put("effectType",2);
                                    outJson.put("deductionHours",48);
                                }
                                outJson.put("cilentId",json.getString("supplyId"));
                                outJson.put("rightsType",json.getString("rights"));
                                outJson.put("clientName",json.getString("supplyName"));
                                outJson.put("payRule",json.getString("selfRule1"));
                                double price = json.getDouble("price");
                                price = price/100;
                                outJson.put("price",price);
                                outList.add(outJson);
                            }
                            rightsService.saveAllClientRightsModel(outList);
                        }
                    }
                }
            }else {
                log.warn("获取权益平台url失败========");
            }
        }catch (Exception e){
            log.error("数据中心定时全量同步权益平台商户对应权益信息出错",e);
        }
    }
    /**
     * 数据中心定时全量同步权益平台卡对应权益信息
     */
    public void takeCardRightsInfo(){
        try {
            final String jsonFile = this.getClass().getResource("/").getPath()+"config.json";
            System.out.println("==========file地址"+jsonFile);
            JSONObject outObj = new JSONObject();
            JSONObject obj = JsonOperate.readJson(jsonFile);
            System.out.println("==========obj"+obj.toString());
            String lastUpdateTime = obj.getString("rightsLastUpdateTime");//终端最近一次同步时间
            outObj.put("lastUpdateTime",lastUpdateTime);
            Map url = parkService.getBenefitUrl();
            if (url!=null){
                String result = "";
                if(!"".equals(lastUpdateTime))
                    result = HttpUtil.HttpPost(url.get("benefitUrl")+PropertyUtils.getPropertyValue("benefit.cardrightinc"),outObj);
                else
                    result =   HttpUtil.HttpPost(url.get("benefitUrl")+PropertyUtils.getPropertyValue("benefit.cardright"),null);
                if (null!=result&&!"".equals(result)) {
                    JSONObject inobj = JSONObject.parseObject(result);
                    if (inobj!=null&&inobj.getBoolean("isSuccess")){
                        lastUpdateTime =  inobj.getString("message");
                        List<JSONObject> list = JSONArray.toJavaObject(inobj.getJSONArray("itemList"),List.class);
                        if (list!=null&&list.size()>0){
                            rightsService.saveAllRights(list);
                        }
                        obj.put("rightsLastUpdateTime",lastUpdateTime);
                        JsonOperate.writeFile(jsonFile,obj.toJSONString());
                    }
                }
            }else {
                log.warn("获取权益平台url失败========");
            }
        }catch (Exception e){
            log.error("数据中心定时全量同步权益平台卡对应权益信息出错",e);
        }
    }

    /**
     * 上传流水到权益平台
     */
    public void uploadBill(){
        try {
            Map map = new HashMap<>();
            map.put("uploadTag","0");
            List<Bill> billList = billService.getBill(map);
            if (billList!=null&&billList.size()>0){
                JSONObject obj = new JSONObject();
                obj.put("arrayList",JSONArray.toJSONString(billList));
                Map url = parkService.getBenefitUrl();
                if (url!=null){
                    String result = HttpUtil.HttpPost(url.get("benefitUrl")+PropertyUtils.getPropertyValue("benefit.useCardRights"),obj);
                    if (null!=result&&!"".equals(result)) {
                        JSONObject json = JSONObject.parseObject(result);
                        if (json!=null&&json.getBoolean("isSuccess")){
                            Map errorMap = new HashMap();
                            if (null!=json.get("count")){
                                Integer count = json.getInteger("count");
                                if (count>0){
                                    JSONArray array = json.getJSONArray("itemList");
                                    for (int i=0;i<array.size();i++){
                                        Bill bill = JSONObject.toJavaObject(array.getJSONObject(i),Bill.class);
                                        errorMap.put(bill.getBillNo()+"-"+bill.getClientId()+"-"+bill.getTerminalId(),1);
                                    }
                                }
                            }
                            List<String> ids = new ArrayList<>();
                            for (Bill bill:billList){
                                if (errorMap.get(bill.getBillNo()+"-"+bill.getClientId()+"-"+bill.getTerminalId())!=null){
                                    continue;//错误不改变状态
                                }
                                ids.add(bill.getId());
                            }
                            if(ids != null && ids.size() > 0)
                                billService.updateTagBatchO(ids);//改变状态
                        }
                    }
                }else {
                    log.warn("获取权益平台url失败========");
                }
            }
        }catch (Exception e){
            log.error("上传流水到权益平台出错",e);
        }
    }
}