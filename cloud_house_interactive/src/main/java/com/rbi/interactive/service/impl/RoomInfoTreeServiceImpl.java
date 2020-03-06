package com.rbi.interactive.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.rbi.interactive.dao.IRoomInfoDAO;
import com.rbi.interactive.dao.UserInfoDAO;
import com.rbi.interactive.entity.UserInfoDO;
import com.rbi.interactive.entity.dto.HouseAndProprietorDTO;
import com.rbi.interactive.entity.dto.RoomInfoTreeDTO;
import com.rbi.interactive.service.RoomInfoTreeService;
import com.rbi.interactive.utils.EncapsulationTreeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomInfoTreeServiceImpl implements RoomInfoTreeService {

    private final static Logger logger = LoggerFactory.getLogger(RoomInfoTreeServiceImpl.class);

    @Autowired(required = false)
    IRoomInfoDAO iRoomInfoDAO;

    @Autowired
    UserInfoDAO userInfoDAO;

    @Override
    public Map<String,Object> findRoomInfoByMobilePhone(String userId,String mobilePhone) throws Exception {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        List<HouseAndProprietorDTO> houseAndProprietorDTOS = iRoomInfoDAO.findHouseInfoByMobilePhone(userInfoDO.getOrganizationId(),mobilePhone);
        List<RoomInfoTreeDTO> roomInfoTreeDTOS = new ArrayList<>();
        for (HouseAndProprietorDTO houseAndProprietorDTO:houseAndProprietorDTOS) {
            RoomInfoTreeDTO roomInfoTreeDTO = new RoomInfoTreeDTO();
            roomInfoTreeDTO.setCode(houseAndProprietorDTO.getVillageCode());
            roomInfoTreeDTO.setName(houseAndProprietorDTO.getVillageName());
            roomInfoTreeDTOS.add(roomInfoTreeDTO);
        }
        for (HouseAndProprietorDTO houseAndProprietorDTO:houseAndProprietorDTOS) {
            RoomInfoTreeDTO roomInfoTreeDTO = new RoomInfoTreeDTO();
            roomInfoTreeDTO.setCode(houseAndProprietorDTO.getRegionCode());
            roomInfoTreeDTO.setName(houseAndProprietorDTO.getRegionName());
            roomInfoTreeDTO.setPid(houseAndProprietorDTO.getVillageCode());
            roomInfoTreeDTOS.add(roomInfoTreeDTO);
        }
        for (HouseAndProprietorDTO houseAndProprietorDTO:houseAndProprietorDTOS) {
            RoomInfoTreeDTO roomInfoTreeDTO = new RoomInfoTreeDTO();
            roomInfoTreeDTO.setCode(houseAndProprietorDTO.getBuildingCode());
            roomInfoTreeDTO.setName(houseAndProprietorDTO.getBuildingName());
            roomInfoTreeDTO.setPid(houseAndProprietorDTO.getRegionCode());
            roomInfoTreeDTOS.add(roomInfoTreeDTO);
        }
        for (HouseAndProprietorDTO houseAndProprietorDTO:houseAndProprietorDTOS) {
            RoomInfoTreeDTO roomInfoTreeDTO = new RoomInfoTreeDTO();
            roomInfoTreeDTO.setCode(houseAndProprietorDTO.getUnitCode());
            roomInfoTreeDTO.setName(houseAndProprietorDTO.getUnitName());
            roomInfoTreeDTO.setPid(houseAndProprietorDTO.getBuildingCode());
            roomInfoTreeDTOS.add(roomInfoTreeDTO);
        }
        for (HouseAndProprietorDTO houseAndProprietorDTO:houseAndProprietorDTOS) {
            RoomInfoTreeDTO roomInfoTreeDTO = new RoomInfoTreeDTO();
            roomInfoTreeDTO.setCode(houseAndProprietorDTO.getRoomCode());
            roomInfoTreeDTO.setName(houseAndProprietorDTO.getRoomCode());
            roomInfoTreeDTO.setPid(houseAndProprietorDTO.getUnitCode());
            roomInfoTreeDTOS.add(roomInfoTreeDTO);
        }
        List<RoomInfoTreeDTO> roomInfoTreeDTOList = EncapsulationTreeUtil.getTree(roomInfoTreeDTOS,"code","pid","villageChoose2DTO");
        Map<String,Object> map = new HashMap<>();
        map.put("roomTree",roomInfoTreeDTOList);
        map.put("userInfo",userInfoDO);
        return map;
    }
}
