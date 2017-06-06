package com.unipay.benext.model.basic;

import java.io.Serializable;

/**
 * Created by WEI on 2017/2/22.
 */
public class ResponseModel implements Serializable{
    private static final long serialVersionUID = -1L;
    private int code;
    private Data data;
    private String info;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
