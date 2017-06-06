package com.unipay.benext.model.park;

import com.unipay.benext.model.BaseModel;

/**
 * Created by LiuY on 2017/2/16 0016.
 */
public class ExitWay extends BaseModel{

    private String oldId;

    public String getOldId() {
        return oldId;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }

    private String exitWayName;

    private String parkId;

    private String payPanelId;

    private String payPanelName;

    private String payPanelCode;

    private String delTag;

    private String parkName;

    public String getExitWayName() {
        return exitWayName;
    }

    public void setExitWayName(String exitWayName) {
        this.exitWayName = exitWayName;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getPayPanelId() {
        return payPanelId;
    }

    public void setPayPanelId(String payPanelId) {
        this.payPanelId = payPanelId;
    }

    public String getPayPanelName() {
        return payPanelName;
    }

    public void setPayPanelName(String payPanelName) {
        this.payPanelName = payPanelName;
    }

    public String getPayPanelCode() {
        return payPanelCode;
    }

    public void setPayPanelCode(String payPanelCode) {
        this.payPanelCode = payPanelCode;
    }

    public String getDelTag() {
        return delTag;
    }

    public void setDelTag(String delTag) {
        this.delTag = delTag;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

}
