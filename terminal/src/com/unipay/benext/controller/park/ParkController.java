package com.unipay.benext.controller.park;

import com.alibaba.fastjson.JSONObject;
import com.test.server.Hex;
import com.test.server.MyHandler;
import com.unipay.benext.framework.tool.Grid;
import com.unipay.benext.framework.tool.Json;
import com.unipay.benext.framework.tool.PageFilter;
import com.unipay.benext.framework.tool.Tree;
import com.unipay.benext.model.park.*;
import com.unipay.benext.service.basic.RightsService;
import com.unipay.benext.service.park.ParkService;
import com.unipay.benext.utils.ExeclUtil;
import com.unipay.benext.utils.HttpUtil;
import com.unipay.benext.utils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 停车场相关contro
 * Created by liuh on 2017/2/13 0013.
 */

@Controller
@RequestMapping("/park")
public class ParkController {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private ParkService parkService;

    @Autowired
    private RightsService rightsService;
    /**
     * 跳转到parkInfo
     */
    @RequestMapping("/toParkInfo")
    public ModelAndView parkInfo(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        List<Park> list= parkService.getPark();
        Map dataMap = new HashMap();

        if(list!=null&&list.size()>0){
            Park park = list.get(0);
            dataMap.put("parkId",park.getParkId());
            dataMap.put("parkName",park.getParkName());
            dataMap.put("supplyId",park.getSupplyId());
            dataMap.put("supplyName",park.getSupplyName());
            dataMap.put("charge",park.getCharge());
        }

        mv.addAllObjects(dataMap);
        mv.setViewName("park/parkInfo");
        return mv;
    }

    /**
     * 从数据中心获取 现在有的权益 商户停车场信息
     */
    @RequestMapping("/getParkInfoFromDataCenter")
    @ResponseBody
    public JSONObject getParkInfoFromDataCenter(RequestModel requestModel){
        String rs = "";
        try {
            JSONObject json = (JSONObject) JSONObject.toJSON(requestModel);
            List<Map> urlList = parkService.getCloudSet();
            if (urlList!=null&&urlList.size()>0){
                rs = HttpUtil.HttpPost(urlList.get(0).get("cloudUrl").toString()+PropertyUtils.getPropertyValue("cloud.getParkList"),json);
            }else {
                log.warn("获取云平台接口url失败==========");
            }
        } catch (Exception e) {
            log.debug("下拉停车场信息错误",e);
            e.printStackTrace();
        }
        return  JSONObject.parseObject(rs);
    }

    /**
     * 绑定停车场信息
     */
    @RequestMapping("/addOrupdateParkInfo")
    @ResponseBody
    public Json addOrupdateParkInfo(Park park){
        Json json = new Json();
        try{
            parkService.addOrupdateParkInfo(park);
            json.setSuccess(true);
            json.setMsg("绑定成功！");
        }catch (Exception e){
            log.debug("绑定停车场信息错误",e);
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("绑定失败！");
        }
        return json;
    }

    @RequestMapping("/addOrUpdateParkForCharge")
    @ResponseBody
    public Json addOrupdateCharge(Park park){
        Json json = new Json();
        try{
            parkService.addOrUpdateParkForCharge(park);
            json.setSuccess(true);
            json.setMsg("设置价格成功！");
        }catch (Exception e){
            log.debug("设置价格失败",e);
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("设置价格失败！");
        }
        return json;
    }


    /** ------------------------------------------------面板管理-------------------------------------- */

    /**
     * 跳转到payPanel
     */
    @RequestMapping("/toPayPanel")
    public ModelAndView toPayPanel(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("park/payPanel");
        return mv;
    }
    /**
     * 跳转到payPanelAdd
     */
    @RequestMapping("/toPayPanelAdd")
    public ModelAndView toPayPanelAdd(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("park/payPanelAdd");
        return mv;
    }

    /**
     * 跳转到payPanelEdit
     */
    @RequestMapping("/toPayPanelEdit")
    public ModelAndView toPayPanelEdit(String id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("park/payPanelEdit");
        mv.addObject(parkService.getPayPanelById(id));
        return mv;
    }

    @RequestMapping("/payPanelInfo")
    @ResponseBody
    public Grid payPanelInfo(HttpServletRequest request,PageFilter ph) {
        Grid grid = new Grid();
        grid.setRows(parkService.getPayPanel(ph));
        grid.setTotal((long) parkService.getPayPanelCount());
        return grid;
    }


    @RequestMapping("/payPanelAdd")
    @ResponseBody
    public Json payPanelAdd(PayPanel payPanel){
        Json json = new Json();
        try{
            parkService.addPayPanel(payPanel);
            json.setSuccess(true);
            json.setMsg("添加面板成功！");
        }catch (Exception e){
            log.debug("添加面板失败",e);
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("添加面板失败！");
        }
        return json;
    }

