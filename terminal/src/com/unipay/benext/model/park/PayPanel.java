package com.unipay.benext.model.park;

import com.unipay.benext.model.BaseModel;

/** 面板信息
 * Created by LiuY on 2017/2/15 0015.
 */
public class PayPanel extends BaseModel {

    /**面板名称*/
    private String payPanelName;
    /**机器码*/
    private String payPanelCode;
    /**IP*/
    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

}
