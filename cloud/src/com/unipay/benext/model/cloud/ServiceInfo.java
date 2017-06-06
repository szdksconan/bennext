package com.unipay.benext.model.cloud;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LiuY on 2017/3/3 0003.
 */
public class ServiceInfo implements Serializable {
    private static final long serialVersionUID = -1L;

    String id;

    String name;

    String terminalCode;

    String clientId;

    Date updateTime;

    int stat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }
}
