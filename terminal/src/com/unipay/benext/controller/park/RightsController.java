package com.unipay.benext.controller.park;

import com.alibaba.fastjson.JSONObject;
import com.test.server.Hex;
import com.test.server.MinaServer;
import com.test.server.MyHandler;
import com.test.server.ServerLock;
import com.unipay.benext.model.basic.*;
import com.unipay.benext.model.entity.CardBin;
import com.unipay.benext.model.park.Park;
import com.unipay.benext.model.park.PayPanel;
import com.unipay.benext.service.basic.RightsService;
import com.unipay.benext.service.park.ParkService;
import com.unipay.benext.service.terminal.CardBinService;
import com.unipay.benext.utils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by WEI on 2017/2/21.
 */
@Controller
@RequestMapping("rights")
public class RightsController {
    @Autowired
    RightsService rightsService;
    @Autowired
    CardBinService cardBinService;

    @Autowired
    ParkService parkService;
    private Logger logger = Logger.getLogger("MINA");


    //得到符合的停车权益IDS
    List getOptimalRightsId(String cardBin) {
        //根据卡号获取卡级别
        // List<CardBin> list = this.cardBinService.getLoclCardBinList();
        CardBin cardBinModel = this.cardBinService.getLoclCardBinByCardNo(cardBin);
        int cardRank = 999;
        if (cardBinModel != null)
            cardRank = cardBinModel.getCardRank();
//        for (CardBin cardBinIn : list) {
//            int start = cardBinIn.getCardStart();
//            int length = cardBinIn.getRangLength();
//            String cardBinNo = cardBinIn.getCardBin();
//            if (cardBinNo.equals(cardBin.substring(start - 1, length))) {
//                cardRank = cardBinIn.getCardRank();
//                break;
//            }
//        }
        if (cardRank != 999) {
            //检查卡级别对应的卡权益
            List<RightsCount> rightsCountList = this.rightsService.getRightsCountByRank(String.valueOf(cardRank));
            if (rightsCountList != null && rightsCountList.size() > 0) {
                List<ClientRightsModel> outList = this.rightsService.getLocalClientRightsListByRightsIdsD(rightsCountList);
                if (outList != null && outList.size() > 0) {
                    return outList;
                }
            }
        }
        return new ArrayList();
    }

