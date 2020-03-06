package com.rbi.admin.controller.connect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.controller.CustomerInfoController;
import com.rbi.admin.entity.dto.RoomDTO;
import com.rbi.admin.entity.dto.houseinfo.HouseInfoDTO;
import com.rbi.admin.entity.dto.houseinfo.RoomInfo2DTO;
import com.rbi.admin.service.connect.RoomService;
import com.rbi.admin.util.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/room")
public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerInfoController.class);

    @Autowired
    RoomService roomService;

    /**
     * 查询房间里面的业主副业主列表
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/findHouseInfo",method = RequestMethod.POST)
    public ResponseVo findHouseInfo(@RequestBody JSONObject jsonObject){
        try{
        HouseInfoDTO<String, RoomInfo2DTO> houseInfoDTO= JSON.toJavaObject(jsonObject,HouseInfoDTO.class);
        HouseInfoDTO result=this.roomService.findHouseUser(houseInfoDTO);
        return ResponseVo.build("1000","成功",result);
        }catch (Exception e){
            logger.error("【服务器处理异常】error:{}",e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/findByRoomCodes",method = RequestMethod.POST)
    public ResponseVo<List<RoomDTO>> findMessageByRoomCodes(@RequestBody JSONObject request){
        try {
            String roomCodes = request.getString("roomCodes");
            List<RoomDTO> roomDTOS = roomService.findMessageByRoomCodes(roomCodes);
            return ResponseVo.build("1000","成功",roomDTOS);
        }catch (Exception e){
            logger.error("【服务器处理异常】error:{}",e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

}
