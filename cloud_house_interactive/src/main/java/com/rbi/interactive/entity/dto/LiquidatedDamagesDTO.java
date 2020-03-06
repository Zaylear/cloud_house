package com.rbi.interactive.entity.dto;

import java.io.Serializable;

public class LiquidatedDamagesDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String dueTimeFront;
    private String dueTimeAfter;
    private Double amountMoney;
    private Integer days;

    public String getDueTimeFront() {
        return dueTimeFront;
    }

    public void setDueTimeFront(String dueTimeFront) {
        this.dueTimeFront = dueTimeFront;
    }

    public String getDueTimeAfter() {
        return dueTimeAfter;
    }

    public void setDueTimeAfter(String dueTimeAfter) {
        this.dueTimeAfter = dueTimeAfter;
    }

    public Double getAmountMoney() {
        return amountMoney;
    }

    public void setAmountMoney(Double amountMoney) {
        this.amountMoney = amountMoney;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "LiquidatedDamagesDTO{" +
                "dueTimeFront='" + dueTimeFront + '\'' +
                ", dueTimeAfter='" + dueTimeAfter + '\'' +
                ", amountMoney=" + amountMoney +
                ", days=" + days +
                '}';
    }
}
