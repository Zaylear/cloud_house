package com.rbi.interactive.controller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.dao.UserInfoDAO;
import com.rbi.interactive.entity.ParkingSpaceManagementDO;
import com.rbi.interactive.entity.UserInfoDO;
import com.rbi.interactive.service.QueryExclusiveParkingSpaceService;
import com.rbi.interactive.utils.JwtToken;
import com.rbi.interactive.utils.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class QueryExclusiveParkingSpaceController {

    private final static Logger logger = LoggerFactory.getLogger(QueryExclusiveParkingSpaceController.class);

    @Autowired
    QueryExclusiveParkingSpaceService queryExclusiveParkingSpaceService;

    @Autowired
    UserInfoDAO userInfoDAO;

    @PostMapping("/query_exclusive_parking_space")
    public ResponseVo<List<ParkingSpaceManagementDO>> queryExclusiveParkingSpaceController(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
            List<ParkingSpaceManagementDO> parkingSpaceManagementDOS = queryExclusiveParkingSpaceService.queryExclusiveParkingSpaceService(jsonObject.getString("roomCode"),userInfoDO.getOrganizationId());
            return ResponseVo.build("1000","请求成功",parkingSpaceManagementDOS);
        } catch (Exception e) {
            logger.error("【专有车位查询请求类】查询专有车位信息错误，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

}
