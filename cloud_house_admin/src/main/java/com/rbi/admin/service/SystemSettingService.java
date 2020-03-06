package com.rbi.admin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.dao.SystemSettingDAO;
import com.rbi.admin.dao.connect.SystemSetDAO;
import com.rbi.admin.entity.edo.SystemSettingDO;
import com.rbi.admin.entity.dto.SystemSettingDTO;
import com.rbi.admin.entity.dto.structure.OrganizationStructureDTO;
import com.rbi.admin.util.DateUtil;
import com.rbi.admin.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SystemSettingService {

    @Autowired
    SystemSettingDAO systemSettingDAO;

    @Autowired(required = false)
    SystemSetDAO systemSetDAO;

    public void addSetting(SystemSettingDO systemSettingDO) {
        systemSettingDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        systemSettingDAO.save(systemSettingDO);
    }

    public void deleteSetting(int id) {
        systemSettingDAO.deleteById(id);
    }

    public void updateSetting(SystemSettingDO systemSettingDO) {
        systemSettingDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        systemSettingDAO.saveAndFlush(systemSettingDO);
    }


    public SystemSettingDO findById(int id) {
        SystemSettingDO systemSettingDO = systemSettingDAO.findById(id);
        return systemSettingDO;
    }

    public PageData findByPage(String organizationId, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<SystemSettingDO> systemSettingDOS = systemSettingDAO.findByPage(organizationId, pageNo, pageSize);
        List<SystemSettingDTO> systemSettingDTOS = systemSetDAO.findSettingType();
        for (int i = 0; i < systemSettingDOS.size(); i++) {
            for (int j = 0; j < systemSettingDTOS.size(); j++) {
                String type = systemSettingDOS.get(i).getSettingType();
                if (type.equals(systemSettingDTOS.get(j).getSettingCode())) {
                    String settingName = systemSettingDTOS.get(j).getSettingName();
                    System.out.println(settingName);
                    systemSettingDOS.get(i).setSettingType(settingName);
                }
            }
        }
        int totalPage = 0;
        int count = systemSettingDAO.findNum(organizationId);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, systemSettingDOS);
    }


    public String findOrganizationId(String userId) {
        String organizationId = systemSettingDAO.findOrganizationId(userId);
        return organizationId;
    }

    public OrganizationStructureDTO findOrganization(String userId) {
        OrganizationStructureDTO organizationStructureDTO = systemSettingDAO.findOrganization(userId);
        return organizationStructureDTO;
    }

    public List<SystemSettingDTO> findSettingType() {
        List<SystemSettingDTO> systemSettingDTOS = systemSetDAO.findSettingType();
        return systemSettingDTOS;
    }


    public void deleteByIds(JSONArray result) {
        String temp = "";
        List<String> list = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            JSONObject obj = (JSONObject) result.get(i);
            String id = obj.getString("id");
            temp = "'" + id + "'";
            list.add(temp);
        }
        String str = String.join(",", list);
        String ids = "("+str+")";
        systemSetDAO.deleteByIds(ids);
    }


    public void deleteByIdsA(JSONArray result) {
        String temp = "";
        List<String> list = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            JSONObject obj = (JSONObject) result.get(i);
            String id = obj.getString("id");
            temp = "'" + id + "'";
            list.add(temp);
        }
        String str = String.join(",", list);
        String ids = "("+str+")";
        systemSetDAO.deleteByIds(ids);
    }


    public List<SystemSettingDTO>findChoose(String settingType,String organizationId){
        List<SystemSettingDTO> systemSettingDTOS = systemSetDAO.findChoose(settingType,organizationId);
        return systemSettingDTOS;
    }

    public List<SystemSettingDTO>findChoose2(String settingType){
        List<SystemSettingDTO> systemSettingDTOS = systemSetDAO.findChoose2(settingType);
        return systemSettingDTOS;
    }


    public PageData findByPageA(int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<SystemSettingDO> systemSettingDOS = systemSettingDAO.findByPageA(pageNo,pageSize);
        int totalPage = 0;
        int count = systemSettingDAO.findNumA();
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNo, pageSize, totalPage, count,systemSettingDOS);
    }

    public Map<String,Object> findAdmChoose(JSONArray array){
        Map<String,Object> map = new HashMap<>();
        for (int i=0;i<array.size();i++){
            JSONObject json = JSON.parseObject(array.get(i).toString());
            String settingType = json.getString("settingType");
            List<SystemSettingDTO> systemSettingDTOS = systemSetDAO.findChoose2(settingType);
            map.put(settingType,systemSettingDTOS);
        }
        return map;
    }

    public Map<String,Object> findPaymentMethod(){
        Map<String,Object> map = new HashMap<>();
        String settingType = "PAYMENT_METHOD";
        List<SystemSettingDTO> systemSettingDTOS = systemSetDAO.findPaymentMethod(settingType);
        map.put(settingType,systemSettingDTOS);
        return map;
    }

    public Map<String,Object> findNatChoose(JSONArray array,String organizationId){
        Map<String,Object> map = new HashMap<>();
        for (int i=0;i<array.size();i++){
            JSONObject json = JSON.parseObject(array.get(i).toString());
            String settingType = json.getString("settingType");
            List<SystemSettingDTO> systemSettingDTOS = systemSetDAO.findChoose(settingType,organizationId);
            map.put(settingType,systemSettingDTOS);
        }
        return map;
    }

}
