package com.rbi.admin.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.dao.DepartmentDAO;
import com.rbi.admin.dao.IdsDeleteDAO;
import com.rbi.admin.entity.edo.DepartmentDO;
import com.rbi.admin.entity.dto.Department2DTO;
import com.rbi.admin.util.DateUtil;
import com.rbi.admin.util.EncapsulationTreeUtil;
import com.rbi.admin.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {
    @Autowired(required = false)
    DepartmentDAO departmentDAO;

    @Autowired(required = false)
    IdsDeleteDAO idsDeleteDAO;

    public void addDepartment(DepartmentDO departmentDO){
        departmentDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        departmentDAO.save(departmentDO);
    }

    public void deleteDepartment(int id){
        departmentDAO.deleteById(id);
    }

    public void updateDepartment(DepartmentDO departmentDO){
        departmentDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        departmentDAO.saveAndFlush(departmentDO);
    }

    public DepartmentDO findById(int id){
        DepartmentDO departmentDO = departmentDAO.findById(id);
        return departmentDO;
    }

    public PageData findByPage(int pageNum, int pageSize){
        int pageNo = pageSize * (pageNum - 1);
        List<DepartmentDO> departmentDOS  = departmentDAO.findByPage(pageNo,pageSize);
        for (int i = 0; i<departmentDOS.size();i++){
            if (null != departmentDOS.get(i).getPid() || !"".equals(departmentDOS.get(i).getPid())){
                String pname = departmentDAO.findNameByOrganizationId(departmentDOS.get(i).getPid());
                departmentDOS.get(i).setPname(pname);
            }
        }
        int totalPage = 0;
        int count = departmentDAO.findNum();
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum,pageSize,totalPage,count,departmentDOS);
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
        idsDeleteDAO.deleteDepartmentByIds(ids);
    }

    public List<Department2DTO> findTree(String organizationId) throws Exception{
        List<DepartmentDO> departmentDOS = departmentDAO.findTree(organizationId);
        List<Department2DTO> department2DTOS = JSONObject.parseArray(JSONArray.toJSON(departmentDOS).toString(), Department2DTO.class);
        return  EncapsulationTreeUtil.getTree(department2DTOS,"deptId","pid","department2DTO");

    }

    public int findNum(String deptId){
        return departmentDAO.countByDeptId(deptId);
    }

}
