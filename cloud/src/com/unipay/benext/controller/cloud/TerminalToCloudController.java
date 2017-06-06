package com.unipay.benext.controller.cloud;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.framework.tool.Json;
import com.unipay.benext.model.cloud.Bill;
import com.unipay.benext.model.cloud.ParkSupply;
import com.unipay.benext.model.cloud.Rights;
import com.unipay.benext.service.cloud.BillService;
import com.unipay.benext.service.cloud.CardBinService;
import com.unipay.benext.service.cloud.RightsCountService;
import com.unipay.benext.service.cloud.RightsService;
import com.unipay.benext.utils.DateFormatUtil;
import com.unipay.benext.utils.ExeclUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 终端发起调用接口
 * Created by Administrator on 2017/2/13.
 */
@Controller
@RequestMapping("terminalToCloud")
public class TerminalToCloudController {
    @Autowired
    private BillService billService;
    @Autowired
    private RightsService rightsService;
    @Autowired
    private CardBinService cardBinService;
    @Autowired
    private RightsCountService rightsCountService;

    /**
     * 终端上传流水
     * @param continualInfo
     * @return
     */
    @RequestMapping("addContinual")
    @ResponseBody
    public Json addContinual(String continualInfo){
        Json json = new Json();
        try {
            JSONArray array = JSONArray.parseArray(continualInfo);
            if (array!=null&&array.size()>0){
                List<Bill> billList = new ArrayList<>();
                for (int i=0;i<array.size();i++){
                    Bill bill = JSONObject.toJavaObject(array.getJSONObject(i),Bill.class);
                    bill.setUploadTag("0");
                    bill.setId(UUID.randomUUID().toString().replace("-",""));
                    billList.add(bill);
                }
                billService.save(billList);
            }
            json.setSuccess(true);
            json.setMsg("上传成功！");
        }catch (Exception e){
            json.setMsg(e.getMessage());
        }
        return json;
    }

    /** -----------------------------停车场商户相关-------------------------- */
    @RequestMapping("getParkSupplyList")
    @ResponseBody
    public JSONObject getParkSupplyList(ParkSupply parkSupply){
        JSONObject json = new JSONObject();
        json.put("parkSupplyList",rightsService.getParkSupplyList(parkSupply));
        return  json;
    }

    /**
     * 根据商户id获取停车场权益信息
     * @param clientId
     * @return
     */
    @RequestMapping("getRightsInfoForClientId")
    @ResponseBody
    public Json getRightsInfoForClientId(String clientId){
        Json json = new Json();
        try {
            json.setSuccess(true);
            Rights rights = rightsService.getRightsInfoForClientId(clientId);
            if (rights!=null){
                json.setMsg(JSONObject.toJSONString(rights));
            }
        }catch (Exception e){
            e.printStackTrace();
            json.setMsg(e.getMessage());
        }
        return json;
    }

    /**
     * 终端同步卡bin增量信息
     * type 1:增量 2:全部
     * lastUpdateTime 最近的一次同步时间
     * @param param
     * @return
     */
    @RequestMapping("getCarBinInfo")
    @ResponseBody
    public JSONObject getCarBinInfo(String param){
        JSONObject json = new JSONObject();
        try {
            json.put("cardBinList",cardBinService.getCarBinInfo(JSONObject.parseObject(param)));
            json.put("lastUpdateTime", DateFormatUtil.dateFormat("yyyy-MM-dd HH:mm:ss"));
            json.put("success",true);
        }catch (Exception e){
            e.printStackTrace();
            json.put("success",false);
            json.put("msg",e.getMessage());
        }
        return json;
    }

    /**
     * 终端同步卡级别对应权限类型
     * @return
     */
    @RequestMapping("getRightsCount")
    @ResponseBody
    public JSONObject getRightsCount(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("list",rightsCountService.getList());
        }catch (Exception e){
            e.printStackTrace();
            obj.put("list",new ArrayList<>());
        }
        return obj;
    }

    /**
     * excel导出demo
     * @param response
     * @throws Exception
     */
    @RequestMapping("exportExl")
    public void exportExl(HttpServletResponse response) throws Exception{
        List<Map> list = new ArrayList<>();
        Map map1 = new HashMap(){{
            put("1",1);
            put("2",2);
            put("3",3);
        }};
        Map map2 = map1;
        list.add(map1);
        list.add(map2);
        String[] colTitleAry = {"序号","列表1","列表2","列表3"};
        String[][] convStr = new String[list.size()][4];
        short[] colWidthAry = {80,100,150,300};
        for (int i=0;i<list.size();i++){
            Map map = list.get(i);
            convStr[i][0] = i+1+"";
            convStr[i][1] = map.get("1").toString();
            convStr[i][2] = map.get("2").toString();
            convStr[i][3] = map.get("3").toString();
        }
        ExeclUtil execlUtil = new ExeclUtil();
        execlUtil.writeExecl(colTitleAry,colWidthAry,convStr,null,response,"excel导出测试");
    }
}
