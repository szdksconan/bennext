package com.test.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import sun.misc.PostVMInitHook;

/**
 * Created by Administrator on 2017/2/12 0012.
 */
public class ServerTest {


    public static void main(String[] args) {
        ClassPathXmlApplicationContext cp = new ClassPathXmlApplicationContext("spring-mina.xml");
    }
}
