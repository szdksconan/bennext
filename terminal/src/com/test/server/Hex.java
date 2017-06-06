package com.test.server;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.mina.core.buffer.IoBuffer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Condition;

/**
 * Created by LiuY on 2017/2/22 0022.
 */
public class Hex {

    private static String hexStr =  "0123456789ABCDEF";
    private static String[] binaryArray =
            {"0000","0001","0010","0011",
                    "0100","0101","0110","0111",
                    "1000","1001","1010","1011",
                    "1100","1101","1110","1111"};

    /**
     *
     * @return 二进制数组转换为二进制字符串   2-2
     */
    public static String bytes2BinStr(byte[] bArray){

        String outStr = "";
        int pos = 0;
        for(byte b:bArray){
            //高四位
            pos = (b&0xF0)>>4;
            outStr+=binaryArray[pos];
            //低四位
            pos=b&0x0F;
            outStr+=binaryArray[pos];
        }
        return outStr;
    }

    /**
     *
     * @param bytes
     * @return 将二进制数组转换为十六进制字符串  2-16
     */
    public static String bin2HexStr(byte[] bytes){

        String result = "";
        String hex = "";
        for(int i=0;i<bytes.length;i++){
            //字节高4位
            hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));
            //字节低4位
            hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));
            result +=hex;  //+" "
        }
        return result;
    }

    public static String byteToHex(byte byte_1){
        String hex = "";
        //字节高4位
        hex = String.valueOf(hexStr.charAt((byte_1&0xF0)>>4));
        //字节低4位
        hex += String.valueOf(hexStr.charAt(byte_1&0x0F));
        return hex;
    }

    /**
     *
     * @param hexString
     * @return 将十六进制转换为二进制字节数组   16-2
     */
    public static byte[] hexStr2BinArr(String hexString){
        //hexString的长度对2取整，作为bytes的长度
        int len = hexString.length()/2;
        byte[] bytes = new byte[len];
        byte high = 0;//字节高四位
        byte low = 0;//字节低四位
        for(int i=0;i<len;i++){
            //右移四位得到高位
            high = (byte)((hexStr.indexOf(hexString.charAt(2*i)))<<4);
            low = (byte)hexStr.indexOf(hexString.charAt(2*i+1));
            bytes[i] = (byte) (high|low);//高地位做或运算
        }
        return bytes;
    }



    /**
     *
     * @param hexString
     * @return 将十六进制转换为二进制字符串   16-2
     */
    public static String hexStr2BinStr(String hexString){
        return bytes2BinStr(hexStr2BinArr(hexString));
    }


    public static String strToHex(String str){
        byte[] ba;
        StringBuffer  sb = new StringBuffer();;
        try {
            ba = str.getBytes("UTF-16");
            char[] ca = hexStr.toCharArray();
            for (byte b : ba) {
                int bi = (b & 0xf0) >> 4;
                sb.append(ca[bi]);
                int bi2 = b & 0x0f;
                sb.append(ca[bi2]);
            }
            System.out.println(sb.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String hexStringToString(String s,String charset) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, charset);
            System.out.println(s);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * 协议体位数不足补空格
     * @param num  协议体byte长度
     * @param type  1是左边补足 2是右边补足
     */
    public  static String hexFormat(String s,int num,int type)  {
        int trueLength = 0;
        try {
            trueLength = s.getBytes("gbk").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(s.length()>num) return s;
        int addnum = num-(trueLength-s.length());

        if(1==type)
            return String.format("%"+addnum+"s", s);
        else if(2==type)
            return String.format("%-"+addnum+"s", s);
        else
            return "";
    }


    /**
     * 封包demo
     * @throws UnsupportedEncodingException
     */
    public static void testDemoByteBuffer() throws UnsupportedEncodingException {
        String s16 = "0A0001004C20202020202020202020202020737A657061726B323031362D30352D32302030303A30303A30302020323031362D30352D32302030313A30303A3030B9F341383838383820203030303030310B10D0";
        ByteBuffer buffer = ByteBuffer.allocate(84);
        buffer.put((byte)0x0A);
        buffer.put((byte)0x00);
        buffer.put((byte)0x01);
        buffer.put((byte)0x00);
        buffer.put((byte)0x4C);
        System.out.println(buffer.position());
        buffer.put(Hex.hexFormat("szepark",20,1).getBytes(MinaServer.charSet));
        System.out.println(buffer.position());
        buffer.put("2016-05-20 00:00:00  2016-05-20 01:00:00".getBytes(MinaServer.charSet));
        System.out.println(buffer.position());
        buffer.put(Hex.hexFormat("贵A88888",10,2).getBytes(MinaServer.charSet));
        System.out.println(buffer.position());
        buffer.put("000001".getBytes(MinaServer.charSet));
        System.out.println(buffer.position());
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
        String hex = Hex.bin2HexStr(buffer.array());
        System.out.println(hex);
        System.out.println(s16.equals(hex));
    }

    public static void testDemoLiushui() throws UnsupportedEncodingException {
        String s16 = "0A0001004C20202020202020202020202020737A657061726B323031362D30352D32302030303A30303A30302020323031362D30352D32302030313A30303A3030B9F341383838383820203030303030310B10D0";
        ByteBuffer buffer = ByteBuffer.allocate(93);
        buffer.put((byte)0x0A);
        buffer.put((byte)0x01);
        buffer.put((byte)0x01);
        buffer.put((byte)0x00);
        buffer.put((byte)0x55);
        System.out.println(buffer.position());
        buffer.put(new SimpleDateFormat("HHmmssSSS") .format(new Date() ).getBytes(MinaServer.charSet));
        System.out.println(buffer.position());
        buffer.put(Hex.hexFormat("szepark",20,1).getBytes(MinaServer.charSet));//应用终端ID
        buffer.put("2016-05-20 00:00:00  2016-05-20 01:00:00".getBytes(MinaServer.charSet));//出入时间
        System.out.println(buffer.position());
        buffer.put(Hex.hexFormat("贵A88888",10,2).getBytes(MinaServer.charSet));//车牌号
        System.out.println(buffer.position());
        buffer.put("000001".getBytes(MinaServer.charSet));//金额 1分
        System.out.println(buffer.position());
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
        System.out.println("扣费协议hex：===="+hex);
        System.out.println("扣费协议长度：===="+hex.length());
    }

    public static void testDemoCanel() throws UnsupportedEncodingException {
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
        String hex = Hex.bin2HexStr(buffer.array());
        System.out.println("取消协议hex：===="+hex);
        System.out.println("取消协议长度：===="+hex.length());

    }


    public static void testLiushuiReturn() throws UnsupportedEncodingException {
        ByteBuffer buffer = ByteBuffer.allocate(37);
        buffer.put((byte)0x0A);
        buffer.put((byte)0x01);
        buffer.put((byte)0x08);
        buffer.put((byte)0x00);
        buffer.put((byte)0x1d);
        buffer.put(new SimpleDateFormat("HHmmssSSS") .format(new Date() ).getBytes(MinaServer.charSet));//流水号
        buffer.put(Hex.hexFormat("szepark",20,1).getBytes(MinaServer.charSet));//应用终端ID
        buffer.rewind();
        //计算校验和
        int checkSum = 0;
        for(int i=0;i<37-3;i++){//减去头尾
            if(i==0) {
                buffer.get();
                continue;
            }
            checkSum += buffer.get()&0xff;//转无符号int
        }
        buffer.put((byte)0x0b);//协议尾
        buffer.putShort((short) checkSum);
        String hex = Hex.bin2HexStr(buffer.array());
        System.out.println("流水返回协议hex：===="+hex);
        System.out.println("流水返回协议长度：===="+hex.length());

    }

    /**
     * 封包demo
     * @throws UnsupportedEncodingException
     */
    public static  void  testSeachCardDemo(){

        String s16 = "0A0001004C20202020202020202020202020737A657061726B323031362D30352D32302030303A30303A30302020323031362D30352D32302030313A30303A3030B9F341383838383820203030303030310B10D0";
        //测试寻卡
        //组装指令
        StringBuffer sb = new StringBuffer();
        sb.append(MinaServer.oa);
        sb.append(MinaServer.searchCard);//协议头
        sb.append(MinaServer.searchCardLen);
        try {
            sb.append(Hex.bin2HexStr(Hex.hexFormat("szepark",20,1).getBytes(MinaServer.charSet)));//终端应用ID
            sb.append(Hex.bin2HexStr("2016-05-20 00:00:00  2016-05-20 01:00:00".getBytes(MinaServer.charSet)));//出入时间
            sb.append(Hex.bin2HexStr(Hex.hexFormat("贵A88888",10,2).getBytes(MinaServer.charSet)));//车牌号
            sb.append(Hex.bin2HexStr("000001".getBytes(MinaServer.charSet)));//金额
        }catch (Exception e){
            e.printStackTrace();
        }
        //校验和
        String data  = sb.toString();
        int checkSum = 0;
        for(int i=0;i<sb.length()-1;i+=2){
            if(i==0) continue;
            checkSum += Integer.parseInt(sb.substring(i,i+2),16);
            System.out.print("  "+checkSum);
        }
        System.out.println();
        System.out.println("checkSum:"+checkSum);
        String sumHex = Integer.toHexString(checkSum);
        System.out.println(checkSum);
        sb.append(MinaServer.ob);//协议尾
        sb.append(sumHex);
        System.out.println(sb.toString());
        System.out.println(sb.toString().length());
        System.out.println(s16);
        System.out.println(s16.length());
        System.out.println(sb.toString().toUpperCase().equals(s16));
    }


    public static void main(String[] args) throws UnsupportedEncodingException {
        //流水返回
        testLiushuiReturn();

        //取消模拟协议
        testDemoCanel();

        //扣费模拟协议
        testDemoLiushui();
        System.out.println("0A0101005530303030303030303220202020202020202020202020737A657061726B323031362D30352D32302030303A30303A30302020323031362D30352D32302030313A30303A3030B9F341383838383820203030303030320B128D".length());

        System.out.println(new SimpleDateFormat("HHmmssSSS") .format(new Date() ));
        System.out.println(Integer.parseInt("0055",16));
        testDemoByteBuffer();
        testSeachCardDemo();

        //校验和 S16为 协议数据 去除头尾
        String s16 = "0A0001004C20202020202020202020202020737A657061726B323031362D30352D32302030303A30303A30302020323031362D30352D32302030313A30303A3030B9F341383838383820203030303030310B";
        int checkSum = 0;
        for(int i=0;i<s16.length()-2;i+=2){
            if(i==0) continue;
            checkSum += Integer.parseInt(s16.substring(i,i+2),16);
        }
        System.out.println(Integer.toHexString(checkSum));
        System.out.println(Integer.parseInt("10D0",16));


        String s= "02817021601";

        System.out.println("return_1:"+hexFormat(s,20,1));
        System.out.println("return_1:"+hexFormat(s,20,2));
        System.out.println((Integer.parseInt("0055",16)));
        System.out.println(Hex.bin2HexStr("2016-05-20 00:00:00  2016-05-20 01:00:00".getBytes("gbk")));

        String hex = "0A0001004C20202020202020202020202020737A657061726B323031362D30352D32302030303A30303A30302020323031362D30352D32302030313A30303A3030B9F341383838383820203030303030310B10D0";
        String returnHex = "0A0001002A2020202020202020203032383137303231363031202020202020202020202020202020202020202000010B05F8";

        String hex_1 = hex.substring(0,10);//协议头 5B
        String hex_2 = hex.substring(10,50);//终端应用id号 20b
        System.out.println("hex_3:"+new String (Hex.hexStr2BinArr(hex_2),"gbk"));
        String hex_3 = hex.substring(50,130);//车辆出入场时间 40b
        System.out.println("hex_3:"+new String (Hex.hexStr2BinArr(hex_3),"gbk"));
        String hex_4= hex.substring(130,150);// 车牌号 10b
        System.out.println("hex_4:"+new String (Hex.hexStr2BinArr(hex_4),"gbk"));
        String hex_5 = hex.substring(150,162);//缴费金额 6b
        System.out.println("hex_5:"+new String (Hex.hexStr2BinArr(hex_5),"gbk"));
        String hex_6 = hex.substring(162,168);//协议尾
        System.out.println("hex_6:"+hex_6);


        parsexunka(returnHex);
    }

    //响应数据
    public static  void parsexunka(String returnHex) throws UnsupportedEncodingException {
        String return_1 = returnHex.substring(10,50);//设备号 20b
        System.out.println("return_1:"+new String (Hex.hexStr2BinArr(return_1),"gbk"));
        String return_2 = returnHex.substring(50,90);//银行卡号 20B
        System.out.println("return_2:"+new String (Hex.hexStr2BinArr(return_2),"gbk"));
        System.out.println(returnHex.substring(90,94));
    }



}
