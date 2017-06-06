package com.unipay.benext.model.basic;

import java.io.Serializable;

/**
 * Created by WEI on 2017/3/17.
 */
public class Data implements Serializable {
    private static final long serialVersionUID = -1L;
    private String carPlate;
    private double fee;
    private String deal_ID;
    private String failReason;
    private int outId;

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getDeal_ID() {
        return deal_ID;
    }

    public void setDeal_ID(String deal_ID) {
        this.deal_ID = deal_ID;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public int getOutId() {
        return outId;
    }

    public void setOutId(int outId) {
        this.outId = outId;
    }
}
