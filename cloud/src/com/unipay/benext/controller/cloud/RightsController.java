package com.unipay.benext.controller.cloud;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.unipay.benext.framework.tool.Grid;
import com.unipay.benext.framework.tool.PageFilter;
import com.unipay.benext.model.basic.RequestModel;
import com.unipay.benext.model.cloud.Bill;
import com.unipay.benext.service.cloud.BillService;
import com.unipay.benext.service.cloud.ParkService;
import com.unipay.benext.service.cloud.RightsService;
import com.unipay.benext.utils.DateFormatUtil;
import com.unipay.benext.utils.ExeclUtil;
import com.unipay.benext.utils.HttpUtil;
import com.unipay.benext.utils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/15 0015.
 */


@Controller
@RequestMapping("/rights")
public class RightsController {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    RightsService rightsService;
    @Autowired
    BillService billService;
    @Autowired
    ParkService parkService;

    //下拉卡片对应权益信息
    @RequestMapping(value = "getRightsInfoList")
    @ResponseBody
    public JSONObject getRightsInfoList(String param){
            JSONObject json = new JSONObject();
            json.put("rights",this.rightsService.getRightsInfoList(JSONObject.parseObject(param)));
            json.put("lastUpdateTime", DateFormatUtil.dateFormat("yyyy-MM-dd HH:mm:ss"));
            json.put("tag",true);
            return  json;
    }

    //商户-对应权益信息接口
    @RequestMapping(value = "getClientRightsListByFilter")
    @ResponseBody
    public List getClientRightsListByFilter( String  supplyId){
       List list = new ArrayList();
        try{
            list =  this.rightsService.getClientRightsListByFilter(supplyId);
        }
        catch (Exception e){
          e.printStackTrace();
        }
        return  list;
    }

    //获取停车场信息接口
    @RequestMapping(value = "getParkListByFilter")
    @ResponseBody
    public JSONObject getParkListByFilter(RequestModel requestModel){
        JSONObject json = new JSONObject();
        try{
            requestModel.setOrderSql(requestModel.getOrderString());
            json.put("rows", this.rightsService.getParkListByFilter(requestModel));
            json.put("total", this.rightsService.getParkCountListByFilter(requestModel));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }

    //绑定停车场
    @RequestMapping(value = "updatePark",method = RequestMethod.POST)
    @ResponseBody
    public String updatePark(RequestModel requestModel){
        try{
            this.rightsService.updatePark(requestModel);
            return "1";
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "0";
    }

    /** -----------------------------------------------流水查询导出------------------------------------ */


    /**
     * 跳转到billInfo
     */
    @RequestMapping("/toBillInfo")
    public ModelAndView toBillInfo() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("basic/bill");
        return mv;
    }

    /**
     * 跳转到billInfo
     */
    @RequestMapping("/billInfo")
    @ResponseBody
    public Grid billInfo(HttpServletRequest request, PageFilter ph) {
        Grid grid = new Grid();
        Map map = new HashMap();
        int start = (ph.getPage() - 1) * ph.getRows()<0?0:(ph.getPage() - 1) * ph.getRows();
        int end = ph.getRows();
        map.put("startnum", start);
        map.put("endnum", end);
        map.put("startTime",request.getParameter("startTime"));
        map.put("endTime",request.getParameter("endTime"));
        List<Bill> list = billService.getBill(map);
        grid.setRows(list);
        grid.setTotal((long) billService.getBillCount(map));
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
        List<Bill> listBill = billService.getBill(mapPara);
        String[] colTitleAry = {"流水号","银行卡号","车牌号","停车场名称","出口名称","金额","停车小时数","权益类型","流水生成时间","状态"};
        String[][] convStr = new String[listBill.size()][colTitleAry.length];
        short[] colWidthAry = {150,150,150,150,150,150,150,150,150,150,150};
        for (int i=0;i<listBill.size();i++){
            Bill bill = listBill.get(i);
//            convStr[i][0] = bill.getId()+"";
            convStr[i][0] = bill.getBillNo();
            convStr[i][1] = bill.getCardNo();
            convStr[i][2] = bill.getCarNo();
            convStr[i][3] = bill.getParkName();
            convStr[i][4] = bill.getExitWayName();
            convStr[i][5] = bill.getTotalPrice();
            convStr[i][6] = bill.getStopTime();
            convStr[i][7] = bill.getRightsType();
            convStr[i][8] = bill.getCreateTime();
            convStr[i][9] = bill.getUploadTag();
        }
        ExeclUtil execlUtil = new ExeclUtil();
        execlUtil.writeExecl(colTitleAry,colWidthAry,convStr,null,response,"流水导出");
    }

    @RequestMapping("takeCardRightsInfo")
    public void takeCardRightsInfo(){
        try {
            Map url = parkService.getBenefitUrl();
            if (url!=null){
                String result = HttpUtil.HttpPost(PropertyUtils.getPropertyValue("benefit.cardright"),null);
                if (null!=result&&!"".equals(result)) {
                    JSONObject obj = JSONObject.parseObject(result);
                    if (obj!=null&&obj.getBoolean("isSuccess")){
                        List<JSONObject> list = JSONArray.toJavaObject(obj.getJSONArray("itemList"),List.class);
                        if (list!=null&&list.size()>0){
//                        list = list.subList(0,1000);
                            rightsService.saveAllRights(list);
                        }
                    }
                }
            }else {
                log.warn("获取权益平台url失败========");
            }
        }catch (Exception e){
            log.error("数据中心获取权益平台cardRight出错",e);
        }
    }

}