    //接收第三方停车场扣费信息
    @RequestMapping("getTempCarChargeInfo")
    @ResponseBody
    public ResponseModel getTempCarChargeInfo(RequestModel requestModel) {
        ServerLock serverLock = new ServerLock();

        ResponseModel responseModel = new ResponseModel();
        Data data = new Data();
        data.setOutId(requestModel.getOutId());
        Double effectHours = requestModel.getParkTime();
        double cash = requestModel.getFee();
        String carNo = requestModel.getCarPlate();

        String pkId = requestModel.getDeal_ID();
        double dkcash = requestModel.getFee();
        try{
            logger.warn("原始车牌号："+carNo);
//            carNo = new String(carNo.getBytes("ISO-8859-1"),"utf-8");
            carNo = new String(carNo.getBytes("utf-8"),"utf-8");
            logger.warn("解析车牌号："+carNo);
            System.out.println("车牌号=======>"+carNo);
            Park park = this.parkService.getPark().get(0);
            int clientId = Integer.valueOf(park.getSupplyId());
            int parkId = Integer.valueOf(park.getParkId());
            double charge = park.getCharge();
            Date exitTime = new Date();
            //获得面板ID
            Map map = this.parkService.getExitWayById(String.valueOf(requestModel.getOutId()));
            String payPanelId = String.valueOf(map.get("payPanelId"));
            PayPanel payPanel = this.parkService.getPayPanelById(payPanelId);
            String ip = payPanel.getIp();
            String parkName = park.getParkName();
            String exitWayName = map.get("exitWayName").toString();
            String payPanelName = payPanel.getPayPanelName();
            int version  = serverLock.add(ip);
            //寻卡
            String cardBin = getCardNum(ip,requestModel.getInTime(),requestModel.getOutTime(),carNo,version,serverLock);
            cardBin = getTrueCardBin(cardBin);
            System.out.println("=============>卡号"+cardBin);
            if(cardBin.equals("")){
                responseModel.setCode(2);
                data.setCarPlate(carNo);
                data.setDeal_ID(pkId);
                data.setFee(cash);
                responseModel.setData(data);
                responseModel.setInfo("寻卡失败");
                return responseModel;
            }
            //默认使用2小时权益
            int effectType = 1;
            if (effectHours >= 15)
                effectType = 2;
            //判断是否有权益可以使用
            List<ClientRightsModel> list = getOptimalRightsId(cardBin);
            boolean useTag = this.rightsService.checkUseRights(cardBin);
            List<ClientRightsModel> compareList = new ArrayList<ClientRightsModel>();//全匹配的list
            List<ClientRightsModel> unCompareList = new ArrayList<ClientRightsModel>();//不能匹配的list
            Map<String, String> resultmap = new HashMap<String, String>();
            boolean useRights = false;
            if (list != null && list.size() > 0 && useTag) {
                for (ClientRightsModel clientRightsModel : list) {
                    if (effectType == clientRightsModel.getEffectType()) {
                        compareList.add(clientRightsModel);
                    } else {
                        unCompareList.add(clientRightsModel);
                    }

                }
                if (compareList != null && compareList.size() > 0) {
                    compareList = this.rightsService.getLocalClientRightsListSortByRightsIds(compareList, "order by payRule");
                    for (ClientRightsModel clientRightsModel : compareList) {
                        resultmap = useRights(clientRightsModel,cardBin);
                        if (resultmap.get("tag").equals("1")){
                            //请求面板缴费
                            String tag = "";
                            Map<String, String> outmap = new HashMap();
                            if("0.00".equals(resultmap.get("cash"))){
                                tag = "1";
                            }
                            else{
                                outmap = pay(carNo,requestModel.getInTime(),requestModel.getOutTime(),ip, Double.valueOf(resultmap.get("cash")),version,serverLock);
                                tag =   outmap.get("tag");
                            }
                            if ("1".equals(tag)){
                                addBill(pkId, cardBin, carNo, clientId, parkId, exitTime,effectHours, resultmap.get("rightsId"), resultmap.get("cash"),parkName,exitWayName,payPanelName);
                                //计算剩余金额
                                int dkhours = Integer.valueOf(resultmap.get("deductionHours").toString()) ;
//                            cash = cash - (dkhours*charge);
                                cash = ( cash*100 - (dkhours*charge*100))/100;
                                useRights = true;
                            }
                            else{
                                responseModel.setCode(2);
                                data.setCarPlate(carNo);
                                data.setDeal_ID(pkId);
                                data.setFee(cash);
                                responseModel.setInfo(outmap.get("info").toString());
                            }
                            break;
                        }

                    }
                    if (resultmap.get("tag").equals("0") && unCompareList != null && unCompareList.size() > 0) {
                        unCompareList = this.rightsService.getLocalClientRightsListSortByRightsIds(unCompareList, "order by payRule");
                        for (ClientRightsModel clientRightsModel : unCompareList) {
                            resultmap = useRights(clientRightsModel, cardBin);
                            if (resultmap.get("tag").equals("1")){
                                //请求面板缴费
                                String tag = "";
                                Map<String, String> outmap = new HashMap();
                                if("0.00".equals(resultmap.get("cash"))){
                                    tag = "1";
                                }
                                else{
                                    outmap = pay(carNo,requestModel.getInTime(),requestModel.getOutTime(),ip, Double.valueOf(resultmap.get("cash")),version,serverLock);
                                    tag =   outmap.get("tag");
                                }
                                if ("1".equals(tag)) {
                                    addBill(pkId, cardBin, carNo, clientId, parkId, exitTime,effectHours, resultmap.get("rightsId"), resultmap.get("cash"),parkName,exitWayName,payPanelName);
                                    //计算剩余金额
                                    int dkhours = Integer.valueOf(resultmap.get("deductionHours").toString());
                                    cash = ( cash*100 - (dkhours*charge*100))/100;
                                    useRights = true;
                                }
                                else{
                                    responseModel.setCode(2);
                                    data.setCarPlate(carNo);
                                    data.setDeal_ID(pkId);
                                    data.setFee(cash);
                                    responseModel.setInfo(outmap.get("info").toString());
                                }
                                break;
                            }

                        }
                    }
                } else {
                    if (unCompareList != null && unCompareList.size() > 0) {
                        unCompareList = this.rightsService.getLocalClientRightsListSortByRightsIds(unCompareList, "order by payRule desc");
                        for (ClientRightsModel clientRightsModel : unCompareList) {
                            resultmap = useRights(clientRightsModel, cardBin);
                            if (resultmap.get("tag").equals("1")) {
                                //请求面板缴费
                                String tag = "";
                                Map<String, String> outmap = new HashMap();
                                if("0.00".equals(resultmap.get("cash"))){
                                    tag = "1";
                                }
                                else{
                                    outmap = pay(carNo,requestModel.getInTime(),requestModel.getOutTime(),ip, Double.valueOf(resultmap.get("cash")),version,serverLock);
                                    tag =   outmap.get("tag");
                                }
                                if ("1".equals(tag)) {
                                    addBill(pkId, cardBin, carNo, clientId, parkId, exitTime,effectHours, resultmap.get("rightsId"), resultmap.get("cash"),parkName,exitWayName,payPanelName);
                                    //计算剩余金额
                                    int dkhours = Integer.valueOf(resultmap.get("deductionHours").toString());
                                    cash = ( cash*100 - (dkhours*charge*100))/100;
                                    useRights = true;
                                }
                                else{
                                    responseModel.setCode(2);
                                    data.setCarPlate(carNo);
                                    data.setDeal_ID(pkId);
                                    data.setFee(cash);
                                    responseModel.setInfo(outmap.get("info").toString());
                                }
                                break;
                            }
                        }
                    }
                }
                //是否还需要交费
                if(cash > 0){
                    Map<String, String> outmap = pay(carNo,requestModel.getInTime(),requestModel.getOutTime(),ip, cash,version,serverLock);
                    String tag = outmap.get("tag");
                    if ("1".equals(tag)){
                        responseModel.setCode(0);
                        data.setCarPlate(carNo);
                        data.setDeal_ID(pkId);
                        data.setFee(0);
                        responseModel.setInfo("缴费成功");
                    }
//                    addBill(pkId, cardBin, carNo, clientId, parkId, exitTime,Integer.valueOf(map.get("deductionHours").toString()), resultmap.get("rightsId"), resultmap.get("cash"));
                    else{
                        if(useRights)
                            responseModel.setCode(1);
                        else
                            responseModel.setCode(1);
                        data.setCarPlate(carNo);
                        data.setDeal_ID(pkId);
                        data.setFee(cash);
                        responseModel.setInfo(outmap.get("info").toString());
                    }
                }
                else{
                    responseModel.setCode(0);
                    data.setCarPlate(carNo);
                    data.setDeal_ID(pkId);
                    data.setFee(0);
                    responseModel.setInfo("缴费成功");
                }
                //没有可用权益走正常收费流程
//            if (resultmap.get("tag").equals("0")) {
//                Map<String, String> outmap = pay(carNo, requestModel.getInTime() + " " + requestModel.getOutTime(), payPanelId, cash);
//                String tag = outmap.get("tag");
//                if (tag.equals("1"))
//                    responseModel.setCode(0);
//                else
//                    responseModel.setCode(2);
//                responseModel.setCarNo(carNo);
//                responseModel.setDeductionHours(effectHours);
//                responseModel.setPkId(pkId);
//                responseModel.setPrice(cash);
//                responseModel.setRightsId("");
//                responseModel.setFailReason(outmap.get("info"));
//
//            } else {
//                String billNo = requestModel.getPkId();
//                Park park = this.parkService.getPark().get(0);
//                int clientId = Integer.valueOf(park.getSupplyId());
//                int parkId = Integer.valueOf(park.getParkId());
//                Date exitTime = new Date();
//                //组装返回信息
//                responseModel.setCode(1);
//                responseModel.setPrice(Double.valueOf(resultmap.get("cash")));
//                responseModel.setCarNo(carNo);
//                responseModel.setDeductionHours(Integer.valueOf(resultmap.get("deductionHours")));
//                responseModel.setPkId(pkId);
//                responseModel.setRightsId(resultmap.get("rightsId"));
//                responseModel.setFailReason("");
//                if (Double.valueOf(resultmap.get("cash")) > 0.00) {
//
//                    String tag = outmap.get("tag");
//                    if (!"1".equals(tag)) {
//                        responseModel.setCode(2);
//                        responseModel.setFailReason(outmap.get("info"));
//                        responseModel.setPrice(Double.valueOf(resultmap.get("cash")));
//                        return responseModel;
//                    }
//                }
//                addBill(billNo, cardBin, carNo, clientId, parkId, exitTime, effectHours, resultmap.get("rightsId"), resultmap.get("cash"));
//
//            }
            } else {//正常收费流程
                Map<String, String> outmap = pay(carNo,requestModel.getInTime(),requestModel.getOutTime(),ip, cash,version,serverLock);
                String tag = outmap.get("tag");
                System.out.println("缴费标识============>"+tag);
                if (tag.equals("1")){
                    responseModel.setCode(0);
                    data.setFee(0);
                    responseModel.setInfo("缴费成功");
                }
                else{
                    responseModel.setCode(2);
                    data.setFee(cash);
                    responseModel.setInfo(outmap.get("info"));
                }
                data.setCarPlate(carNo);
                data.setDeal_ID(pkId);

            }
        }catch (Exception e){
            logger.warn("停车收费请求异常",e);
            e.printStackTrace();
            responseModel.setCode(2);
            data.setFee(cash);
            responseModel.setInfo(e.toString());
            data.setCarPlate(carNo);
            data.setDeal_ID(pkId);
        }
        responseModel.setData(data);
        return responseModel;
    }

