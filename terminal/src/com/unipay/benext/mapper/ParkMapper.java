package com.unipay.benext.mapper;

import com.unipay.benext.framework.tool.PageFilter;
import com.unipay.benext.model.park.Bill;
import com.unipay.benext.model.park.ExitWay;
import com.unipay.benext.model.park.Park;
import com.unipay.benext.model.park.PayPanel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by liuh on 2017/2/13 0013.
 */
public interface ParkMapper {
    /**
     * 获取停车场信息
     * @return
     */
    public List<Park> getParkInfo();
    public int addOrUpdatePark(Park park);
    public int addOrUpdateParkForCharge(Park park);

    /**
     * 获取面板信息
     * @return
     */
    public List<PayPanel> getPayPanel(Map map);
    public PayPanel getPayPanelById(String id);
    public int getPayPanelCount();
    public int addPayPanel(PayPanel payPanel);
    public int editPayPanel(PayPanel payPanel);
    public int delPayPanel(String id);

    /**
     * 获取出口信息
     * @return
     */
    public List<Map> getExitWay(Map map);
    public int getExitWayCount();
    public int getExitWayCountByCondi(Map map);
    public int addExitWay(ExitWay exitWay);
    public Map getExitWayById(String id);
    public int editExitWay(ExitWay exitWay);
    public int delExitWay(String id);

    /**
     * 获取流水信息
     */
    public List<Bill> getBill(Map map);
    public int getBillCount(Map map);

    /**
     * 获取云端设置
     */
    public List<Map> getCloudSet();
    public int addOrUpdateCloudSet(Map map);


}
