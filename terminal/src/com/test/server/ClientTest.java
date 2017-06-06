package com.test.server;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class ClientTest {
    public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException {
        //创建客户端连接器. 
        NioSocketConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new MyCodeFactory(Charset.forName("utf-16")))); //设置编码过滤器
        connector.setHandler(new ClientHandler());//设置事件处理器
        connector.getSessionConfig().setUseReadOperation(true);//设置这个才允许 阻塞读取  连接多不推荐使用
        ConnectFuture cf = connector.connect(new InetSocketAddress("192.168.0.100", 8889));//建立连接
        cf.awaitUninterruptibly();//等待连接创建完成
        String str = "2015-11-12 12-34-13 ";
        String hex = Hex.strToHex(str);
        String hex_1 = "0203";
        String hex_2 =  Hex.strToHex("测试一下");
        sendMsg(hex_2,cf.getSession());//阻塞
        //cf.getSession().write(Hex.hexStr2BinArr(hex_2));//非阻塞
        //cf.getSession().closeOnFlush();
        //cf.getSession().getCloseFuture().awaitUninterruptibly();//等待连接断开 
       // connector.dispose();
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
            readF.awaitUninterruptibly(10000l);
            if (readF.getException() != null) {
                System.out.println(readF.getException().getMessage());
            } else {
                System.out.println("[client]"+readF.getMessage().toString());
            }
        } else {
            System.out.println("error!");
        }
    }
}