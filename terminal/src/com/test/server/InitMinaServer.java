package com.test.server;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * Created by liuh on 2017/2/26 0026.
 */
public class InitMinaServer {


    public InitMinaServer(NioSocketAcceptor ioAcceptor){
        System.out.println("=============mina init ===================");
      //  ioAcceptor.getSessionConfig().setUseReadOperation(true);
    }


}
