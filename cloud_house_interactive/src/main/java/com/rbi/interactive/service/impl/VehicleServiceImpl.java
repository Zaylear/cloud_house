package com.rbi.interactive.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.dao.IVehicleInformationDAO;
import com.rbi.interactive.dao.OrganizationDAO;
import com.rbi.interactive.dao.UserInfoDAO;
import com.rbi.interactive.dao.VehicleInformationDAO;
import com.rbi.interactive.entity.OrganizationDO;
import com.rbi.interactive.entity.ParkingSpaceInfoDO;
import com.rbi.interactive.entity.UserInfoDO;
import com.rbi.interactive.entity.VehicleInformationDO;
import com.rbi.interactive.service.VehicleService;
import com.rbi.interactive.utils.DateUtil;
import com.rbi.interactive.utils.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    VehicleInformationDAO vehicleInformationDAO;

    @Autowired(required = false)
    IVehicleInformationDAO iVehicleInformationDAO;

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    OrganizationDAO organizationDAO;

    @Override
    public void leadingIn(JSONObject jsonObject, String userId) {

    }

    @Override
    public void add(JSONObject jsonObject, String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);

        VehicleInformationDO vehicleInformationDO = JSONObject.parseObject(jsonObject.toJSONString(),VehicleInformationDO.class);
        vehicleInformationDO.setOrganizationId(organizationId);
        vehicleInformationDO.setOrganizationName(organizationDO.getOrganizationName());
        vehicleInformationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        vehicleInformationDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        vehicleInformationDAO.save(vehicleInformationDO);
    }

    @Override
    public void update(VehicleInformationDO vehicleInformationDO) {
        vehicleInformationDAO.saveAndFlush(vehicleInformationDO);
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        vehicleInformationDAO.deleteByIdIn(ids);
    }

    @Override
    public PageData findByPage(int pageNum, int pageSize, String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        int pageNo = (pageNum-1)*pageSize;

        int count = iVehicleInformationDAO.findCount(organizationId);
        List<VehicleInformationDO> vehicleInformationDOS = iVehicleInformationDAO.findByPage(organizationId,pageNo,pageSize);

        int totalPage;
        if (count%pageSize==0){
            totalPage = count/pageSize;
        }else {
            totalPage = count/pageSize+1;
        }

        return new PageData(pageNum,pageSize,totalPage,count,vehicleInformationDOS);
    }
}
