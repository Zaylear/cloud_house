package com.rbi.admin.controller.connect;

import com.alibaba.fastjson.JSONArray;
import com.rbi.admin.service.SystemSettingService;
import com.rbi.admin.service.connect.VillageChooseService;
import com.rbi.admin.util.JwtToken;
import com.rbi.admin.util.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/villageChooze")
public class VillageChooseController {
    @Autowired
    VillageChooseService villageChooseService;
    @Autowired
    SystemSettingService systemSettingService;

    @PostMapping("/findTree")
    public ResponseVo<JSONArray> findRoomTree(HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            JSONArray jsonArray = villageChooseService.roomTree(organizationId);
            return ResponseVo.build("1000","成功",jsonArray);
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","处理异常");
        }
    }

    @PostMapping("/findBusineseTree")
    public ResponseVo<JSONArray> findBusineseTree(HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String organizationId = systemSettingService.findOrganizationId(userId);
            JSONArray jsonArray = villageChooseService.busineseTree(organizationId);
            return ResponseVo.build("1000","成功",jsonArray);
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","处理异常");
        }

    }


    @Scheduled(cron = "0 0 2 * * ? ")
    public void propertyInarrearsStatistics(){
        villageChooseService.saveAllVillageTree();
    }


    //    @PostMapping("/findTree")
//    public ResponseVo<List<VillageChoose2DTO>> findTree(HttpServletRequest request){
//        try {
//            String token = request.getHeader("appkey");
//            String userId = JwtToken.getClaim(token,"userId");
//            String organizationId = systemSettingService.findOrganizationId(userId);
//            List<VillageChoose2DTO> villageChooseDTO2s = villageChooseService.findTree(organizationId);
//            return ResponseVo.build("1000","成功",villageChooseDTO2s);
//        }catch (Exception e){
//            System.out.println(e);
//            return ResponseVo.build("1002","处理异常");
//        }
//    }
}
