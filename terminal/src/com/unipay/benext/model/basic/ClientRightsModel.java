package com.unipay.benext.model.basic;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/19 0019.
 */
public class ClientRightsModel implements Serializable{
    private static final long serialVersionUID = -1L;
    @Id
    private int id;
    private String cilentId;
    private String rightsType;
    private String clientName;
    private int effectType;
    private int payRule;
    private int deductionHours;
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCilentId() {
        return cilentId;
    }

    public void setCilentId(String cilentId) {
        this.cilentId = cilentId;
    }

    public String getRightsType() {
        return rightsType;
    }

    public void setRightsType(String rightsType) {
        this.rightsType = rightsType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getEffectType() {
        return effectType;
    }

    public void setEffectType(int effectType) {
        this.effectType = effectType;
    }

    public int getPayRule() {
        return payRule;
    }

    public void setPayRule(int payRule) {
        this.payRule = payRule;
    }

    public int getDeductionHours() {
        return deductionHours;
    }

    public void setDeductionHours(int deductionHours) {
        this.deductionHours = deductionHours;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
