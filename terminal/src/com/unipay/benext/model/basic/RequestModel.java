package com.unipay.benext.model.basic;

import java.io.Serializable;

/**
 * Created by WEI on 2017/2/21.
 */
public class RequestModel implements Serializable{
    private static final long serialVersionUID = -1L;
    private String carPlate;
    private int outId;
    private String inTime;
    private String outTime;
    private double parkTime;
    private String deal_ID;
    private double fee;
    private String breakDeal;

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public int getOutId() {
        return outId;
    }

    public void setOutId(int outId) {
        this.outId = outId;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public double getParkTime() {
        return parkTime;
    }

    public void setParkTime(double parkTime) {
        this.parkTime = parkTime;
    }

    public String getDeal_ID() {
        return deal_ID;
    }

    public void setDeal_ID(String deal_ID) {
        this.deal_ID = deal_ID;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getBreakDeal() {
        return breakDeal;
    }

    public void setBreakDeal(String breakDeal) {
        this.breakDeal = breakDeal;
    }
}
