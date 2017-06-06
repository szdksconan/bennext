package com.unipay.benext.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * 操作json文件
 * Created by Administrator on 2017/2/18.
 */
public class JsonOperate {
    /*
	* 取出文件内容，填充对象
	*/
    public static JSONObject readJson(String path){
        String sets=ReadFile(path);//获得json文件的内容
        JSONObject jo=JSONObject.parseObject(sets);//格式化成json对象
        return jo;
    }

    //读文件，返回字符串
    private static String ReadFile(String path){
        File file = new File(path);
        String laststr = "";
        RandomAccessFile read = null;
        FileChannel channel = null;
        FileLock lock = null;
        try {
            //获取文件锁，独占锁 知道文件被释放
            read = new RandomAccessFile(file, "rws");
            channel = read.getChannel();
            while (true){
                try {
                    lock = channel.tryLock();
                    break;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            String tempString = null;
            int line = 1;
            //一次读入一行，直到读入null为文件结束
            while ((tempString = read.readLine()) != null) {
                //显示行号
                laststr = laststr + tempString;
                line ++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (read != null) {
                try {
                    read.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return laststr;
    }

    //把json格式的字符串写到文件
    public static void writeFile(String filePath, String sets){
        File file = new File(filePath);
        RandomAccessFile write = null;
        FileChannel channel = null;
        FileLock lock = null;
        try {
            //获取文件锁，独占锁 知道文件被释放
            write = new RandomAccessFile(file, "rws");
            channel = write.getChannel();
            while (true){
                try {
                    lock = channel.tryLock();
                    break;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            write.write(sets.getBytes("utf-8"));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                write.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
