package com.rbi.admin.service;

import com.rbi.admin.dao.CustomerInfoDAO;
import com.rbi.admin.dao.connect.CustomerDAO2;
import com.rbi.admin.dao.connect.RoomDAO;
import com.rbi.admin.entity.edo.CustomerInfoDO;
import com.rbi.admin.entity.dto.RoomUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerInfoService {

    @Autowired(required = false)
    RoomDAO roomDAO;

    @Autowired(required = false)
    CustomerDAO2 customerDao;

    @Autowired(required = false)
    CustomerInfoDAO customerInfoDAO;

    public List<CustomerInfoDO> findByUserIds(String userIds) {
        String customer[] = userIds.split(",");
        List<String> list = new ArrayList<>();
        String temp;
        for (int i=0;i<customer.length;i++){
            temp = "'"+customer[i]+"'";
            list.add(temp);
        }
        String str= String.join(",",list);
        List<CustomerInfoDO> customerDOS = customerDao.findByUserIds(str);
        return customerDOS;
    }

    public CustomerInfoDO finedByOpenId(String openId) {
        CustomerInfoDO customerInfoDO = customerInfoDAO.findByOpenId(openId);
        return customerInfoDO;
    }

    /**
     * 获取指定的房间信息和用户信息
     * @param list
     * @return
     */
    public List<RoomUserDTO> findRoomUser(List<RoomUserDTO> list) {

        List<RoomUserDTO> roomUserDTOS = new ArrayList<>();
        for (RoomUserDTO roomUserDTO:list){
            roomUserDTOS.add(roomDAO.findRoomUser(roomUserDTO.getRoomCode(),roomUserDTO.getUserId()));
        }

        return roomUserDTOS;
    }
}
