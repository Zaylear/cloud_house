package com.rbi.service;

import com.rbi.entity.RoleDO;
import com.rbi.util.PageData;

import java.util.List;

public interface RoleService {

    PageData findAllPage(int pageNum, int pageSize);

    PageData findByOrganizationIdAndPage(int pageNum, int pageSize, String userId);

    void deleteByIds(List<Integer> ids);

    void add(RoleDO roleDO,String userId);

    void update(RoleDO roleDO);

    PageData findByPage(Integer pageNo, Integer pageSize,String organizationId,String roleName);

    void addAdmin(RoleDO roleDO);
}
