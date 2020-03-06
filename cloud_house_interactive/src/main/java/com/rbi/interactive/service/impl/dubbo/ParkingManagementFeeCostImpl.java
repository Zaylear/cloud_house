package com.rbi.interactive.service.impl.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.rbi.common.interactive.ParkingManagementFeeCost;
import com.rbi.interactive.abnormal.ParkingSpaceDueTimeException;
import com.rbi.interactive.service.FrontOfficeCashierService;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.ConnectException;
import java.text.ParseException;

@Service
public class ParkingManagementFeeCostImpl implements ParkingManagementFeeCost {

    @Autowired
    FrontOfficeCashierService frontOfficeCashierService;


    @Override
    public JSONObject parkingManagementFee(JSONObject jsonObject, String organizationId) {
        try {
            JSONObject json = new JSONObject();
            json.put("status","OK");
            json.put("data",frontOfficeCashierService.parkingManagementFeeCost(jsonObject,organizationId));
            return json;
        } catch (ParkingSpaceDueTimeException e) {
            JSONObject json = new JSONObject();
            json.put("status","ERROR");
            json.put("data","查询到期时间失败");
            return json;
        } catch (ParseException e) {
            JSONObject json = new JSONObject();
            json.put("status","ERROR");
            json.put("data","时间计算错误");
            return json;
        } catch (ConnectException e) {
            JSONObject json = new JSONObject();
            json.put("status","ERROR");
            json.put("data","服务器处理失败");
            return json;
        }
    }
}
