package com.unipay.benext.model.basic;


import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/15 0015.
 */
public class Rights implements Serializable{
    private static final long serialVersionUID = -1L;
    @Id
    private int id;
    private long cardNo;//卡片信息
    private String rightsType;//权益类型
    private int rightsCount;//剩余次数
    private int PayRule;//付款规则
    private int rightsMaxCount;//最大次数
    private Date updateTime;//同步时间


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCardNo() {
        return cardNo;
    }

    public void setCardNo(long cardNo) {
        this.cardNo = cardNo;
    }

    public String getRightsType() {
        return rightsType;
    }

    public void setRightsType(String rightsType) {
        this.rightsType = rightsType;
    }

    public int getRightsCount() {
        return rightsCount;
    }

    public void setRightsCount(int rightsCount) {
        this.rightsCount = rightsCount;
    }

    public int getPayRule() {
        return PayRule;
    }

    public void setPayRule(int payRule) {
        PayRule = payRule;
    }

    public int getRightsMaxCount() {
        return rightsMaxCount;
    }

    public void setRightsMaxCount(int rightsMaxCount) {
        this.rightsMaxCount = rightsMaxCount;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
