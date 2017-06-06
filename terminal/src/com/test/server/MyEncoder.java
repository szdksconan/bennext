package com.test.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;

/**
 * Created by LiuY on 2017/2/22 0022.
 */
public class MyEncoder extends ProtocolEncoderAdapter {


    static Logger log = Logger.getRootLogger();

    public MyEncoder(){

    }
    @Override
    public void encode(IoSession session, Object message,
                       ProtocolEncoderOutput out) throws Exception {
       log.debug("MyEncoder.encode()");
        byte[] bytes = (byte[])message;
        //1024
        IoBuffer buffer = IoBuffer.allocate(256);
        buffer.setAutoExpand(true);
        buffer.put(bytes);
        buffer.flip();
        out.write(buffer);
        out.flush();
        buffer.free();
    }

    public void dispose(IoSession session) throws Exception {
    }
}
