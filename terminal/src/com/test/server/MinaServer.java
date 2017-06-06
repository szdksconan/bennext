package com.test.server;

import org.apache.ibatis.annotations.Case;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Liuh on 2017/2/24 0024.
 */
public class MinaServer {
        private Logger logger = Logger.getLogger("MINA");
        public static final String charSet="gbk";

        public static final String oa = "0A";
        public static final String ob = "0B";

        public static final String searchCard= "0001";//寻卡
        public static final String pay ="0101";//扣费
        public static final String billUp="0108";//流水上传
        public static final String cancel="0104";//取消交易

        public static final String searchCardLen="004C";//数据长度76
        public static final String searchPayLen="0055";//数据长度85

    /**
     *
     * @param buffer 包含协议信息的buffer
     * @param session 连接对象
     */
    public void handler(ByteBuffer buffer, IoSession session) throws UnsupportedEncodingException {
        System.out.println("返回数据长度："+buffer.limit());
        logger.warn("返回数据长度："+buffer.limit());
        buffer.get();//协议头
        String type = Hex.byteToHex(buffer.get())+Hex.byteToHex(buffer.get());//type
        System.out.println("type===================================="+type);
            if(searchCard.equals(type)){
               // System.out.println("寻卡响应");
             //   logger.warn("寻卡响应"+session);
                logger.warn("寻卡返回指令"+Hex.bin2HexStr(buffer.array()));
                int dataSize = buffer.getShort();//协议体数据长度
                byte[]  clientNumB =new byte[20];
                buffer.get(clientNumB);
                String clientNumStr = new String(clientNumB,"gbk");
                System.out.println("clientNumStr============="+clientNumStr);
                byte[]  cardNumB =new byte[20];
                buffer.get(cardNumB);
                String cardNum = new String(cardNumB,"gbk");
                System.out.println("cardNum============="+cardNum);
               // logger.warn("卡号"+cardNum);
                String stat = Hex.byteToHex(buffer.get())+Hex.byteToHex(buffer.get());
                System.out.println("交易状态========="+stat);
              //  logger.warn("交易状态========="+stat);
                System.out.println("存放对象"+ session.getAttribute("waitCardInfo"));
                session.setAttribute("cardNum",cardNum.replaceAll(" ",""));
//                if( session.getAttribute("waitCardInfo")!=null) {
//                    Object o =session.getAttribute("waitCardInfo");
//                    session.setAttribute("waitCardInfo_1",cardNum);
//                    session.setAttribute("cardNum",cardNum);
//                    synchronized (o){
//                        o.notifyAll();
//                    }

//                }
                ConcurrentHashMap<String,Object>  map = (ConcurrentHashMap<String, Object>) session.getAttribute("lock");
                Set<String> keys = map.keySet();
                for(String key:keys){
                    Object obj =  map.get(key);
                    synchronized (obj){
                        obj.notifyAll();
                    }

                }
            }else if(pay.equals(type)){
//                System.out.println("扣费响应");
//                logger.warn("扣费响应=========");
                logger.warn("扣费返回指令"+Hex.bin2HexStr(buffer.array()));
                int dataSize = buffer.getShort();//协议体数据长度
                byte[]  liushui =new byte[9];
                buffer.get(liushui);
                String liushuiStr = new String(liushui,"gbk");
                System.out.println("liushuiStr============="+liushuiStr);
//                logger.warn("流水号========="+liushuiStr);
                byte[]  clientNumB =new byte[20];
                buffer.get(clientNumB);
                String clientNumStr = new String(clientNumB,"gbk");
                System.out.println("clientNumStr============="+clientNumStr);

                byte[] carNum = new byte[10];
                buffer.get(carNum);
                String carNumStr = new String(carNum,"gbk");
                System.out.println("carNumStr============="+carNumStr);
//                logger.warn("车牌号========="+carNumStr);
                String stat = Hex.byteToHex(buffer.get())+Hex.byteToHex(buffer.get());
                System.out.println("交易状态========="+stat);
//                logger.warn("交易状态========="+stat);
                session.setAttribute("stat",stat);
                ConcurrentHashMap<String,Object>  map = (ConcurrentHashMap<String, Object>) session.getAttribute("lock");
                Set<String> keys = map.keySet();
                for(String key:keys){
                    Object obj =  map.get(key);
                    synchronized (obj){
                        obj.notifyAll();
                    }

                }
            }else if(billUp.equals(type)){
              //  System.out.println("流水上传");
//                logger.warn("流水上传=========");
                logger.warn("流水返回指令"+Hex.bin2HexStr(buffer.array()));
                int dataSize = buffer.getShort();//协议体数据长度
                byte[]  liushui =new byte[9];
                buffer.get(liushui);
                String liushuiStr = new String(liushui,"gbk");
                System.out.println("===========>"+Hex.bin2HexStr(liushui));
                System.out.println("liushuiStr============="+liushuiStr);
//                logger.warn("流水号========="+liushuiStr);
                byte[]  clientNumB =new byte[20];
                buffer.get(clientNumB);
                String clientNumStr = new String(clientNumB,"gbk");
                System.out.println("===========>"+Hex.bin2HexStr(clientNumB));
                System.out.println("clientNumStr============="+clientNumStr);

                byte[] carNum = new byte[10];
                buffer.get(carNum);
                String carNumStr = new String(carNum,"gbk");
                System.out.println("===========>"+Hex.bin2HexStr(carNum));
                System.out.println("carNumStr============="+carNumStr);
//                logger.warn("车牌号========="+carNumStr);
                String dec = Hex.byteToHex(buffer.get())+Hex.byteToHex(buffer.get());
                System.out.println("交易说明========="+dec);
//                logger.warn("交易说明========="+dec);

                byte[]  payTime = new byte[20];
                buffer.get(payTime);
                String payTimeStr = new String(payTime,"gbk");
                    System.out.println("===========>"+Hex.bin2HexStr(payTime));
                System.out.println("交易时间========="+payTimeStr);

                byte[]  payCard = new byte[20];
                buffer.get(payCard);
                String payCardStr = new String(payCard,"gbk");
                System.out.println("===========>"+Hex.bin2HexStr(payCard));
                System.out.println("交易卡号========="+payCardStr);

                byte[] money = new byte[6];
                buffer.get(money);
                String moneyStr = new String (money,"gbk");
                System.out.println("===========>"+Hex.bin2HexStr(money));
                System.out.println("收费金额========="+moneyStr);
//
                byte[] money_1 = new byte[6];
                buffer.get(money_1);
                String money_1Str = new String (money_1,"gbk");
//                System.out.println("===========>"+Hex.bin2HexStr(money_1));
                System.out.println("收费金额========="+money_1Str);
                session.setAttribute("money_1Str",money_1Str);
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("dec",dec);
                session.setAttribute(liushuiStr,map);
                ConcurrentHashMap<String,Object>  liuMap = (ConcurrentHashMap<String, Object>) session.getAttribute("rlock");
                Set<String> keys = liuMap.keySet();
                for(String key:keys){
                    Object obj =  liuMap.get(key);
                    synchronized (obj){
                        obj.notifyAll();
                    }

                }

            }else if(cancel.equals(type)){
                System.out.println("取消交易响应");
//                logger.warn("=========取消交易响应");
                logger.warn("取消交易返回指令"+Hex.bin2HexStr(buffer.array()));
                int dataSize = buffer.getShort();//协议体数据长度
                byte[]  liushui =new byte[9];
                buffer.get(liushui);
                String liushuiStr = new String(liushui,"gbk");
                System.out.println("liushuiStr============="+liushuiStr);
//                byte[]  clientNumB =new byte[20];
//                buffer.get(clientNumB);
//                String clientNumStr = new String(clientNumB,"gbk");
//                System.out.println("clientNumStr============="+clientNumStr);
                session.setAttribute("liushuiCancel",liushuiStr);
                ConcurrentHashMap<String,Object>  map = (ConcurrentHashMap<String, Object>) session.getAttribute("lock");
                Set<String> keys = map.keySet();
                for(String key:keys){
                    Object obj =  map.get(key);
                    synchronized (obj){
                        obj.notifyAll();
                    }

                }
//                Object cancelObj = session.getAttribute("cancel");
//                if(cancelObj != null){
//                    synchronized (cancelObj){
//                        cancelObj.notifyAll();
//                    }
//                }
            }


        }

}
