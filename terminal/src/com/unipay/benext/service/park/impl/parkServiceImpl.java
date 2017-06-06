package com.unipay.benext.service.park.impl;

import com.unipay.benext.framework.tool.PageFilter;
import com.unipay.benext.mapper.ParkMapper;
import com.unipay.benext.model.park.Bill;
import com.unipay.benext.model.park.ExitWay;
import com.unipay.benext.model.park.Park;
import com.unipay.benext.model.park.PayPanel;
import com.unipay.benext.service.park.ParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuh on 2017/2/13 0013.
 */
@Component
public class parkServiceImpl implements ParkService {

    @Autowired
    private ParkMapper parkMapper;

    /**
     * 获取停车场信息
     *
     * @return
     */
    @Override
    public List<Park> getPark() {

        List<Park> parkInfo = parkMapper.getParkInfo();
        return parkInfo;
    }

    @Override
    public int addOrupdateParkInfo(Park park) {
        List<Park> list = parkMapper.getParkInfo();
        if(list.size()>0)park.setCharge(list.get(0).getCharge());
        return parkMapper.addOrUpdatePark(park);
    }

    @Override
    public int addOrUpdateParkForCharge(Park park) {
        List<Park> list = parkMapper.getParkInfo();
        Park submitPark = new Park() ;
        if(list.size()>0) {
            submitPark = list.get(0);
        }
        submitPark.setCharge(park.getCharge());
        return parkMapper.addOrUpdatePark(submitPark);
    }


    /**
     * 获取面板信息
     * L
     *
     * @return
     */
    @Override
    public List<PayPanel> getPayPanel(PageFilter pageFilter) {
        Map map = new HashMap();
        int start = (pageFilter.getPage() - 1) * pageFilter.getRows()<0?0:(pageFilter.getPage() - 1) * pageFilter.getRows();
        int end = pageFilter.getRows();
        map.put("startnum", start);
        map.put("endnum", end);
        List<PayPanel> list = parkMapper.getPayPanel(map);
        return list;
    }

    @Override
    public PayPanel getPayPanelById(String id) {
        return parkMapper.getPayPanelById(id);
    }

    @Override
    public int getPayPanelCount() {
        return parkMapper.getPayPanelCount();
    }

    @Override
    public int addPayPanel(PayPanel payPanel) {
        return parkMapper.addPayPanel(payPanel);
    }

    @Override
    public int editPayPanel(PayPanel payPanel) {
        return parkMapper.editPayPanel(payPanel);
    }

    @Override
    public int delPayPanel(String id) {
        Map map = new HashMap();
        map.put("payPanelId",id);
        if(parkMapper.getExitWayCountByCondi(map)>0) return -1;
        return parkMapper.delPayPanel(id);
    }

    /**
     * 获取出口信息
     *
     * @param pageFilter
     */
    @Override
    public List<Map> getExitWay(PageFilter pageFilter) {
        Map map = new HashMap();
        int start = (pageFilter.getPage() - 1) * pageFilter.getRows()<0?0:(pageFilter.getPage() - 1) * pageFilter.getRows();
        int end = pageFilter.getRows();
        map.put("startnum", start);
        map.put("endnum", end);
        List list = parkMapper.getExitWay(map);
        return list;
    }

    @Override
    public int getExitWayCount() {
        return parkMapper.getExitWayCount();
    }

    @Override
    public int addExitWay(ExitWay exitWay) {
        return parkMapper.addExitWay(exitWay);
    }

    @Override
    public Map getExitWayById(String id) {
        return parkMapper.getExitWayById(id);
    }

    @Override
    public int editExitWay(ExitWay exitWay) {
        return parkMapper.editExitWay(exitWay);
    }

    @Override
    public int delExitWay(String id) {
        return parkMapper.delExitWay(id);
    }

    /**
     * 获取流水信息
     */
    @Override
    public List<Bill> getBill(Map map) {
        return parkMapper.getBill(map);
    }

    @Override
    public int getBillCount(Map map) {
        return parkMapper.getBillCount(map);
    }

    /**
     * 云端信息设置
     */
    @Override
    public List<Map> getCloudSet() {
        return parkMapper.getCloudSet();
    }

    @Override
    public int addOrUpdateCloudSet(Map map){
        return parkMapper.addOrUpdateCloudSet(map);
    }

}
