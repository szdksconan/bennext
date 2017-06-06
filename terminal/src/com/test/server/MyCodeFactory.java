package com.test.server;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MyCodeFactory implements ProtocolCodecFactory {

    private final MyEncoder encoder;
    private final MyDecoder decoder;

    public MyCodeFactory() {
        this(Charset.forName("utf-16"));
    }

    public MyCodeFactory(Charset charset) {
        encoder = new MyEncoder();
        decoder = new MyDecoder();
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
        // TODO Auto-generated method stub
        return decoder;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
        // TODO Auto-generated method stub
        return encoder;
    }

    /*public int getEncoderMaxLineLength() {
        return encoder.getMaxLineLength();
    }

    public void setEncoderMaxLineLength(int maxLineLength) {
        encoder.setMaxLineLength(maxLineLength);
    }

    public int getDecoderMaxLineLength() {
        return decoder.getMaxLineLength();
    }

    public void setDecoderMaxLineLength(int maxLineLength) {
        decoder.setMaxLineLength(maxLineLength);
    }*/
}