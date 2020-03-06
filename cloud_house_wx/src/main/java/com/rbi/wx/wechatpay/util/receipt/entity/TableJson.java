package com.rbi.wx.wechatpay.util.receipt.entity;

import java.io.Serializable;

public class TableJson implements Serializable{
    private String name;//项目名称
    private String houseSize;//建筑面积
    private String lastMonth;//上月读数
    private String month;//本月读数
    private String monthDosage;//本月用量
    private double price;//单价
    private String priceDate;//计费时间
    private double monthPrice;//本月费用
    private double lastMonthPrice;//往月欠交
    private double sum;//应交合计

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHouseSize() {
        return houseSize;
    }

    public void setHouseSize(String houseSize) {
        this.houseSize = houseSize;
    }

    public String getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(String lastMonth) {
        this.lastMonth = lastMonth;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonthDosage() {
        return monthDosage;
    }

    public void setMonthDosage(String monthDosage) {
        this.monthDosage = monthDosage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(String priceDate) {
        this.priceDate = priceDate;
    }

    public double getMonthPrice() {
        return monthPrice;
    }

    public void setMonthPrice(double monthPrice) {
        this.monthPrice = monthPrice;
    }

    public double getLastMonthPrice() {
        return lastMonthPrice;
    }

    public void setLastMonthPrice(double lastMonthPrice) {
        this.lastMonthPrice = lastMonthPrice;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }


}
