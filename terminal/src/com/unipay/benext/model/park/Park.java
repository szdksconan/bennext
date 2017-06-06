package com.unipay.benext.model.park;

import com.unipay.benext.model.BaseModel;

/**
 * Created by liuh on 2017/2/13 0013.
 */
public class Park extends BaseModel {

    /**
     * 停车场名称
     */
    private String parkName;
    /**
     * 商户ID
     */
    private String parkId;
    /**
     * 商户Id
     */
    private String supplyId;
    /**
     *商户name

     */
    private String supplyName;

    private double charge;

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
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

    public String getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(String supplyId) {
        this.supplyId = supplyId;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }




}
