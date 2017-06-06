package com.unipay.benext.model.cloud;

/**
 *
 * Created by LiuY on 2017/2/17 0017.
 */
public class ParkSupply {

    private String parkId;//停车场ID

    private String parkName;

    private String supplyId;//商户ID

    private String  supplyName;

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
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