    //useRights
    Map useRights(ClientRightsModel clientRightsModel, String cardBin) {
        Rights rights = this.rightsService.getRightsByFilter(clientRightsModel.getRightsType(), cardBin);
        Map<String, String> map = new HashMap<String, String>();
        map.put("tag", "0");
        map.put("rightsId", clientRightsModel.getRightsType());
        map.put("cardBin", String.valueOf(cardBin));
        map.put("deductionHours", String.valueOf(clientRightsModel.getDeductionHours()));
        map.put("cash","0.00");
        //存在
        if (rights != null) {
            //判断是否免费
            if (rights.getRightsCount() > 0) {
                if (clientRightsModel.getPayRule() == 3) {
                    map.put("cash", "0.00");
                    map.put("tag", "1");
                    return map;
                } else if (clientRightsModel.getPayRule() == 1 && rights.getRightsCount() < rights.getRightsMaxCount()) {
                    map.put("cash", "0.00");
                    map.put("tag", "1");
                    return map;
                } else {
                    map.put("cash", String.valueOf(clientRightsModel.getPrice()));
                    map.put("tag", "1");
                }
            }

//            if(rights.getPayRule().equals("2")){
//                //生成权益使用明细
//                if(rights.getRightsCount() > 0){
//                    map.put("cash","0");
//                    map.put("tag","1");
//                    return map;
//                }
//            }
//            else{
//                //生成权益使用明细
//                if(rights.getRightsCount() > 0){
//                    map.put("tag","1");
//                    //是否首次扣费
//                    if(rights.getRightsCount() == rights.getRightsMaxCount()){
//                        map.put("cash","1");
//
//                    }
//                    else{
//                        map.put("cash","0");
//                    }
//                }
//            }
        } else {
            map.put("tag", "1");
            map.put("cash", String.valueOf(clientRightsModel.getPrice()));
        }

        return map;
    }

