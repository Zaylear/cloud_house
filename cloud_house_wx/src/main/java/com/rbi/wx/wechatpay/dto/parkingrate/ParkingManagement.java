package com.rbi.wx.wechatpay.dto.parkingrate;

import java.io.Serializable;

public class ParkingManagement implements Serializable {
    private String contractNumber;
    private String licensePlateNumber;
    private String surname;
    private String parkingSpaceCode;
    private String address;
    private String organizationId;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getParkingSpaceCode() {
        return parkingSpaceCode;
    }

    public void setParkingSpaceCode(String parkingSpaceCode) {
        this.parkingSpaceCode = parkingSpaceCode;
    }
}
