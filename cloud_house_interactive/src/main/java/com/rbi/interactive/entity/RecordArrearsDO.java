package com.rbi.interactive.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "record_arrears")
public class RecordArrearsDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id",columnDefinition = "varchar(50) comment '订单Id'")
    private String orderId;

    @Column(name = "toll_collector_id",columnDefinition = "varchar(50) comment '收费人编号，当前收费客户人员的UserId'")
    private String tollCollectorId;

    @Column(name = "payment_type",columnDefinition = "tinyint(4) comment '费用类型：1 物业费，2'")
    private String paymentType;

    @Column(name = "state_of_arrears",columnDefinition = "tinyint(4) comment '欠费状态：0 未欠费，1 欠费；默认：0'")
    private Integer stateOfArrears;

    @Column(name = "invalid_state",columnDefinition = "tinyint(4) comment '失效状态：1 失效，0 有效；默认：0'")
    private Integer invalidState;

    @Column(name = "village_code",columnDefinition = "varchar(50) comment '小区编号'")
    private String villageCode;

    @Column(name = "village_name",columnDefinition = "varchar(50) comment '小区名称'")
    private String villageName;

    @Column(name = "region_code",columnDefinition = "varchar(20) comment '地块编号'")
    private String regionCode;

    @Column(name = "region_name",columnDefinition = "varchar(50) comment '地块名称'")
    private String regionName;

    @Column(name = "building_code",columnDefinition = "varchar(20) comment '楼宇编号'")
    private String buildingCode;

    @Column(name = "building_name",columnDefinition = "varchar(50) comment '楼宇名称'")
    private String buildingName;

    @Column(name = "unit_code",columnDefinition = "varchar(20) comment '单元编号'")
    private String unitCode;

    @Column(name = "unit_name",columnDefinition = "varchar(20) comment '单元名称'")
    private String unitName;

    @Column(name = "user_id",columnDefinition = "varchar(20) comment '客户ID，唯一标识'")
    private String userId;

    @Column(name = "surname",columnDefinition = "varchar(4) comment '姓氏'")
    private String surname;

    @Column(name = "mobile_phone",columnDefinition = "varchar(20) comment '客户电话'")
    private String mobilePhone;

    @Column(name = "room_code",columnDefinition = "varchar(20) comment '房间编号'")
    private String roomCode;

    @Column(name = "room_size",columnDefinition = "float(20,4) comment '住房大小 单位：平方米'")
    private Double roomSize;

    @Column(name = "organization_id",columnDefinition = "varchar(50) comment '组织/机构ID'")
    private String organizationId;

    @Column(name = "organization_name",columnDefinition = "varchar(250) comment '组织/机构名称'")
    private String organizationName;

    @Column(name = "charge_code",columnDefinition = "varchar(50) comment '项目编号'")
    private String chargeCode;

    @Column(name = "charge_name",columnDefinition = "varchar(20) comment '项目名称'")
    private String chargeName;

    @Column(name = "amount_receivable",columnDefinition = "float(0) comment '应收金额'",scale = 2)
    private Double amountReceivable;

    @Column(name = "charge_unit",columnDefinition = "varchar(20) comment '收费单位:物业费：元/平方米  电费：元/度 车位：元/月'")
    private String chargeUnit;

    @Column(name = "charge_standard",columnDefinition = "double(20,4) comment '收费单价'")
    private Double chargeStandard;

    @Column(name = "usage_amount",columnDefinition = "double(20,4) comment '使用量'")
    private Double usageAmount;

    @Column(name = "current_readings",columnDefinition = "double(20,4) comment '当前读数'")
    private Double currentReadings;

    @Column(name = "last_reading",columnDefinition = "double(20,4) comment '上次读数'")
    private Double lastReading;

    @Column(name = "datedif",columnDefinition = "int(11) comment '缴费月数'")
    private Integer datedif;

    @Column(name = "discount",columnDefinition = "double(20,4) comment '折扣'")
    private Double discount;

    @Column(name = "start_time",columnDefinition = "date comment '开始计费时间'")
    private String startTime;

    @Column(name = "due_time",columnDefinition = "date comment '结束计费时间'")
    private String dueTime;

    @Column(name = "remark",columnDefinition = "text comment '备注'")
    private String remark;

    @Column(name = "idt",columnDefinition = "datetime(0) comment '新增时间，也是实收时间'")
    private String idt;

    @Column(name = "udt",columnDefinition = "datetime(0) comment '更新时间'")
    private String udt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTollCollectorId() {
        return tollCollectorId;
    }

    public void setTollCollectorId(String tollCollectorId) {
        this.tollCollectorId = tollCollectorId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getStateOfArrears() {
        return stateOfArrears;
    }

    public void setStateOfArrears(Integer stateOfArrears) {
        this.stateOfArrears = stateOfArrears;
    }

    public Integer getInvalidState() {
        return invalidState;
    }

    public void setInvalidState(Integer invalidState) {
        this.invalidState = invalidState;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public Double getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(Double roomSize) {
        this.roomSize = roomSize;
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

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public Double getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(Double amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public String getChargeUnit() {
        return chargeUnit;
    }

    public void setChargeUnit(String chargeUnit) {
        this.chargeUnit = chargeUnit;
    }

    public Double getChargeStandard() {
        return chargeStandard;
    }

    public void setChargeStandard(Double chargeStandard) {
        this.chargeStandard = chargeStandard;
    }

    public Double getUsageAmount() {
        return usageAmount;
    }

    public void setUsageAmount(Double usageAmount) {
        this.usageAmount = usageAmount;
    }

    public Double getCurrentReadings() {
        return currentReadings;
    }

    public void setCurrentReadings(Double currentReadings) {
        this.currentReadings = currentReadings;
    }

    public Double getLastReading() {
        return lastReading;
    }

    public void setLastReading(Double lastReading) {
        this.lastReading = lastReading;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIdt() {
        return idt;
    }

    public void setIdt(String idt) {
        this.idt = idt;
    }

    public String getUdt() {
        return udt;
    }

    public void setUdt(String udt) {
        this.udt = udt;
    }
}
