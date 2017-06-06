package com.unipay.benext.controller.cloud;

import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.framework.tool.Grid;
import com.unipay.benext.framework.tool.Json;
import com.unipay.benext.framework.tool.PageFilter;
import com.unipay.benext.model.cloud.ServiceInfo;
import com.unipay.benext.service.cloud.ParkService;
import com.unipay.benext.service.cloud.ServiceInfoService;
import com.unipay.benext.utils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by LiuY on 2017/3/3 0003.
 */
@Controller
@RequestMapping("/serviceInfo")
public class ServiceInfoController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    ServiceInfoService serviceInfoService;
    @Autowired
    ParkService parkService;

    /******************************************终端管理********************/

    @RequestMapping("/toServiceInfo")
    public ModelAndView toServiceInfo(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("basic/serviceInfo");
        return mv;
    }

    @RequestMapping("/toServiceInfoAdd")
    public ModelAndView toServiceInfoAdd(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("basic/serviceInfoAdd");
        return mv;
    }

    @RequestMapping("/toServiceInfoEdit")
    public ModelAndView toServiceInfoEdit(String id){
        ModelAndView mv = new ModelAndView();
        Map map = new HashMap(1);
        map.put("id",id);
        mv.addObject(serviceInfoService.getServiceInfo(map).get(0));
        mv.setViewName("basic/serviceInfoEdit");
        return mv;
    }


    @RequestMapping("/getServiceInfo")
    @ResponseBody
    public Grid getServiceInfo(HttpServletRequest request, PageFilter ph){
        Grid grid = new Grid();
        Map map = new HashMap();
        int start = (ph.getPage() - 1) * ph.getRows()<0?0:(ph.getPage() - 1) * ph.getRows();
        int end = ph.getRows();
        map.put("startnum", start);
        map.put("endnum", end);
        map.put("name",request.getParameter("name"));
        long now  = System.currentTimeMillis();
        long  heartBeat =Long.valueOf(PropertyUtils.getPropertyValue("heartbeat"));
        heartBeat = heartBeat * 1000;
        List<ServiceInfo> list =serviceInfoService.getServiceInfo(map);
        for (int i = 0; i < list.size(); i++) {
            ServiceInfo serviceInfo = list.get(i);
            long updateTimeInterval = now;
            if(serviceInfo.getUpdateTime() != null){
                updateTimeInterval = now - serviceInfo.getUpdateTime().getTime();
                if(updateTimeInterval > heartBeat)
                    serviceInfo.setStat(2);//连接超时
                else
                    serviceInfo.setStat(1);//连接超时
            }else{
                serviceInfo.setStat(3);//未检测到连接
            }
        }
        int total = serviceInfoService.getServiceInfoCount(map);
        grid.setRows(list);
        grid.setTotal((long) total);
        return grid;
    }

    @RequestMapping("/serviceInfoAdd")
    @ResponseBody
    public Json serviceInfoAdd(ServiceInfo serviceInfo){
        Json json = new Json();
        try{
            Map map = new HashMap();
            map.put("terminalCode",serviceInfo.getTerminalCode());
            List l = serviceInfoService.getServiceInfo(map);
            if(l.size() > 0){
                json.setSuccess(false);
                json.setMsg("重复终端号绑定！");
            }
            else{
                serviceInfoService.serviceInfoAdd(serviceInfo);
                json.setSuccess(true);
                json.setMsg("添加终端成功！");
            }
        }catch (Exception e){
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("添加终端失败！");
        }
        return json;
    }

    @RequestMapping("/serviceInfoEdit")
    @ResponseBody
    public Json serviceInfoEdit(ServiceInfo serviceInfo){
        Json json = new Json();
        try{
            Map map = new HashMap();
            map.put("terminalCode",serviceInfo.getTerminalCode());
            List l = serviceInfoService.getServiceInfo(map);
            if(l.size() > 1){
                json.setSuccess(false);
                json.setMsg("重复终端号绑定！");
            }
            else{
                serviceInfoService.serviceInfoEdit(serviceInfo);
                json.setSuccess(true);
                json.setMsg("编辑终端成功！");
            }
        }catch (Exception e){
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("编辑终端失败！");
        }
        return json;
    }

    @RequestMapping("/serviceInfoDel")
    @ResponseBody
    public Json serviceInfoDel(String id){
        Json json = new Json();
        try{
            serviceInfoService.serviceInfoDel(id);
            json.setSuccess(true);
            json.setMsg("删除终端成功！");
        }catch (Exception e){
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("删除终端失败！");
        }
        return json;
    }

    /**
     * 心跳报警
     */
    @RequestMapping("/toHeartBeat")
    public ModelAndView toHeartBeat(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("basic/heartBeat");
        String heartbeat = PropertyUtils.getPropertyValue("heartbeat");
        mv.addObject("heartbeat",heartbeat);
        return mv;
    }

    @RequestMapping("/intervalCheck")
    @ResponseBody
    public Json intervalCheck(){
        Json json = new Json();
        json.setSuccess(true);
        long now  = System.currentTimeMillis();
        long  heartBeat = Long.valueOf(PropertyUtils.getPropertyValue("heartbeat")) ;
        heartBeat = heartBeat * 1000;
//        if(heartBeat > 0L){
//            return json;
//        }
        List<ServiceInfo> list =serviceInfoService.getServiceInfo(new HashMap());
        List<String> listb = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            ServiceInfo serviceInfo = list.get(i);
            long updateTimeInterval = now;
            if(serviceInfo.getUpdateTime() != null){
                updateTimeInterval = now - serviceInfo.getUpdateTime().getTime();
                if(updateTimeInterval > heartBeat)
                    listb.add(serviceInfo.getTerminalCode());
            }
        }
        if(listb.size()>0){
            json.setSuccess(false);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < listb.size(); i++) {
                sb.append(listb.get(i)).append(" ");
            }
            json.setMsg(sb.toString()+"  连接异常！");
        }

        return json;
    }

    @RequestMapping("/heartBeatEdit")
    @ResponseBody
    public Json heartBeat(String time){
        Json json = new Json();
        Properties props = new Properties();
        FileOutputStream fos = null;
        try {
            Resource resource = new ClassPathResource("serviceConfig.properties");
            props.setProperty("heartbeat",time);
            PropertyUtils.getCtxPropertiesMap().put("heartbeat",time);
            fos = new FileOutputStream(resource.getFile());
            props.store(fos, "Update heartbeat value");
            json.setSuccess(true);
            json.setMsg("设置成功！");
        } catch (IOException ex) {
            ex.printStackTrace();
            json.setSuccess(false);
            json.setMsg("设置失败！");
        } finally {
            IOUtils.closeQuietly(fos);
        }
        return json;
    }

    @RequestMapping("initBenefitUrlPage")
    public ModelAndView initBenefitUrlPage(){
        ModelAndView modelAndView = new ModelAndView("basic/benefitSet");
        modelAndView.addAllObjects(parkService.getBenefitUrl());
        return modelAndView;
    }

    @RequestMapping("saveBenefitUrl")
    @ResponseBody
    public Json saveBenefitUrl(String benefitUrl){
        Json json = new Json();
        try{
            HashMap map = new HashMap(1);
            map.put("benefitUrl",benefitUrl);
            parkService.saveBenefitUrl(map);
            json.setMsg("设置成功！");
        }catch (Exception e){
            log.warn("权益平台url设置失败",e);
            json.setSuccess(false);
            json.setMsg("设置失败！");
        }
        return json;
    }

    @RequestMapping("terminalHeartAcess")
    @ResponseBody
    public JSONObject terminalHeartAcess(String  clientId,String terminalCode){
        JSONObject json = new JSONObject();
        Map map = new HashMap();
        map.put("clientId",clientId);
        map.put("terminalCode",terminalCode);
        try{
        List l = serviceInfoService.getServiceInfo(map);
        if(l.size() > 0){
            map.put("now",new Date());
            serviceInfoService.terminalHeartAcess(map);
            json.put("tag","1");
        }
        else{
            json.put("tag","3");
        }
        }catch (Exception e){
            json.put("tag","2");
        }
        return  json;
    }

}
