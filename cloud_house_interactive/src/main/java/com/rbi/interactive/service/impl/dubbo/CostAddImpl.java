package com.rbi.interactive.service.impl.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.rbi.common.interactive.CostAdd;
import com.rbi.interactive.service.FrontOfficeCashierService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CostAddImpl implements CostAdd {

    @Autowired
    FrontOfficeCashierService frontOfficeCashierService;

    @Override
    public Boolean addBill(JSONObject jsonObject, String userId) {
        try {
            frontOfficeCashierService.addBill(jsonObject,userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
