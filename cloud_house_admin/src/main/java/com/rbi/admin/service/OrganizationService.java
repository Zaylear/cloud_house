package com.rbi.admin.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.dao.IdsDeleteDAO;
import com.rbi.admin.dao.OrganizationDAO;
import com.rbi.admin.dao.TreeDAO;
import com.rbi.admin.entity.dto.Organization2DTO;
import com.rbi.admin.entity.dto.OrganizationDTO;
import com.rbi.admin.entity.edo.OrganizationDO;
import com.rbi.admin.util.DateUtil;
import com.rbi.admin.util.EncapsulationTreeUtil;
import com.rbi.admin.util.PageData;
import com.rbi.admin.util.PinYin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationService {
    @Autowired
    OrganizationDAO organizationDAO;
    @Autowired(required = false)
    IdsDeleteDAO idsDeleteDAO;
    @Autowired(required = false)
    TreeDAO testDao;


    public String addOrganization(OrganizationDO organizationDO){
        String organizationId = PinYin.getPinYinHeadChar(organizationDO.getOrganizationName()).toUpperCase();
        int num = organizationDAO.findIdNum(organizationId);
        if (num != 0){
            return "组织名重复";
        }
        organizationDO.setOrganizationId(organizationId);
        organizationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        organizationDAO.save(organizationDO);
        return "10000";
    }


    public void updateOrganization(OrganizationDO organizationDO){
        organizationDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        organizationDAO.saveAndFlush(organizationDO);
    }

    public OrganizationDO findById(int id){
        OrganizationDO organizationDO = organizationDAO.findById(id);
        return organizationDO;
    }


    public PageData findByPage(int pageNum,int pageSize){
        int pageNo = pageSize * (pageNum - 1);
        List<OrganizationDO> organizationDOS  = organizationDAO.findByPage(pageNo,pageSize);
        int totalPage = 0;
        int count = organizationDAO.findNum();
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum,pageSize,totalPage,count,organizationDOS);
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
        idsDeleteDAO.deleteOrganizationByIds(ids);
    }

    public List<OrganizationDTO> findOrganizationId(){
        List<OrganizationDTO> organizationDTOS = organizationDAO.findOrganizationId();
        return organizationDTOS;
    }

    public List<Organization2DTO> findTree() throws Exception{
        List<Organization2DTO> organizationDOS = testDao.findTree();
        for (int i = 0; i<organizationDOS.size();i++){
            if (null != organizationDOS.get(i).getPid() || !"".equals(organizationDOS.get(i).getPid())){
                String pname = organizationDAO.findNameByOrganizationId(organizationDOS.get(i).getPid());
                organizationDOS.get(i).setPname(pname);
            }
        }
        List<Organization2DTO> organization2DTOS = JSONObject.parseArray(JSONArray.toJSON(organizationDOS).toString(), Organization2DTO.class);
        return EncapsulationTreeUtil.getTree(organization2DTOS,"organizationId","pid","organization2DTO");
    }

}
