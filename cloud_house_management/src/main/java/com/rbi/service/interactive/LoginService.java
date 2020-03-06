package com.rbi.service.interactive;


import com.alibaba.fastjson.JSONObject;
import com.rbi.entity.LogLoginDO;
import com.rbi.util.PageData;

import java.util.Map;

public interface LoginService {
    Map<String,Object> login(String username, String password, String module,String IP) throws Exception;

    void logout(String userId,String ipAdress);

    Map<String,Object> loginAdmin(String username, String password, String module) throws Exception;

    void automaticLogOff();

    PageData<LogLoginDO> findLoginLogByOrganizationId(JSONObject jsonObject, String userId);
}
