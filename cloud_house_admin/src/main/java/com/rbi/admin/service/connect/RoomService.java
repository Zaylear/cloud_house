package com.rbi.admin.service.connect;


import com.rbi.admin.dao.connect.RoomDAO;
import com.rbi.admin.dao.connect.WxUserInfoDAO;
import com.rbi.admin.entity.dto.RoomDTO;
import com.rbi.admin.entity.dto.RoomUserDTO;
import com.rbi.admin.entity.dto.houseinfo.HouseInfoDTO;
import com.rbi.admin.entity.dto.houseinfo.RoomInfo2DTO;
import com.rbi.admin.entity.dto.houseinfo.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    @Autowired(required = false)
    RoomDAO roomDAO;

    @Autowired(required = false)
    WxUserInfoDAO wxUserInfoDAO;


    /**
     * 获取房屋详情的信息
     * @param houseInfoDTO
     * @return
     */
    public HouseInfoDTO<UserDTO,RoomUserDTO> findHouseUser(HouseInfoDTO<String, RoomInfo2DTO> houseInfoDTO) {
        HouseInfoDTO<UserDTO,RoomUserDTO> result=new HouseInfoDTO<UserDTO,RoomUserDTO>();
        RoomUserDTO roomUserDTO=this.roomDAO.findRoomUser(
                houseInfoDTO.getHouseInfo().getRoomCode()
                ,houseInfoDTO.getHouseInfo().getUserId());
        List property=this.wxUserInfoDAO.findPhoneAndName(houseInfoDTO.getPropertyList());
        List tenant=this.wxUserInfoDAO.findPhoneAndName(houseInfoDTO.getTenantList());
        result.setHouseInfo(roomUserDTO);
        result.setPropertyList(property);
        result.setTenantList(tenant);
        return result;
    }


    public List<RoomDTO> findMessageByRoomCodes(String roomCodes) {

        String roomC[] = roomCodes.split(",");
        List<String> list = new ArrayList<>();
        String temp;
        for (int i=0;i<roomC.length;i++){
            temp = "'"+roomC[i]+"'";
            list.add(temp);
        }
        String ss = String.join(",",list);
        List<RoomDTO> roomDTOS = roomDAO.findMessageByRoomCodes(ss);
        return roomDTOS;
    }
}
