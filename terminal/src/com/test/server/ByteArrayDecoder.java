package com.test.server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * @author liuh 处理粘包断包demo 有需要再看 现在协议 不需要
 * 字节数组解码器
 */
public class ByteArrayDecoder extends CumulativeProtocolDecoder {

    public boolean doDecode(IoSession session, IoBuffer in,
                            ProtocolDecoderOutput out) throws Exception {
        // TODO Auto-generated method stub
        if(in.remaining()==2){ //如果是心跳包 直接处理
            byte[] heart =new  byte[2];
            in.get(heart);
            out.write(heart);
            return false;
        }
        if (in.remaining() > 5) {// 如果是业务数据包

            // 有数据时，读取 4 字节判断消息长度
            byte[] sizeBytes = new byte[2];
            // 标记当前位置，以便 reset
            in.mark();
            // 读取钱 4 个字节
            in.get(sizeBytes,3,5);
            int size = NumberUtil.bytes2int(sizeBytes);
            if (size > in.remaining()) {
                // 如果消息内容的长度不够，则重置（相当于不读取 size），返回 false
                in.reset();
                // 接收新数据，以拼凑成完整的数据~
                return false;
            } else {
                byte[] dataBytes = new byte[size];
                in.get(dataBytes, 0, size);
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