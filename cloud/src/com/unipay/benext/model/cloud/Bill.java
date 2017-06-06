package com.unipay.benext.model.cloud;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/13.
 */
public class Bill implements Serializable{
    private static final long serialVersionUID = 1L;

    private String id;
    private String billNo;
    private String carNo;
    private String parkName;
    private String parkId;
    private String exitWayName;
    private String exitWayId;
    private String payPanelId;
    private String payPanelName;
    private String clientId;
    private String stopTime;
    private String exitTime;
    private String totalPrice;
    private String cardNo;
    private String rightsType;
    private String createTime;
    private String creator;
    private String updateTime;
    private String updator;
    private String uploadTag;
    private String isRights;
    private Integer terminalId;//终端编号

    public Integer getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }

    public String getIsRights() {
        return isRights;
    }

    public void setIsRights(String isRights) {
        this.isRights = isRights;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getCarNo() {
        return carNo;
    }
    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getParkName() {
        return parkName;
    }
    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getParkId() {
        return parkId;
    }
    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getExitWayName() {
        return exitWayName;
    }
    public void setExitWayName(String exitWayName) {
        this.exitWayName = exitWayName;
    }

    public String getExitWayId() {
        return exitWayId;
    }
    public void setExitWayId(String exitWayId) {
        this.exitWayId = exitWayId;
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

    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getStopTime() {
        return stopTime;
    }
    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getExitTime() {
        return exitTime;
    }
    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

    public String getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCardNo() {
        return cardNo;
    }
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getRightsType() {
        return rightsType;
    }
    public void setRightsType(String rightsType) {
        this.rightsType = rightsType;
    }

    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdator() {
        return updator;
    }
    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public String getUploadTag() {
        return uploadTag;
    }
    public void setUploadTag(String uploadTag) {
        this.uploadTag = uploadTag;
    }
}