    public Map<String, String> pay(String carNo, String inTime,String outTime, String ip, double cash,int version,ServerLock serverLock) {
        Map<String,String> map = new HashMap<String,String>();
        String tag = "";
        String info = "";
        IoSession ioSession = MyHandler.sessionsConcurrentHashMap.get(ip);
        if(ioSession  != null){
            try{
                //组装扣费指令
                ByteBuffer buffer = ByteBuffer.allocate(93);
                buffer.put((byte)0x0A);
                buffer.put((byte)0x01);
                buffer.put((byte)0x01);
                buffer.put((byte)0x00);
                buffer.put((byte)0x55);
//            System.out.println(buffer.position());
                String lius = new SimpleDateFormat("HHmmssSSS").format(new Date());
//                String lius = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                buffer.put(lius.getBytes(MinaServer.charSet));
//            System.out.println(buffer.position());
                buffer.put(Hex.hexFormat("szepark",20,1).getBytes(MinaServer.charSet));//应用终端ID
                buffer.put((inTime+"  "+outTime).getBytes(MinaServer.charSet));//出入时间
//            System.out.println(buffer.position());
//                carNo = unicode2String(carNo);
                buffer.put(Hex.hexFormat(carNo,10,2).getBytes(MinaServer.charSet));//车牌号
//            System.out.println(buffer.position());
                String yje = "000000";
                String je =  cash*100+"" ;
                je = je.substring(0,je.indexOf("."));
                int len = je.length();
                int celen = 6-len;
                if(celen > 0){
                    yje = yje.substring(0,celen);
                    yje = yje+je;
                }
                else{
                    yje = je;
                }

                buffer.put(yje.getBytes(MinaServer.charSet));//金额 已分为单位
               // System.out.println(buffer.position());
                logger.warn("========>流水号"+lius+"=========>金额hex码"+Hex.bin2HexStr(yje.getBytes(MinaServer.charSet)));
                buffer.rewind();
                //计算校验和
                int checkSum = 0;
                for(int i=0;i<93-3;i++){//减去头尾
                    if(i==0) {
                        buffer.get();
                        continue;
                    }
                    checkSum += buffer.get()&0xff;//转无符号int
                }
                buffer.put((byte)0x0b);//协议尾
                buffer.putShort((short) checkSum);
                String hex = Hex.bin2HexStr(buffer.array());
                logger.warn("=============>IP:"+ip+"收费指令发送"+hex);
                ioSession.write(buffer.array());
                int i = version;
                ConcurrentHashMap<String, Object> hashmap = new ConcurrentHashMap<String, Object>();
                if(ioSession.getAttribute("lock") != null){
                    hashmap = (ConcurrentHashMap<String, Object>) ioSession.getAttribute("lock");
                }
                hashmap.put("key"+i,serverLock);
                ioSession.setAttribute("lock",hashmap);
                synchronized (serverLock) {
                    serverLock.wait();
                }
                if(i<ServerLock.getVersion(ip)){
                    tag = "0";
                    info = "过期";
                    hashmap.remove("key"+i);
                }
                else{
                    String  stat = ioSession.getAttribute("stat").toString();
                    ioSession.removeAttribute("stat");
                    if(stat.equals("0000")){
                        Object obj = new Object();
                        ConcurrentHashMap<String, Object> reMap = new ConcurrentHashMap<String, Object>();
                        if(ioSession.getAttribute("rlock") != null){
                            reMap = (ConcurrentHashMap<String, Object>) ioSession.getAttribute("rlock");
                        }
                        reMap.put(lius,obj);
                        ioSession.setAttribute("rlock",reMap);
                        synchronized (obj){
                            obj.wait(13000);
                        }
                        if(ioSession.getAttribute(lius) != null){
                            Map<String,Object> liumap  = (HashMap<String,Object>)ioSession.getAttribute(lius);
                            ioSession.removeAttribute(lius);
                            String dec = liumap.get("dec").toString();
                            if(dec.equals("0000")){
                                String  money_1Str = ioSession.getAttribute("money_1Str").toString();
                                System.out.println("========>应收金额"+money_1Str);
                                ioSession.removeAttribute("money_1Str");
                                if(money_1Str.equals(yje))
                                {
                                    tag = "1";
                                    logger.warn("收费请求完成，面板IP:"+ip);
                                    LiushuiReturn(lius,ip);
                                    map.put("lius",lius);
                                }
                                else{
                                    tag = "0";
                                    LiushuiReturn(lius,ip);
                                }
                            }
                            else{
                                tag = "0";
                                LiushuiReturn(lius,ip);
                            }

                        }
                        else {
                            tag = "0";
                        }
//                        synchronized (serverLock) {
//                            serverLock.wait();
//                        }
//                        if(i<ServerLock.i){
//                            tag = "0";
//                            info = "过期";
//                            hashmap.remove("key"+i);
//                        }
//                        else{
//                            String  dec = ioSession.getAttribute("dec").toString();
//                            ioSession.removeAttribute("dec");
//                            if(dec.equals("0000"))
//                                tag = "1";
//                            else
//                                tag = "0";
//                        }
                    }
                    else
                        tag = "0";
                    hashmap.remove("key"+i);
                }
            }catch (Exception e){
                logger.warn("收费指令异常，面板IP:"+ip,e);
                e.printStackTrace();
            }
        }
        else{
            tag = "0";
            info = "没有找到匹配硬件";
        }

        //读取读卡器返回，成功返回1，失败返回0
        map.put("tag", tag);
        map.put("info", info);
        return map;
    }

