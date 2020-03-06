package com.rbi.admin.service.connect;


import com.rbi.admin.dao.ExcelDAO;
import com.rbi.admin.dao.VacantRoomDAO;
import com.rbi.admin.entity.dto.RoomDTO;
import com.rbi.admin.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacantRoomService {
    @Autowired(required = false)
    VacantRoomDAO vacantRoomDAO;
    @Autowired(required = false)
    ExcelDAO excelDAO;



    public PageData findVillageByPage(String villageCode,String roomType, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<RoomDTO> roomInfoDTOS = vacantRoomDAO.findVillageByPage(villageCode,roomType, pageNo, pageSize);

        if (roomInfoDTOS.size()!=0) {
            for (int i = 0; i < roomInfoDTOS.size(); i++) {
                Integer floor = excelDAO.findFloorByFloorCode(roomInfoDTOS.get(i).getFloorCode());
                roomInfoDTOS.get(i).setFloor(floor);
            }
        }

        int totalPage = 0;
        int count = vacantRoomDAO.findVillageNum(villageCode,roomType);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, roomInfoDTOS);
    }

    public PageData findRegionByPage(String regionCode,String roomType, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<RoomDTO> roomInfoDTOS = vacantRoomDAO.findRegionByPage(regionCode,roomType, pageNo, pageSize);
        if (roomInfoDTOS.size()!=0) {
            for (int i = 0; i < roomInfoDTOS.size(); i++) {
                Integer floor = excelDAO.findFloorByFloorCode(roomInfoDTOS.get(i).getFloorCode());
                roomInfoDTOS.get(i).setFloor(floor);
            }
        }

        int totalPage = 0;
        int count = vacantRoomDAO.findRegionNum(regionCode,roomType);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, roomInfoDTOS);
    }

    public PageData findBuildingByPage(String buildingCode,String roomType, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<RoomDTO> roomInfoDTOS = vacantRoomDAO.findBuildingByPage(buildingCode,roomType, pageNo, pageSize);
        if (roomInfoDTOS.size()!=0) {
            for (int i = 0; i < roomInfoDTOS.size(); i++) {
                Integer floor = excelDAO.findFloorByFloorCode(roomInfoDTOS.get(i).getFloorCode());
                roomInfoDTOS.get(i).setFloor(floor);
            }
        }

        int totalPage = 0;
        int count = vacantRoomDAO.findBuildingNum(buildingCode,roomType);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, roomInfoDTOS);
    }

    public PageData findUnitByPage(String unitCode,String roomType, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<RoomDTO> roomInfoDTOS = vacantRoomDAO.findUnitByPage(unitCode,roomType, pageNo, pageSize);
        if (roomInfoDTOS.size()!=0) {
            for (int i = 0; i < roomInfoDTOS.size(); i++) {
                Integer floor = excelDAO.findFloorByFloorCode(roomInfoDTOS.get(i).getFloorCode());
                roomInfoDTOS.get(i).setFloor(floor);
            }
        }

        int totalPage = 0;
        int count = vacantRoomDAO.findUnitNum(unitCode,roomType);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, roomInfoDTOS);
    }

    public PageData findRoomByPage(String roomCode,String roomType, int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<RoomDTO> roomInfoDTOS = vacantRoomDAO.findRoomByPage(roomCode,roomType, pageNo, pageSize);

        if (roomInfoDTOS.size()!=0) {
            for (int i = 0; i < roomInfoDTOS.size(); i++) {
                Integer floor = excelDAO.findFloorByFloorCode(roomInfoDTOS.get(i).getFloorCode());
                roomInfoDTOS.get(i).setFloor(floor);
            }
        }
        int totalPage = 0;
        int count = vacantRoomDAO.findRoomNum(roomCode,roomType);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, roomInfoDTOS);
    }


    public PageData findRoomByRoomCode(String roomCode,int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        String roomCode2 = "'%" + roomCode + "%'";
        List<RoomDTO> roomInfoDTOS = vacantRoomDAO.findRoomByRoomCode(roomCode2, pageNo, pageSize);

        if (roomInfoDTOS.size()!=0) {
            for (int i = 0; i < roomInfoDTOS.size(); i++) {
                Integer floor = excelDAO.findFloorByFloorCode(roomInfoDTOS.get(i).getFloorCode());
                roomInfoDTOS.get(i).setFloor(floor);
            }
        }

        int totalPage = 0;
        int count = vacantRoomDAO.findRoomByRoomCodeNum(roomCode2);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, roomInfoDTOS);
    }



}
