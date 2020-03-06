package com.rbi.wx.wechatpay.dto.houseinfo;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 房屋基本信息的实体类
 * @param <T>
 * @param <S>
 */
@ApiModel(value = "房屋基本信息实体")
public class HouseInfoDTO<T,S>implements Serializable{
    private S HouseInfo;

    public List getChargeMaxTime() {
        return chargeMaxTime;
    }

    public void setChargeMaxTime(List chargeMaxTime) {
        this.chargeMaxTime = chargeMaxTime;
    }
//    @ApiModelProperty(value = "业主的列表",dataType = "String",example = "null")
//    private List<T> propertyList;
//    @ApiModelProperty(value = "租客的列表",dataType = "String",example = "null")
//    private List<T> tenantList;
    @ApiModelProperty(value = "各项费用到期时间的列表",dataType = "String",example = "物业费到期时间:2019-9-13")
    private List chargeMaxTime;

    public S getHouseInfo() {
        return HouseInfo;
    }

    public void setHouseInfo(S houseInfo) {
        HouseInfo = houseInfo;
    }

//    public List<T> getPropertyList() {
//        return propertyList;
//    }
//
//    public void setPropertyList(List<T> propertyList) {
//        this.propertyList = propertyList;
//    }
//
//    public List<T> getTenantList() {
//        return tenantList;
//    }
//
//    public void setTenantList(List<T> tenantList) {
//        this.tenantList = tenantList;
//    }
}
