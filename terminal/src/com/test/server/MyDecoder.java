package com.test.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.util.byteaccess.ByteArray;

import java.nio.charset.Charset;

/**
 * Created by LiuY on 2017/2/22 0022.
 */
public class MyDecoder extends CumulativeProtocolDecoder {

    static Logger log = Logger.getRootLogger();
    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        System.out.println("=====================MINA 解码"+in.remaining());
        // TODO Auto-generated method stub
        if(in.remaining()==2){ //如果是心跳包 直接处理
            byte[] heart =new  byte[2];
            in.get(heart);
            out.write(heart);
            return false;
        }
        if (in.remaining() > 5) {// 如果是业务数据包
            // 标记当前位置，以便 reset
            in.mark();
            //前三个长度没用
            byte[] throwBytes = new byte[3];
            in.get(throwBytes);
            // 有数据时，读取 2 字节判断消息长度
            int size =  in.getShort();
            System.out.println("协议数据长度========"+size);
            System.out.println("实际数据长度========"+size);
            in.reset();
            if (size > in.limit()-8) {
                // 如果消息内容的长度不够，则重置（相当于不读取 size），返回 false
                // 接收新数据，以拼凑成完整的数据~
                return false;
            } else {
                byte[] dataBytes = new byte[size+8];
                in.get(dataBytes);
                out.write(dataBytes);
                if (in.remaining() > 0) {
                    // 如果读取内容后还粘了包，就让父类把剩下的数据再给解析一次~
                    return true;
                }
            }
        }
        // 处理成功，让父类进行接收下个包
        return false;

    }

}
