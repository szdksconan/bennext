package com.unipay.benext.model.park;

import com.unipay.benext.model.BaseModel;

import java.util.List;

/**
 * 流水信息
 * Created by LiuY on 2017/3/2 0002.
 */
public class Bill extends BaseModel{
    private String billNo;//流水号

    private String carNo;//车牌号

    private String parkName;//停车场名

    private String parkId;//停车场ID

    private String exitWayName;//出口名称

    private String exitWayId;//出口ID

    private String payPanelId;//支付面板ID

    private String payPanelName;//支付面板名称

    private String clientId;//商户ID

    private String stopTime;//停车时常

    private String exitTime;//exitTime

    private double totalPrice;//totalPrice

    private String cardNo;//卡号

    private String rightsType;//权益类型

    private String isRights;//是否首次使用权益 0-否 1-是

    private String uploadTag;//上传标记

    private Integer terminalId;//终端编号

    public Integer getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }

    private List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
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

    public String getIsRights() {
        return isRights;
    }

    public void setIsRights(String isRights) {
        this.isRights = isRights;
    }

    public String getUploadTag() {
        return uploadTag;
    }

    public void setUploadTag(String uploadTag) {
        this.uploadTag = uploadTag;
    }
}
