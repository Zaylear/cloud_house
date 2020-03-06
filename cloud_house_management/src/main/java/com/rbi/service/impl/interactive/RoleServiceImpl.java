package com.rbi.service.impl.interactive;

import com.rbi.dao.IRoleDAO;
import com.rbi.dao.RoleDAO;
import com.rbi.dao.UserInfoDAO;
import com.rbi.entity.RoleDO;
import com.rbi.entity.UserInfoDO;
import com.rbi.service.RoleService;
import com.rbi.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired(required = false)
    IRoleDAO iRoleDAO;

    @Autowired
    RoleDAO roleDAO;

    @Autowired
    UserInfoDAO userInfoDAO;


    @Override
    public PageData findAllPage(int pageNum, int pageSize) {
        Integer pageNo = pageSize*(pageNum-1);
        int count = iRoleDAO.findAllCount();
        List<RoleDO> roleDOS = iRoleDAO.findAllPage(pageNo,pageSize);
        Integer totalPage = 0;
        if (0==count%pageSize){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }
        return new PageData(pageNum,pageSize,totalPage,count,roleDOS);
    }

    @Override
    public PageData findByOrganizationIdAndPage(int pageNum, int pageSize, String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        Integer pageNo = pageSize*(pageNum-1);
        int count = iRoleDAO.findByOrganizationIdCount(organizationId);
        List<RoleDO> roleDOS = iRoleDAO.findByOrganizationIdAndPage(organizationId,pageNo,pageSize);
        Integer totalPage = 0;
        if (0==count%pageSize){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }
        return new PageData(pageNum,pageSize,totalPage,count,roleDOS);
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        roleDAO.deleteByIdIn(ids);
    }

    @Override
    public void add(RoleDO roleDO,String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        String roleCode = organizationId+"-"+ PinYin.getPinYinHeadChar(roleDO.getRoleName()).toUpperCase()+"-"+ Tools.randLetter(2);
        roleDO.setRoleCode(roleCode);
        roleDO.setOrganizationId(organizationId);
        roleDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        roleDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        roleDAO.save(roleDO);
    }

    @Override
    public void update(RoleDO roleDO) {
        roleDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        roleDAO.saveAndFlush(roleDO);
    }

    @Override
    public PageData findByPage(Integer pageNum, Integer pageSize,String organizationId,String roleName) {
//        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
//        String organizationId = userInfoDO.getOrganizationId();
//        Integer pageNo = pageSize*(pageNum-1);
//        int count = iRoleDAO.findByCount();
//        List<RoleDO> roleDOS = iRoleDAO.findByPage(pageNo,pageSize);
//        Integer totalPage = 0;
//        if (0==count%pageSize){
//            totalPage = count/pageSize;
//        }else {
//            totalPage = count/pageSize+1;
//        }
//        return new PageData(pageNum,pageSize,totalPage,count,roleDOS);

        Pageable pageable = new PageRequest(pageNum - 1, pageSize);
        Page<RoleDO> page = roleDAO.findAll(new Specification<RoleDO>() {
            @Override
            public Predicate toPredicate(Root<RoleDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                if (StringUtils.isNotBlank(organizationId)){
                    predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"),organizationId)));
                }
                if (StringUtils.isNotBlank(roleName)){
                    predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("roleName"),"%"+roleName+"%")));
                }
                return criteriaQuery.where(criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]))).getRestriction();
            }
        },pageable);

        return new PageData(page);
    }

    @Override
    public void addAdmin(RoleDO roleDO) {
        String roleCode = roleDO.getOrganizationId()+"-"+ PinYin.getPinYinHeadChar(roleDO.getRoleName()).toUpperCase()+"-"+ Tools.randLetter(2);
        roleDO.setRoleCode(roleCode);
        roleDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        roleDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        roleDAO.save(roleDO);
    }
}
