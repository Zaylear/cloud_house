package com.rbi.wx.wechatpay.util.tableutil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ExcelEntity implements Serializable{
   private HashMap<String,String> columName; //列名键值对
   private List<HashMap<String,String>> columValue;//列里面的值键值对集合

    public HashMap<String, String> getColumName() {
        return columName;
    }

    public void setColumName(HashMap<String, String> columName) {
        this.columName = columName;
    }

    public List<HashMap<String, String>> getColumValue() {
        return columValue;
    }

    public void setColumValue(List<HashMap<String, String>> columValue) {
        this.columValue = columValue;
    }
}
