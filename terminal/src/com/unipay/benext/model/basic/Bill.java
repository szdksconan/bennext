package com.unipay.benext.model.basic;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/13.
 */
public class Bill implements Serializable{
    private static final long serialVersionUID = 1L;

    private int id;
    private String billNo;
    private String carNo;
    private String parkName;
    private int parkId;
    private String exitWayName;
    private int exitWayId;
    private int payPanelId;
    private String payPanelName;
    private int clientId;
    private Double stopTime;
    private Date exitTime;
    private String totalPrice;
    private String cardNo;
    private String rightsType;
    private Date createTime;
    private String creator;
    private Date updateTime;
    private String updator;
    private String uploadTag;
    private Integer terminalId;//终端编号

    public Integer getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }

    public String getExitWayName() {
        return exitWayName;
    }

    public void setExitWayName(String exitWayName) {
        this.exitWayName = exitWayName;
    }

    public int getExitWayId() {
        return exitWayId;
    }

    public void setExitWayId(int exitWayId) {
        this.exitWayId = exitWayId;
    }

    public int getPayPanelId() {
        return payPanelId;
    }

    public void setPayPanelId(int payPanelId) {
        this.payPanelId = payPanelId;
    }

    public String getPayPanelName() {
        return payPanelName;
    }

    public void setPayPanelName(String payPanelName) {
        this.payPanelName = payPanelName;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }


    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }


    public String getRightsType() {
        return rightsType;
    }

    public void setRightsType(String rightsType) {
        this.rightsType = rightsType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Double getStopTime() {
        return stopTime;
    }

    public void setStopTime(Double stopTime) {
        this.stopTime = stopTime;
    }
}