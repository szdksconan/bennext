package com.unipay.benext.model.park;

import java.io.Serializable;

/**
 * 字典表
 * Created by Administrator on 2017/3/25.
 */
public class TerminalDic implements Serializable{
    private String key;
    private String value;

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
