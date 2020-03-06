package com.rbi.wx.wechatpay.util.jsonobjectword.jsonobjectentity;

import java.io.Serializable;

public class PageLimitUtil implements Serializable{
    private String PageNum;
    private String pageSize;

    public String getPageNum() {
        return PageNum;
    }

    public void setPageNum(String pageNum) {
        PageNum = pageNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
