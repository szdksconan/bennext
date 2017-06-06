package com.unipay.benext.service.park;

import com.unipay.benext.framework.tool.PageFilter;
import com.unipay.benext.model.park.Bill;
import com.unipay.benext.model.park.ExitWay;
import com.unipay.benext.model.park.Park;
import com.unipay.benext.model.park.PayPanel;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/13 0013.
 */
public interface ParkService {

    /**
     * 获取停车场信息
     * @return
     */
    public List<Park> getPark();
    public int addOrupdateParkInfo(Park park);
    public int addOrUpdateParkForCharge(Park park);

    /**
     * 获取面板信息
     * @return
     */
    public List<PayPanel> getPayPanel(PageFilter pageFilter);
    public PayPanel getPayPanelById(String id);
    public int getPayPanelCount();
    public int addPayPanel(PayPanel payPanel);
    public int editPayPanel(PayPanel payPanel);
    public int delPayPanel(String id);

    /**
     * 获取出口信息
     */

    public List<Map> getExitWay(PageFilter pageFilter);
    public int getExitWayCount();
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
     * 云端信息设置
     */
    public List<Map> getCloudSet();
    public int addOrUpdateCloudSet(Map map);

}
