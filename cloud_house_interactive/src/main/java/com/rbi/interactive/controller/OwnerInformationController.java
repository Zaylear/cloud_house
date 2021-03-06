package com.rbi.interactive.controller;

import com.rbi.interactive.service.OwnerInformationService;
import com.rbi.interactive.utils.JwtToken;
import com.rbi.interactive.utils.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/owner")
public class OwnerInformationController {

    private final static Logger logger = LoggerFactory.getLogger(OwnerInformationController.class);

    @Autowired
    OwnerInformationService ownerInformationService;

    /**
     * 批量导入业主信息，通过Excel表格导入
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/import")
    public ResponseVo batchProcessingLiquidatedDamages(MultipartFile file, HttpServletRequest request){
        try {
            if (file.getOriginalFilename().indexOf("xls")>-1||file.getOriginalFilename().indexOf("xlsx")>-1){
                String token = request.getHeader("appkey");
                String userId = JwtToken.getClaim(token,"userId");
                Map<String,Object> map = ownerInformationService.ownerInformation(file,userId);
                return ResponseVo.build("1000","请求成功",map);
            }else {
                return ResponseVo.build("1005","文件类型错误，请使用Excel文件！");
            }
        } catch (Exception e) {
            logger.error("【业主信息导入请求类】批量处理业主信息失败！，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理异常，ERROR：{}",e);
        }

    }
}
