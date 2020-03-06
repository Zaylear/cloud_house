package com.rbi.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.dao.*;
import com.rbi.entity.*;
import com.rbi.entity.dto.PermitDTO;
import com.rbi.entity.dto.RoleAndPermitDTO;
import com.rbi.entity.dto.UserAndRoleDTO;
import com.rbi.service.PermissionConfigurationService;
import com.rbi.util.DateUtil;
import com.rbi.util.EncapsulationTreeUtil;
import com.rbi.util.PageData;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PermissionConfigurationServiceImpl implements PermissionConfigurationService {

    @Autowired
    PermitDAO permitDAO;

    @Autowired
    RoleDAO roleDAO;

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    UserAndRoleDAO userAndRoleDAO;

    @Autowired
    RoleAndPermitDAO roleAndPermitDAO;

    @Autowired(required = false)
    IUserAndRoleDAO iUserAndRoleDAO;

    @Autowired(required = false)
    IRoleAndPermitDAO iRoleAndPermitDAO;

    @Override
    public List<RoleDO> findAllByOrganizationId(String userId){
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        return roleDAO.findAllByOrganizationId(userInfoDO.getOrganizationId());
    }

    @Override
    public List<PermitDTO> findPermitAll() throws Exception {
        List<PermitDO> permitDOS = permitDAO.findByMenuPermisFlagNotOrderByParentCode(-1);
        List<PermitDTO> permitDTOS = JSONObject.parseArray(JSONArray.toJSON(permitDOS).toString(),PermitDTO.class);
        return EncapsulationTreeUtil.getTree(permitDTOS,"permisCode","parentCode","permitDTO");

    }

    @Override
    public List<PermitDTO> findAdminPermitAll() throws Exception {
        List<PermitDO> permitDOS = permitDAO.findAll();
        List<PermitDTO> permitDTOS = JSONObject.parseArray(JSONArray.toJSON(permitDOS).toString(),PermitDTO.class);
        return EncapsulationTreeUtil.getTree(permitDTOS,"permisCode","parentCode","permitDTO");
    }

    @Override
    public List<UserInfoDO> findUserInfoByOrganizationId(String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        List<UserInfoDO> userInfoDOS = userInfoDAO.findAllByOrganizationId(userInfoDO.getOrganizationId());
        return userInfoDOS;
    }

    @Override
    public void addUserInfoAndRole(JSONObject jsonObject) {
        List<String> roleCodes = new ArrayList<>(Arrays.asList(jsonObject.getString("roleCodes").split(",")));
        List<UserAndRoleDO> userAndRoleDOS = new ArrayList<>();
        for (String roleCode:roleCodes) {
            UserAndRoleDO userAndRoleDO = new UserAndRoleDO();
            userAndRoleDO.setRoleCode(roleCode);
            userAndRoleDO.setUserId(jsonObject.getString("userId"));
            String nowTime = DateUtil.date(DateUtil.FORMAT_PATTERN);
            userAndRoleDO.setIdt(nowTime);
            userAndRoleDO.setUdt(nowTime);
            userAndRoleDOS.add(userAndRoleDO);
        }
        userAndRoleDAO.saveAll(userAndRoleDOS);
    }

    @Override
    public void addRoleAndPermit(JSONObject jsonObject) {
        List<String> permisCodes = new ArrayList<>(Arrays.asList(jsonObject.getString("permisCodes").split(",")));
        List<RoleAndPermitDO> roleAndPermitDOS = new ArrayList<>();
        for (String permisCode:permisCodes) {
            RoleAndPermitDO roleAndPermitDO = new RoleAndPermitDO();
            roleAndPermitDO.setPermisCode(permisCode);
            roleAndPermitDO.setRoleCode(jsonObject.getString("roleCode"));
            String nowTime = DateUtil.date(DateUtil.FORMAT_PATTERN);
            roleAndPermitDO.setIdt(nowTime);
            roleAndPermitDO.setUdt(nowTime);
            roleAndPermitDOS.add(roleAndPermitDO);
            List<RoleAndPermitDO> roleAndPermitDOList = roleAndPermitDAO.findAllByRoleCodeAndPermisCode(roleAndPermitDO.getRoleCode(),roleAndPermitDO.getPermisCode());
            for (RoleAndPermitDO roleAndPermitDO1:roleAndPermitDOList) {
                roleAndPermitDAO.deleteById(roleAndPermitDO1.getId());
            }
        }
        roleAndPermitDAO.saveAll(roleAndPermitDOS);
    }

    @Override
    public PageData findUserAndRolePage(Integer pageNum,Integer pageSize,String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        Integer pageNo = pageSize*(pageNum-1);
        Integer count = iUserAndRoleDAO.findUserAndRoleCount(userInfoDO.getOrganizationId());
        Integer totalPage = 0;
        if (0==count%pageSize){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }
        List<UserAndRoleDTO> userAndRoleDTOS = iUserAndRoleDAO.findUserAndRolePage(userInfoDO.getOrganizationId(),pageNo,pageSize);
        return new PageData(pageNum,pageSize,totalPage,count,userAndRoleDTOS);
    }

    @Override
    public PageData findRoleAndPermitPage(Integer pageNum,Integer pageSize,String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        Integer pageNo = pageSize*(pageNum-1);
        Integer count = iRoleAndPermitDAO.findRoleAndPermitCount(userInfoDO.getOrganizationId());
        Integer totalPage = 0;
        if (0==count%pageSize){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }
        List<RoleAndPermitDTO> roleAndPermitDTOS = iRoleAndPermitDAO.findRoleAndPermitPage(userInfoDO.getOrganizationId(),pageNo,pageSize);
        return new PageData(pageNum,pageSize,totalPage,count,roleAndPermitDTOS);
    }

    @Override
    public void deleteRoleAndPermitByIds(List<Integer> ids) {
        roleAndPermitDAO.deleteByIdIn(ids);
    }

    @Override
    public void deleteRoleAndPermit(JSONArray jsonObjectList) {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0;i<jsonObjectList.size();i++) {
            String roleCode = JSONObject.parseObject(jsonObjectList.get(i).toString()).getString("roleCode");
            String permitCode = JSONObject.parseObject(jsonObjectList.get(i).toString()).getString("permisCode");
            List<RoleAndPermitDO> roleAndPermitDOList = roleAndPermitDAO.findAllByRoleCodeAndPermisCode(roleCode,permitCode);
            for (RoleAndPermitDO roleAndPermitDO:roleAndPermitDOList){
                if (null!=roleAndPermitDO){
                    ids.add(roleAndPermitDO.getId());
                }
            }
        }
        if (ids.size()>0){
            deleteRoleAndPermitByIds(ids);
        }
    }

    @Override
    public void deleteUserAndRoleByIds(List<Integer> ids) {
        userAndRoleDAO.deleteByIdIn(ids);
    }

    @Override
    public List<RoleAndPermitDTO> findPermitByRole(String roleCode) {
        List<RoleAndPermitDTO> roleAndPermitDTOS = iRoleAndPermitDAO.findRoleAndPermitByRoleCode(roleCode);
//        List<String> permisCodes = new ArrayList<>();
//        for (RoleAndPermitDTO roleAndPermitDTO: roleAndPermitDTOS) {
//            permisCodes.add(roleAndPermitDTO.getPermisCode());
//        }
//        List<PermitDO> permitDOS = permitDAO.findAllByPermisCodeIn(permisCodes);
//        Map<String,Object> map = new HashMap<>();
//        map.put("roleAndPermitDTOS",roleAndPermitDTOS);
//        map.put("permitDOS",permitDOS);
//        return map;
        return roleAndPermitDTOS;
    }

    @Override
    public List<UserAndRoleDTO> findRoleByUserId(String userId) {

        return iUserAndRoleDAO.findUserAndRoleByUserId(userId);
    }

    @Override
    public List<RoleDO> findRoleAllByOrganizationId(String organizationId) {
        return roleDAO.findAllByOrganizationId(organizationId);
    }

    @Override
    public List<UserInfoDO> findAdminUserInfoByOrganizationId(String organizationId) {
        List<UserInfoDO> userInfoDOS = userInfoDAO.findAllByOrganizationId(organizationId);
        return userInfoDOS;
    }

    @Override
    public PageData findAdminUserAndRolePage(int pageNum, int pageSize, String organizationId) {
        Integer pageNo = pageSize*(pageNum-1);
        Integer count = iUserAndRoleDAO.findUserAndRoleCount(organizationId);
        Integer totalPage = 0;
        if (0==count%pageSize){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }
        List<UserAndRoleDTO> userAndRoleDTOS = iUserAndRoleDAO.findUserAndRolePage(organizationId,pageNo,pageSize);
        return new PageData(pageNum,pageSize,totalPage,count,userAndRoleDTOS);
    }

    @Override
    public PageData findAdminRoleAndPermitPage(int pageNum, int pageSize, String organizationId) {
        Integer pageNo = pageSize*(pageNum-1);
        Integer count = iRoleAndPermitDAO.findRoleAndPermitCount(organizationId);
        Integer totalPage = 0;
        if (0==count%pageSize){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }
        List<RoleAndPermitDTO> roleAndPermitDTOS = iRoleAndPermitDAO.findRoleAndPermitPage(organizationId,pageNo,pageSize);
        return new PageData(pageNum,pageSize,totalPage,count,roleAndPermitDTOS);
    }


}
