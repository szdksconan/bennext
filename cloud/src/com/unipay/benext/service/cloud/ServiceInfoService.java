package com.unipay.benext.service.cloud;

import com.unipay.benext.model.cloud.ServiceInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by LiuY on 2017/3/3 0003.
 */
public interface ServiceInfoService {
    /**
     * 获取终端服务器信息
     * @param map
     * @return
     */
    public List<ServiceInfo> getServiceInfo(Map map);
    public int getServiceInfoCount(Map map);
    public int serviceInfoAdd(ServiceInfo serviceInfo);
    public int serviceInfoEdit(ServiceInfo serviceInfo);
    public int serviceInfoDel(String id);
    public int updateBatch(List list,Date now);
    public void terminalHeartAcess(Map map);

}
