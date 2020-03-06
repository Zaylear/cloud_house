package com.rbi.common.interactive;

import com.alibaba.fastjson.JSONObject;

public interface CostAdd {

    Boolean addBill(JSONObject jsonObject, String userId) throws Exception;

}