    public void addBill(String billNo, String cardNo, String carNo, int clientId, int parkId, Date exitTime, Double stopTime, String rightsType, String cash,String parkName,String exitWayName,String payPanelName) {
        Bill bill = new Bill();
        bill.setBillNo(billNo);
        bill.setCardNo(cardNo);
        bill.setCarNo(carNo);
        bill.setClientId(clientId);
        bill.setCreateTime(new Date());
        bill.setParkId(clientId);
        bill.setExitTime(exitTime);
        bill.setStopTime(stopTime);
        bill.setRightsType(rightsType);
        bill.setUploadTag("0");
        bill.setTotalPrice(cash);
        bill.setParkName(parkName);
        bill.setExitWayName(exitWayName);
        bill.setPayPanelName(payPanelName);
        bill.setTerminalId(Integer.valueOf(PropertyUtils.getPropertyValue("TERMINAL_ID")));
        this.rightsService.addBill(bill);
    }

    //取消交易
    @RequestMapping("breakdealInfo")
    @ResponseBody
    public JSONObject breakdealInfo(RequestModel requestModel) {
        JSONObject obj = new JSONObject();
        Map dataMap = new HashMap();
        if (requestModel.getBreakDeal().equals("0")) {
            String carNo = "";
            String outIp = "";
            try {
                Map parkMap = this.parkService.getExitWayById(String.valueOf(requestModel.getOutId()));
                dataMap.put("outId",requestModel.getOutId());
                carNo = requestModel.getCarPlate();
                carNo = new String(carNo.getBytes("ISO-8859-1"),"utf-8");
                System.out.println("车牌号=======>"+carNo);
                String payPanelId = String.valueOf(parkMap.get("payPanelId"));
                PayPanel payPanel = this.parkService.getPayPanelById(payPanelId);
                String ip = payPanel.getIp();
                outIp = ip;
                IoSession ioSession = MyHandler.sessionsConcurrentHashMap.get(ip);
                if(ioSession != null){
                    ByteBuffer buffer = ByteBuffer.allocate(28);
                    buffer.put((byte)0x0A);
                    buffer.put((byte)0x01);
                    buffer.put((byte)0x04);
                    buffer.put((byte)0x00);
                    buffer.put((byte)0x14);
                    buffer.put(Hex.hexFormat("szepark",20,1).getBytes(MinaServer.charSet));//应用终端ID
                    buffer.rewind();
                    //计算校验和
                    int checkSum = 0;
                    for(int i=0;i<28-3;i++){//减去头尾
                        if(i==0) {
                            buffer.get();
                            continue;
                        }
                        checkSum += buffer.get()&0xff;//转无符号int
                    }
                    buffer.put((byte)0x0b);//协议尾
                    buffer.putShort((short) checkSum);
//                    String hex = Hex.bin2HexStr(buffer.array());
                    logger.warn("=============>IP:"+ip+"取消扣费指令发送"+Hex.bin2HexStr(buffer.array()));
                    Object queue = new Object();
                    ioSession.setAttribute("cancel",queue);
                    ioSession.write(buffer.array());
                    synchronized (queue) {
                        queue.wait(6000);
                    }
                    System.out.println(ioSession.getAttribute("liushuiCancel"));
                    String liushui = ioSession.getAttribute("liushuiCancel").toString();
                    ioSession.removeAttribute("liushuiCancel");
                    if(!"".equals(liushui)){
                        obj.put("code",0);
                        dataMap.put("carPlate",carNo);
                        obj.put("info","取消成功");
                    }
                    else{
                        obj.put("code",1);
                        dataMap.put("carPlate",carNo);
                        obj.put("info","取消失败");
                    }
                }
                else{
                    obj.put("code",1);
                    dataMap.put("carPlate",carNo);
                    obj.put("info","取消失败");
                }
                logger.warn("=============>取消扣费请求完成，面板IP:"+ip);
            } catch (Exception e) {
                logger.warn("取消收费请求异常，面板IP:"+outIp,e);
                e.printStackTrace();
                obj.put("code",1);
                dataMap.put("carPlate",carNo);
                obj.put("info","取消失败");
                System.out.println("=============>扣费请求报错");
            }

        }
        obj.put("data",dataMap);
        return obj ;
    }
    //寻卡指令、
    public String getCardNum(String ip,String inTime,String outTime,String carNo,int version,ServerLock serverLock){
        String cardBin = "";
        IoSession ioSession = MyHandler.sessionsConcurrentHashMap.get(ip);
        if(ioSession  != null){
            try{
                //组装寻卡指令
                ByteBuffer buffer = ByteBuffer.allocate(84);
                buffer.put((byte)0x0A);
                buffer.put((byte)0x00);
                buffer.put((byte)0x01);
                buffer.put((byte)0x00);
                buffer.put((byte)0x4C);
//            System.out.println(buffer.position());
                buffer.put(Hex.hexFormat("szepark",20,1).getBytes(MinaServer.charSet));
//            System.out.println(buffer.position());
                buffer.put((inTime+"  "+outTime).getBytes(MinaServer.charSet));
//            System.out.println(buffer.position());
                buffer.put(Hex.hexFormat(carNo,10,2).getBytes(MinaServer.charSet));
//            System.out.println(buffer.position());
                buffer.put("000001".getBytes(MinaServer.charSet));
//            System.out.println(buffer.position());
                buffer.rewind();
                //计算校验和
                int checkSum = 0;
                for(int i=0;i<84-3;i++){//减去头尾
                    if(i==0) {
                        buffer.get();
                        continue;
                    }
                    checkSum += buffer.get()&0xff;//转无符号int
                }
                buffer.put((byte)0x0b);
                buffer.putShort((short) checkSum);
//            String hex = Hex.bin2HexStr(buffer.array());

                int i = version;
                ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
                if(ioSession.getAttribute("lock") != null){
                    map = (ConcurrentHashMap<String, Object>) ioSession.getAttribute("lock");
                }
                map.put("key"+i,serverLock);
                ioSession.setAttribute("lock",map);
                logger.warn("=============>IP"+ip+"寻卡指令"+Hex.bin2HexStr(buffer.array()));
                ioSession.write(buffer.array());
                synchronized (serverLock) {
                    serverLock.wait();
                }
                if(i<ServerLock.getVersion(ip)){
                    cardBin ="";
                    map.remove("key"+i);
                }

                else{
                    cardBin = ioSession.getAttribute("cardNum").toString();
                    ioSession.removeAttribute("cardNum");
                }
//                ioSession.removeAttribute("waitCardInfo1");
                logger.warn("=============>IP"+ip+"寻卡完成");
            }
            catch (Exception e){
                logger.warn("选卡指令异常，面板IP:"+ip,e);
                e.printStackTrace();
                System.out.println("=============>IP:"+ip+"寻卡异常");
            }

        }else{
            logger.warn("没有获取连接，面板IP:"+ip);
        }
        return cardBin;
    }

