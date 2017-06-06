package com.unipay.benext.model.cloud;

import java.io.Serializable;

/**
 * 停车场
 * Created by Administrator on 2017/3/4.
 */
public class Park implements Serializable{
    private static final long serialVersionUID = -1L;

    private Long id;
    private String parkName;
    private String clientId;
    private String creator;
    private String updator;
    private String createTime;
    private String updateTime;
    private String useTag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUseTag() {
        return useTag;
    }

    public void setUseTag(String useTag) {
        this.useTag = useTag;
    }
}
