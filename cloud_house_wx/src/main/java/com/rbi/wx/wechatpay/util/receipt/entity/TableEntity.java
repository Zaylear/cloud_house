package com.rbi.wx.wechatpay.util.receipt.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TableEntity implements Serializable{
    private String doDate;//抄表日期
    private String usrName;//用户名称
    private String printDate;//打印日期
    private String houseName;//大楼名称
    private String remark;//备注
    private String houseID;//房间代码
    private ArrayList<TableJson> taleList;
    public String getHouseID() {
        return houseID;
    }

    public void setHouseID(String houseID) {
        this.houseID = houseID;
    }



    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDoDate() {
        return doDate;
    }

    public void setDoDate(String doDate) {
        this.doDate = doDate;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getPrintDate() {
        return printDate;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public ArrayList<TableJson> getTaleList() {
        return taleList;
    }

    public void setTaleList(ArrayList<TableJson> taleList) {
        this.taleList = taleList;
    }


}
