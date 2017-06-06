package com.unipay.benext.model.basic;


import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/19 0019.
 */
public class RequestModel implements Serializable {
    private static final long serialVersionUID = -1L;
    private int rows = 20;
    private int page = 1;
    private String sort = "id";
    private String desc = "desc";
    private String parkName;
    private String supplyName;
    private String parkId;
    private String supplyId;
    private  String  orderSql;


    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }


    public String getOrderString(){
        if(page == 0){
            return " order by "+sort+" "+desc+" limit 0,"+rows;
        }
        else{
            return " order by "+sort+" "+desc+" limit "+((page-1)*rows)+","+rows;
        }

    }

    public String getLimit(){
        if(page == 0){
            return " limit 0,"+rows;
        }
        else{
            return " limit "+((page-1)*rows)+","+rows;
        }
    }

    public String getOrderSql() {
        return orderSql;
    }

    public void setOrderSql(String orderSql) {
        this.orderSql = orderSql;
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
}
