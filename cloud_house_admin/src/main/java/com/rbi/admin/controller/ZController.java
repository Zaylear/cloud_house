package com.rbi.admin.controller;

import com.rbi.admin.service.ZService;
import com.rbi.admin.util.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ZController {
    @Autowired
    ZService zService;

    @PostMapping("test1")
    public ResponseVo dealeExcel(MultipartFile file){
        try {
            zService.dealeExcel(file);
            return ResponseVo.build("1000","成功");
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","异常");
        }

    }



}
