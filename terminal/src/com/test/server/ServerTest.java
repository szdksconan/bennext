package com.test.server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sun.misc.Unsafe;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;


/**
 * Created by  on 2017/2/12 0012.
 */
public class ServerTest {


    public static void main(String[] args) {
        ClassPathXmlApplicationContext cp = new ClassPathXmlApplicationContext("spring-mina.xml");
        while (true){
            System.out.println("现在连接数："+MyHandler.sessionsConcurrentHashMap.size());
            for(String ip:MyHandler.sessionsConcurrentHashMap.keySet()){
                System.out.println("ip:"+MyHandler.sessionsConcurrentHashMap.get(ip));
                IoSession ioSession = MyHandler.sessionsConcurrentHashMap.get(ip);
                String ipa = (String) ioSession.getAttribute("ip");
                String  porta  = (String)ioSession.getAttribute("port");
                System.out.println("现在连接有："+ipa+":"+porta);
                //测试寻卡
                //组装指令
                //Hex.testSeachCardDemo();
                String s16 = "0A0001004C20202020202020202020202020737A657061726B323031362D30352D32302030303A30303A30302020323031362D30352D32302030313A30303A3030B9F341383838383820203030303030310B10D0";
                ioSession.write(Hex.hexStr2BinArr(s16));
                try {
                    Thread.sleep(10000l);
                //测试取消指令
                // String canel =   "0A0104001420202020202020202020202020737A657061726B0B04B9";
                //    ioSession.write(Hex.hexStr2BinArr(canel));

                //测试扣费指令
               String koufei  ="0A0101005531383135333938323320202020202020202020202020737A657061726B323031362D30352D32302030303A30303A30302020323031362D30352D32302030313A30303A3030B9F341383838383820203030303030310B12B2";
                ioSession.write(Hex.hexStr2BinArr(koufei));
                Thread.sleep(1000000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // sendMsg(s16,ioSession);
            }
            try {
                Thread.sleep(10000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void sendMsg(String s, IoSession session) {//阻塞
        WriteFuture writeF = session.write(Hex.hexStr2BinArr(s));
        writeF.awaitUninterruptibly();
        if (writeF.getException() != null) {
            System.out.println(writeF.getException().getMessage());
        } else if (writeF.isWritten()) {
            System.out.println("msg was sent!");
            // 发送、接受
            ReadFuture readF = session.read();
            readF.awaitUninterruptibly(1000l);
            if (readF.getException() != null) {
                System.out.println(readF.getException().getMessage());
            } else {
                byte [] byte1 = (byte[]) readF.getMessage();
                System.out.println("[client]========="+byte1.length);
            }
        } else {
            System.out.println("error!");
        }
    }

}
