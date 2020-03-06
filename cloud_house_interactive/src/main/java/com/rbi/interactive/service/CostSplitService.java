package com.rbi.interactive.service;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.entity.BillDetailedDO;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

/**
 * 费用拆分
 */
public interface CostSplitService {

    List<BillDetailedDO> costSplit(JSONObject jsonObject) throws ParseException, InvocationTargetException, IllegalAccessException;

}
