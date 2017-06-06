package com.unipay.benext.model.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/21.
 */
public class RightsCount implements Serializable {
    private static final long serialVersionUID = -1L;

    private String rightsId;//权益ID
    private String rank;//卡级别
    private int count;//上限次数

    public String getRightsId() {
        return rightsId;
    }
    public void setRightsId(String rightsId) {
        this.rightsId = rightsId;
    }

    public String getRank() {
        return rank;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