    @RequestMapping("/payPanelEdit")
    @ResponseBody
    public Json payPanelEdit(PayPanel payPanel){
        Json json = new Json();
        try{
            parkService.editPayPanel(payPanel);
            json.setSuccess(true);
            json.setMsg("编辑面板成功！");
        }catch (Exception e){
            log.debug("编辑面板失败",e);
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("编辑面板失败！");
        }
        return json;
    }

    @RequestMapping("/payPanelDel")
    @ResponseBody
    public Json payPanelDel(String id){
        Json json = new Json();
        try{
            int sign = parkService.delPayPanel(id);
            if(sign==-1){
                json.setSuccess(false);
                json.setMsg("删除面板失败，请先删除关联数据!");
                return json;
            }
            json.setSuccess(true);
            json.setMsg("删除面板成功！");
        }catch (Exception e){
            log.debug("删除面板失败",e);
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("删除面板失败！");
        }
        return json;
    }

    /** -----------------------------------------------出口管理------------------------------------ */

    /**
     * 跳转到exitWay
     */
    @RequestMapping("/toExitWay")
    public ModelAndView toExitWay(String id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("park/exitWay");
        return mv;
    }

    @RequestMapping("/toExitWayAdd")
    public ModelAndView toExitWayAdd(String id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("park/exitWayAdd");
        return mv;
    }

    @RequestMapping("/toExitWayEdit")
    public ModelAndView toExitWayEdit(String id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("park/exitWayEdit");
        mv.addObject(parkService.getExitWayById(id));
        return mv;
    }


    @RequestMapping("/exitWayInfo")
    @ResponseBody
    public Grid exitWayInfo(HttpServletRequest request,PageFilter ph) {
        Grid grid = new Grid();
        grid.setRows(parkService.getExitWay(ph));
        grid.setTotal((long) parkService.getExitWayCount());
        return grid;
    }

    @RequestMapping("/payPanelTree")
    @ResponseBody
    public List<Tree> payPanelTree() {
        /** 获取 面板信息*/
        PageFilter ph = new PageFilter();
        ph.setPage(1);
        ph.setRows(parkService.getPayPanelCount());
        List<PayPanel> listP = parkService.getPayPanel(ph);
        /**装载树接口*/
        List<Tree> ls = new ArrayList<Tree>();
        for (PayPanel p :listP) {
            Tree tree = new Tree();
            tree.setText(p.getPayPanelName()+"-"+p.getPayPanelCode());
            tree.setId(p.getId());
            ls.add(tree);
        }
        return ls;
    }

    @RequestMapping("/exitWayAdd")
    @ResponseBody
    public Json exitWayAdd(ExitWay exitWay){
        Json json = new Json();
        try{
            parkService.addExitWay(exitWay);
            json.setSuccess(true);
            json.setMsg("添加出口成功！");
        }catch (Exception e){
            log.debug("添加出口失败",e);
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("添加出口失败！");
        }
        return json;
    }

    @RequestMapping("/exitWayEdit")
    @ResponseBody
    public Json exitWayEdit(ExitWay exitWay){
        Json json = new Json();
        try{
            parkService.editExitWay(exitWay);
            json.setSuccess(true);
            json.setMsg("编辑出口信息成功！");
        }catch (Exception e){
            log.debug("编辑出口信息失败",e);
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("编辑出口信息失败！");
        }
        return json;
    }

    @RequestMapping("/exitWayDel")
    @ResponseBody
    public Json exitWayDel(String id){
        Json json = new Json();
        try{
            parkService.delExitWay(id);
            json.setSuccess(true);
            json.setMsg("删除出口成功！");
        }catch (Exception e){
            log.debug("删除出口失败",e);
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("删除出口失败！");
        }
        return json;
    }

    /**
     * 获取停车场支持的权益类型
     * @param supplyId
     * @return
     */
    @RequestMapping("/getClientRightsListBySupplyId")
    @ResponseBody
    public Json getClientRightsListBySupplyId(String supplyId){
        Json json = new Json();
        try{
            this.rightsService.addClientRightsListBySupplyId(supplyId);
            json.setMsg("同步成功！");
        }catch (Exception e){
            log.debug("同步停车场权益失败",e);
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("同步失败！");
        }
        return json;
    }

    /** -----------------------------------------------流水查询导出------------------------------------ */


