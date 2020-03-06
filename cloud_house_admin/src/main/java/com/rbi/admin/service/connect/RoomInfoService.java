package com.rbi.admin.service.connect;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.dao.connect.RoomInfoDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoomInfoService {
    @Autowired(required = false)
    RoomInfoDAO roomInfoDAO;

    public void logout(JSONObject json) {
        JSONArray array = json.getJSONArray("data");
        for (int i = 0; i<array.size();i++){
            JSONObject jsonObject = (JSONObject)array.get(i);
            String roomCode= jsonObject.getString("roomCode");
            String customerUserId = jsonObject.getString("customerUserId");
            Integer identity = jsonObject.getInteger("identity");
            if (identity==1){
                roomInfoDAO.ChangeCustomerIdentity(4,roomCode,customerUserId);
            }
            if (identity==2){
                roomInfoDAO.ChangeCustomerIdentity(5,roomCode,customerUserId);
            }
            if (identity==3){
                roomInfoDAO.ChangeCustomerIdentity(6,roomCode,customerUserId);
            }
            roomInfoDAO.logoutRC(roomCode,customerUserId);
        }

    }


    public void deleteRoom(JSONArray result) {
        for (int i = 0; i < result.size(); i++) {
            JSONObject object = (JSONObject) result.get(i);
            String roomCode = object.getString("roomCode");
            roomInfoDAO.deleteRoom(roomCode);
            roomInfoDAO.deleteCustomer(roomCode);
            roomInfoDAO.deleteRoomItem(roomCode);
        }
    }

    public void deleteSingleCustomer(JSONObject json) {
        JSONArray jsonArray = json.getJSONArray("data");
        for (int i = 0; i<jsonArray.size();i++){
            JSONObject jsonObject = (JSONObject)jsonArray.get(i);
            String roomCode = jsonObject.getString("roomCode");
            String customerUserId = jsonObject.getString("customerUserId");
            roomInfoDAO.deleteSingleCustomer(roomCode,customerUserId);
        }
    }

//    public void deleteSingleCustomer(String roomCode,String customerUserId){
//        roomInfoDAO.deleteSingleCustomer(roomCode,customerUserId);
//    }

}
