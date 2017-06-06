package com.unipay.benext.task;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.model.park.Bill;
import com.unipay.benext.model.park.Park;
import com.unipay.benext.service.basic.RightsService;
import com.unipay.benext.service.park.ParkService;
import com.unipay.benext.service.terminal.BillService;
import com.unipay.benext.service.terminal.RightsCountService;
import com.unipay.benext.utils.HttpUtil;
import com.unipay.benext.utils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/18.
 */
public class TerminalTask {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private BillService billService;
    @Autowired
    private RightsCountService rightsCountService;
    @Autowired
    private RightsService rightsService;
    @Autowired
    private ParkService parkService;

    /**
     * 定时上传流水
     */
    public void uploadBill(){
        try {
            log.warn("终端上传流水开始==========");
            Bill bill = new Bill();
            bill.setUploadTag("0");
            List<Bill> billList = billService.getBill(bill);
            if (billList!=null&&billList.size()>0){
                Map<String,String> map = new HashMap<>();
                map.put("continualInfo", JSONArray.toJSONString(billList));
                List<Map> urlList = parkService.getCloudSet();
                if (urlList!=null&&urlList.size()>0){
                    String result = HttpUtil.HttpPost(urlList.get(0).get("cloudUrl").toString()+PropertyUtils.getPropertyValue("cloud.saveBill"),map);
                    if (null!=result&&!"".equals(result)) {
                        JSONObject obj = JSONObject.parseObject(result);
                        if (obj!=null&&obj.getBoolean("success")){
                            List<Long> ids = new ArrayList<>();
                            for (Bill b:billList){
                                ids.add(Long.valueOf(b.getId()));
                            }
                            log.warn("终端修改流水状态开始==========");
                            billService.updateTagBatchO(ids);
                            log.warn("终端修改流水状态结束==========");
                        }
                    }
                }else {
                    log.warn("获取云平台接口url失败==========");
                }
            }
            log.warn("终端上传流水结束==========");
        }catch (Exception e){
            log.warn("终端上传流水错误==========",e);
        }
    }

    public void TaskRightsCount(){
//        Json json = new Json();
        try {
            log.warn("同步权益类型信息开始==========");
            List<Map> urlList = parkService.getCloudSet();
            if (urlList!=null&&urlList.size()>0){
                String result = HttpUtil.HttpPost(urlList.get(0).get("cloudUrl").toString()+PropertyUtils.getPropertyValue("cloud.rights_count"),null);
                if (null!=result&&!"".equals(result)){
                    JSONObject obj = JSONObject.parseObject(result);
                    if (obj!=null){
                        List<JSONObject> list = JSONArray.toJavaObject(obj.getJSONArray("list"),List.class);
                        if (list!=null&&list.size()>0){
                            rightsCountService.save2(list);
                        }
                    }
                }
                log.warn("同步权益类型信息结束==========");
            }else {
                log.warn("获取云平台接口url失败==========");
            }
//            json.setSuccess(true);
//            json.setMsg("同步成功！");
        }catch (Exception e){
            e.printStackTrace();
//            json.setSuccess(false);
//            json.setMsg("同步失败！");
            log.warn("同步权益类型信息失败==========",e);
        }
    }

    public void TaskClientRightsListBySupplyId(){
        //获取本币supplyId
//        Json json = new Json();
        List<Park> list  = this.parkService.getPark();
        if(list != null && list.size()>0){
            Park park = list.get(0);
            String supplyId = park.getParkId();
            try{
                this.rightsService.addClientRightsListBySupplyId(supplyId);
//                json.setMsg("同步成功！");
            }catch (Exception e){
                e.printStackTrace();
//                json.setSuccess(false);
//                json.setMsg("同步失败！");
            }
        }
    }

}