    /**
     * 跳转到billInfo
     */
    @RequestMapping("/toBillInfo")
    public ModelAndView toBillInfo() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("park/bill");
        return mv;
    }

    /**
     * 跳转到billInfo
     */
    @RequestMapping("/billInfo")
    @ResponseBody
    public Grid billInfo(HttpServletRequest request,PageFilter ph) {
        Grid grid = new Grid();
        Map map = new HashMap();
        int start = (ph.getPage() - 1) * ph.getRows()<0?0:(ph.getPage() - 1) * ph.getRows();
        int end = ph.getRows();
        map.put("startnum", start);
        map.put("endnum", end);
        map.put("startTime",request.getParameter("startTime"));
        map.put("endTime",request.getParameter("endTime"));
        List<Bill> list = parkService.getBill(map);
        grid.setRows(list);
        grid.setTotal((long) parkService.getBillCount(map));
        return grid;
    }

    /**
     * excel导出billInfo
     * @param response
     * @throws Exception
     */
    @RequestMapping("exportBill")
    public void exportExl(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Map mapPara = new HashMap();
        mapPara.put("startTime",request.getParameter("startTime"));
        mapPara.put("endTime",request.getParameter("endTime"));
        List<Bill> listBill = parkService.getBill(mapPara);
        String[] colTitleAry = {"流水号","银行卡号","车牌号","停车场名称","出口名称","金额","停车小时数","权益类型","流水生成时间","状态"};
        String[][] convStr = new String[listBill.size()][colTitleAry.length];
        short[] colWidthAry = {150,150,150,150,150,150,150,150,150,150};
        for (int i=0;i<listBill.size();i++){
            Bill bill = listBill.get(i);
//            convStr[i][0] = bill.getId();
            convStr[i][0] = bill.getBillNo();
            convStr[i][1] = bill.getCardNo();
            convStr[i][2] = bill.getCarNo();
            convStr[i][3] = bill.getParkName();
            convStr[i][4] = bill.getExitWayName();
            convStr[i][5] = String.valueOf(bill.getTotalPrice());
            convStr[i][6] = bill.getStopTime();
            convStr[i][7] = bill.getRightsType();
            convStr[i][8] = bill.getCreateTime();
            convStr[i][9] = bill.getUploadTag();
        }
        ExeclUtil execlUtil = new ExeclUtil();
        execlUtil.writeExecl(colTitleAry,colWidthAry,convStr,null,response,"流水导出");
    }


    @RequestMapping("heartBeat")
    @ResponseBody
    public String heartBeat(){
        return "12";
    }


    /** -----------------------------------------------云端设置------------------------------------ */

    @RequestMapping("/toCloudSet")
    public ModelAndView toCloudSet(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        List<Map> list= parkService.getCloudSet();
        Map dataMap = new HashMap();
        if(list!=null&&list.size()>0){
            dataMap = list.get(0);

        }
        mv.addAllObjects(dataMap);
        mv.setViewName("park/cloudSet");
        return mv;
    }


    @RequestMapping("/addOrUpdateCloudSet")
    @ResponseBody
    public Json addOrUpdateCloudSet(String cloudUrl){
        Json json = new Json();
        try{
            HashMap map = new HashMap(1);
            map.put("cloudUrl",cloudUrl);
            parkService.addOrUpdateCloudSet(map);
            json.setMsg("设置成功！");
        }catch (Exception e){
            log.debug("云端信息设置失败",e);
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("设置失败！");
        }
        return json;
    }








    /** -----------------------------------------------测试------------------------------------ */

    @RequestMapping("/tt")
    @ResponseBody
    public Json tt()  {
        Json json = new Json();
        if (MyHandler.sessionsConcurrentHashMap.size()>0){
            System.out.println("现在连接数："+ MyHandler.sessionsConcurrentHashMap.size());
            for(String ip:MyHandler.sessionsConcurrentHashMap.keySet()){
                IoSession ioSession = MyHandler.sessionsConcurrentHashMap.get(ip);
                String ipa = (String) ioSession.getAttribute("ip");
                String  porta  = (String)ioSession.getAttribute("port");
                System.out.println("==========================请求寻卡指令====================");
                String s16 = "0A0001004C20202020202020202020202020737A657061726B323031362D30352D32302030303A30303A30302020323031362D30352D32302030313A30303A3030B9F341383838383820203030303030310B10D0";
                ioSession.write(Hex.hexStr2BinArr(s16));
                Object queue = new Object();
                ioSession.setAttribute("waitCardInfo", queue);
                try {
                synchronized (queue) {
                    queue.wait(10000l);
                }
                String s = ioSession.getAttribute("waitCardInfo_1").toString();
                    ioSession.removeAttribute("waitCardInfo");
                    ioSession.removeAttribute("waitCardInfo_1");
                json.setMsg(s);
                System.out.println("返回数据========："+s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            }
        }
        return json;
    }

}
