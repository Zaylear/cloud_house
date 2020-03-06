package com.rbi.admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.dao.DivisonDAO;
import com.rbi.admin.dao.IdsDeleteDAO;
import com.rbi.admin.dao.structure.StructureDAO;
import com.rbi.admin.entity.edo.DivisonDO;

import com.rbi.admin.entity.dto.DivisonDTO;
import com.rbi.admin.entity.dto.structure.DivisonCodeDTO;
import com.rbi.admin.util.DateUtil;
import com.rbi.admin.util.EncapsulationTreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DivisonService {
    @Autowired(required = false)
    DivisonDAO divisonDAO;
    @Autowired(required = false)
    IdsDeleteDAO idsDeleteDAO;
    @Autowired(required = false)
    StructureDAO structureDAO;

    public List<DivisonDTO> findTree() throws Exception{
        List<DivisonDO> divisonDOS = divisonDAO.findAll();
        List<DivisonDTO> divisonDTOS = JSONObject.parseArray(JSONArray.toJSON(divisonDOS).toString(), DivisonDTO.class);
        return EncapsulationTreeUtil.getTree(divisonDTOS,"divisonCode","pid","divisonDTO");
    }

    public void addDivison(DivisonDO divisonDO){
        divisonDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        divisonDAO.save(divisonDO);
    }

    public void updateDivison(DivisonDO divisonDO){
        divisonDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        divisonDAO.saveAndFlush(divisonDO);
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
        String ids = "(" + str + ")";
        idsDeleteDAO.deleteDivisonByIds(ids);
    }

    public List<DivisonCodeDTO> findDivisonCode(){
        List<DivisonCodeDTO> divisonCodeDTOS = structureDAO.findDivisonCode();
        return divisonCodeDTOS;
    }

    public List<DivisonCodeDTO> findDivisonCodeNotId(int id){
        List<DivisonCodeDTO> divisonCodeDTOS = structureDAO.findDivisonCodeNotId(id);
        return divisonCodeDTOS;
    }

}
