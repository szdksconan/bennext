package com.unipay.benext.task;


import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.model.park.Park;
import com.unipay.benext.model.park.TerminalDic;
import com.unipay.benext.service.park.ParkService;
import com.unipay.benext.service.terminal.TerminalService;
import com.unipay.benext.utils.HttpUtil;
import com.unipay.benext.utils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuh on 2017/2/18.
 */
public class HeartBeatTask {
    private Log log = LogFactory.getLog(HeartBeatTask.class);
    @Autowired
    private ParkService parkService;
    @Autowired
    private TerminalService terminalService;


    public void toCloudHttpHeart(){

        List<Map> list = parkService.getCloudSet();
        String url = "";

        if(list!=null&&list.size()>0){
            url = (String) list.get(0).get("cloudUrl");
        }else{
            log.warn("====================未设置云URL===============");
            return;
        }

        if("".equals(url)){
            log.warn("====================URL不能为空===============");
            return;
        }
        try {
            url+=PropertyUtils.getPropertyValue("clod.heartBeat");
            //获取 停车场信息 和 终端信息 调用云端的心跳更新
            TerminalDic dic = new TerminalDic();
            dic.setKey("terminalId");
            dic = terminalService.getValueForKey(dic);
            String value = dic.getValue();//获取字典里面的 终端code
            List<Park> parks = parkService.getPark();
            if(value==null||"".equals(value)){
                log.warn("====================未设置终端号===============");
                return ;
            }

            if(parks==null||parks.size()==0) {
                log.warn("====================未绑定停车场信息===============");
                return ;
            }
            Park park = parks.get(0);
            String id = park.getParkId();
            JSONObject outObj =  new JSONObject();
            outObj.put("clientId",id);
            outObj.put("terminalCode",value);
            String result = HttpUtil.HttpPost(url,outObj);
            if (null!=result&&!"".equals(result)) {
                JSONObject obj = JSONObject.parseObject(result);
                String tag = obj.getString("tag");
                if("1".equals(tag))
                    log.warn("====================连接正常===============");
                else if("2".equals(tag))
                    log.warn("====================终端故障===============");
                else if("3".equals(tag))
                    log.warn("====================未在云端注册===============");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.warn("====================连接异常==============="+url+" Exception:",e);
        }
    }




}
