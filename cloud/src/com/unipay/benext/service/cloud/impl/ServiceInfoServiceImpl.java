package com.unipay.benext.service.cloud.impl;

import com.unipay.benext.mapper.ServiceInfoMapper;
import com.unipay.benext.model.cloud.ServiceInfo;
import com.unipay.benext.service.cloud.ServiceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by LiuY on 2017/3/3 0003.
 */
@Service
public class ServiceInfoServiceImpl implements ServiceInfoService {
    @Autowired
    ServiceInfoMapper serviceInfoMapper;
    /**
     * 获取终端服务器信息
     * @param map
     * @return
     */
    @Override
    public List<ServiceInfo> getServiceInfo(Map map) {
        return serviceInfoMapper.getServiceInfo(map);
    }
    @Override
    public int getServiceInfoCount(Map map) {
        return serviceInfoMapper.getServiceInfoCount(map);
    }

    @Override
    public int serviceInfoAdd(ServiceInfo serviceInfo) {
        return serviceInfoMapper.serviceInfoAdd(serviceInfo);
    }

    @Override
    public int serviceInfoEdit(ServiceInfo serviceInfo) {
        return serviceInfoMapper.serviceInfoEdit(serviceInfo);
    }

    @Override
    public int serviceInfoDel(String id) {
        return serviceInfoMapper.serviceInfoDel(id);
    }

    @Override
    public int updateBatch(List ids,Date now) {
        return serviceInfoMapper.updateBatch(ids,now);
    }

    @Override
    public void terminalHeartAcess(Map map) {
        serviceInfoMapper.terminalHeartAcess(map);
    }
}