    public String getTrueCardBin(String oldCardBin){
        int len = oldCardBin.length();
        int start = len-1;
        int sub = 0;
        for(int i = start;i>=0;i--){
            char matchStr = oldCardBin.charAt(i);
            boolean isNum = Character.isDigit(matchStr);
            if(isNum)
                continue;
            else
                sub++;
        }
        return  oldCardBin.substring(0,len-sub);
    }

    public  void LiushuiReturn(String liushui,String ip) throws UnsupportedEncodingException {
        IoSession ioSession = MyHandler.sessionsConcurrentHashMap.get(ip);
        if(ioSession  != null) {
            ByteBuffer buffer = ByteBuffer.allocate(37);
            buffer.put((byte) 0x0A);
            buffer.put((byte) 0x01);
            buffer.put((byte) 0x08);
            buffer.put((byte) 0x00);
            buffer.put((byte) 0x1d);
            buffer.put(liushui.getBytes(MinaServer.charSet));//流水号
            buffer.put(Hex.hexFormat("szepark", 20, 1).getBytes(MinaServer.charSet));//应用终端ID
            buffer.rewind();
            //计算校验和
            int checkSum = 0;
            for (int i = 0; i < 37 - 3; i++) {//减去头尾
                if (i == 0) {
                    buffer.get();
                    continue;
                }
                checkSum += buffer.get() & 0xff;//转无符号int
            }
            buffer.put((byte) 0x0b);//协议尾
            buffer.putShort((short) checkSum);
            ioSession.write(buffer.array());
            String hex = Hex.bin2HexStr(buffer.array());
            logger.warn("=============>IP:"+ip+"流水发送指令"+hex);
            System.out.println("流水返回协议hex：====" + hex);
            System.out.println("流水返回协议长度：====" + hex.length());
        }

    }
}

