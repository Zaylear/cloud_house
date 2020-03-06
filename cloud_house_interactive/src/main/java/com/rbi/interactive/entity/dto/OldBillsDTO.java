package com.rbi.interactive.entity.dto;

import java.io.Serializable;

public class OldBillsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderId;
    private String roomCode;
    private String mobilePhone;
    private Double amountTotalReceivable;
    private Double actualTotalMoneyCollection;
    private Double surplus;
    private String payerName;
    private String payerPhone;
    private String paymentMethod;

    private String organizationId;
    private String organizationName;

    private String chargeCode;
    private String startTime;
    private Integer datedif;
    private Double discount;
    private Double lastReading;
    private Double currentReadings;
    private Double usageAmount;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Double getAmountTotalReceivable() {
        return amountTotalReceivable;
    }

    public void setAmountTotalReceivable(Double amountTotalReceivable) {
        this.amountTotalReceivable = amountTotalReceivable;
    }

    public Double getActualTotalMoneyCollection() {
        return actualTotalMoneyCollection;
    }

    public void setActualTotalMoneyCollection(Double actualTotalMoneyCollection) {
        this.actualTotalMoneyCollection = actualTotalMoneyCollection;
    }

    public Double getSurplus() {
        return surplus;
    }

    public void setSurplus(Double surplus) {
        this.surplus = surplus;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerPhone() {
        return payerPhone;
    }

    public void setPayerPhone(String payerPhone) {
        this.payerPhone = payerPhone;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getDatedif() {
        return datedif;
    }

    public void setDatedif(Integer datedif) {
        this.datedif = datedif;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getLastReading() {
        return lastReading;
    }

    public void setLastReading(Double lastReading) {
        this.lastReading = lastReading;
    }

    public Double getCurrentReadings() {
        return currentReadings;
    }

    public void setCurrentReadings(Double currentReadings) {
        this.currentReadings = currentReadings;
    }

    public Double getUsageAmount() {
        return usageAmount;
    }

    public void setUsageAmount(Double usageAmount) {
        this.usageAmount = usageAmount;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
