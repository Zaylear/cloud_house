package com.rbi.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.entity.RoleDO;
import com.rbi.entity.UserInfoDO;
import com.rbi.entity.dto.PermitDTO;
import com.rbi.entity.dto.RoleAndPermitDTO;
import com.rbi.entity.dto.UserAndRoleDTO;
import com.rbi.util.PageData;

import java.util.List;

public interface PermissionConfigurationService {

    List<RoleDO> findAllByOrganizationId(String userId);

    List<PermitDTO> findPermitAll() throws Exception;

    List<PermitDTO> findAdminPermitAll() throws Exception;

    List<UserInfoDO> findUserInfoByOrganizationId(String userId);

    void addUserInfoAndRole(JSONObject jsonObject);

    void addRoleAndPermit(JSONObject jsonObject);

    PageData findUserAndRolePage(Integer pageNum, Integer pageSize, String userId);

    PageData findRoleAndPermitPage(Integer pageNum,Integer pageSize,String userId);

    void deleteRoleAndPermitByIds(List<Integer> ids);

    void deleteRoleAndPermit(JSONArray jsonObjectList);

    void deleteUserAndRoleByIds(List<Integer> ids);

    List<RoleAndPermitDTO> findPermitByRole(String roleCode);

    List<UserAndRoleDTO> findRoleByUserId(String userId);

    List<RoleDO> findRoleAllByOrganizationId(String organizationId);

    List<UserInfoDO> findAdminUserInfoByOrganizationId(String organizationId);

    PageData findAdminUserAndRolePage(int pageNo, int pageSize, String organizationId);

    PageData findAdminRoleAndPermitPage(int pageNo, int pageSize, String organizationId);
}
