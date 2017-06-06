package com.unipay.benext.service.cloud.impl;

import com.unipay.benext.service.cloud.ServiceInfoService;
import com.unipay.benext.task.ServiceInfoT;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 线程管理
 * Created by LiuY on 2017/3/5 0005.
 */
@Service
public class ServiceInfoAfter implements InitializingBean {

    @Autowired
    ServiceInfoService serviceInfoService;

    @Override
    public void afterPropertiesSet() throws Exception {
        //Executor executor = Executors.newFixedThreadPool(1);
        //executor.execute(new ServiceInfoT(serviceInfoService));
    }
}
