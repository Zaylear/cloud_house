package com.rbi.controller.interactive;

import com.alibaba.fastjson.JSONObject;
import com.rbi.entity.dto.PermitDTO;
import com.rbi.service.interactive.PermissionService;
import com.rbi.util.JwtToken;
import com.rbi.util.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    @PostMapping("/findByParentCode")
    public ResponseVo findByParentCode(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String parentCode = jsonObject.getString("parentCode");
            List<PermitDTO> permitDTOS = permissionService.findByParentCode(parentCode,userId);
            return ResponseVo.build("1000","请求成功",permitDTOS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.build("1002","服务器处理失败");
        }
    }
}
