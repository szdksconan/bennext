package com.test.server;

import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.javassist.bytecode.SourceFileAttribute;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class MyHandler extends IoHandlerAdapter {
    private final int IDLE = 3000;//(单位s)
    private final Logger LOG = Logger.getLogger(MyHandler.class);
    private MinaServer minaServer ;
    // public static Set<IoSession> sessions = Collections.synchronizedSet(new HashSet<IoSession>());
    public static ConcurrentHashMap<String, IoSession> sessionsConcurrentHashMap = new ConcurrentHashMap<String, IoSession>();

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        session.closeOnFlush();
        LOG.warn("session occured exception, so close it." + cause.getMessage());
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

        byte[] bytes = (byte[]) message;
        if(bytes.length==2){
            String b = "0203";
            session.write(Hex.hexStr2BinArr(b));
            return ;
        }else{
            System.out.println("MyHandler===========server");
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            minaServer.handler(buffer,session);

        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        LOG.warn("messageSent:" + message);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        LOG.warn("remote client [" + session.getRemoteAddress().toString() + "] connected.");
        // 建立连接保存 连接信息
        Long time = System.currentTimeMillis();
        String remoteAddress = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
        session.setAttribute("ip",remoteAddress);
        String port =((InetSocketAddress) session.getRemoteAddress()).getPort()+"";
        session.setAttribute("port",port);
        String id = remoteAddress+":"+port;
        session.setAttribute("id", id);
        sessionsConcurrentHashMap.put(remoteAddress, session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        LOG.warn("sessionClosed.");
        session.closeOnFlush();
        // my
        sessionsConcurrentHashMap.remove(session.getAttribute("id"));
    }

    /**
     * 空闲连接
     * @param session 连接
     * @throws Exception
     */
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        LOG.warn("session idle, so disconnecting......");
       // session.closeOnFlush(); 不做管理
        LOG.warn("disconnected.");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        LOG.warn("sessionOpened.");
        //设置空闲时间判断  这里可以设置为永久的
        //session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, IDLE);
    }

    public MinaServer getMinaServer() {
        return minaServer;
    }

    public void setMinaServer(MinaServer minaServer) {
        this.minaServer = minaServer;
    }
}