package com.unipay.benext.controller.park;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.framework.tool.Json;
import com.unipay.benext.model.entity.CardBin;
import com.unipay.benext.model.entity.RightsCount;
import com.unipay.benext.service.park.ParkService;
import com.unipay.benext.service.terminal.CardBinService;
import com.unipay.benext.service.terminal.RightsCountService;
import com.unipay.benext.utils.HttpUtil;
import com.unipay.benext.utils.JsonOperate;
import com.unipay.benext.utils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 终端调用数据中心
 * Created by Administrator on 2017/2/21.
 */
@Controller
@RequestMapping("terminalTakeCloud")
public class TerminalTakeCloudController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private RightsCountService rightsCountService;
    @Autowired
    CardBinService cardBinService;
    @Autowired
    private ParkService parkService;

    /**
     * 获取数据中心-卡级别对应权益类型 并同步到终端
     * @return
     */
    @RequestMapping("takeRightsCount")
    @ResponseBody
    public Json takeRightsCount(){
        Json json = new Json();
        try {
            log.warn("同步数据中心-卡级别对应权益类型开始========");
            List<Map> urlList = parkService.getCloudSet();
            if (urlList!=null&&urlList.size()>0){
                String result = HttpUtil.HttpPost(urlList.get(0).get("cloudUrl").toString()+PropertyUtils.getPropertyValue("cloud.rights_count"),null);
                if (null!=result&&!"".equals(result)){
                    JSONObject obj = JSONObject.parseObject(result);
                    if (obj!=null){
                        List<RightsCount> list = JSONArray.toJavaObject(obj.getJSONArray("list"),List.class);
                        if (list!=null&&list.size()>0){
                            log.warn("终端保存卡级别对应权益类型开始========");
                            rightsCountService.save(list);
                            log.warn("终端保存卡级别对应权益类型结束========");
                        }
                    }
                }
            }else {
                log.warn("获取云平台接口url失败==========");
            }
            log.warn("同步数据中心-卡级别对应权益类型结束========");
            json.setSuccess(true);
            json.setMsg("同步成功！");
        }catch (Exception e){
            log.warn("同步数据中心-卡级别对应权益类型失败========",e);
            json.setSuccess(false);
            json.setMsg("同步失败！");
        }
        return json;
    }

    /**
     * 调用数据中心更新终端卡bin信息
     * @return
     * @throws Exception
     */
    @RequestMapping("synchCardBinInfo")
    @ResponseBody
    public Json synchCardBinInfo() throws Exception {
        Json json = new Json();
        try {
            log.warn("终端更新卡bin信息开始========");
            final String jsonFile = this.getClass().getResource("/").getPath()+"config.json";
            log.warn("读取路径是:        "+ this.getClass().getResource("/").getPath()+"config.json");
            Map param = new HashMap<>();
            JSONObject obj = JsonOperate.readJson(jsonFile);
            log.warn("最后更新时间为:      "+obj.getString("lastUpdateTime"));
            if (obj!=null){
                String lastUpdateTime = obj.getString("lastUpdateTime");//终端最近一次同步时间
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
                    String result = HttpUtil.HttpPost(urlList.get(0).get("cloudUrl").toString()+PropertyUtils.getPropertyValue("cloud.card_bin"),param);
                    if (null!=result&&!"".equals(result)){
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        if (jsonObject.getBoolean("success")){
                            List<CardBin> cardBinList = (List<CardBin>) jsonObject.get("cardBinList");
                            lastUpdateTime = jsonObject.get("lastUpdateTime").toString();
                            try {
                                if (cardBinList!=null&&cardBinList.size()>0){
                                    log.warn("终端保存卡bin信息开始========");
                                    cardBinService.saveCardBin(cardBinList);
                                    log.warn("终端保存卡bin信息结束========");
                                }
                                obj.put("lastUpdateTime",lastUpdateTime);
                                JsonOperate.writeFile(jsonFile,obj.toJSONString());
                            }catch (Exception e){
                                e.printStackTrace();
                                log.warn("终端保存卡bin信息异常========",e);
                            }
                        }
                    }
                }else {
                    log.warn("获取云平台接口url失败==========");
                }
                log.warn("终端更新卡bin信息结束========");
                json.setSuccess(true);
                json.setMsg("同步成功！");
            }
        }catch (Exception e){
            log.warn("终端更新卡bin信息失败========",e);
            json.setSuccess(false);
            json.setMsg("同步失败！");
        }
        return json;
    }
}
