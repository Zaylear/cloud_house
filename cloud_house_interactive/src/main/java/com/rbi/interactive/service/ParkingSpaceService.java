package com.rbi.interactive.service;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.utils.PageData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ParkingSpaceService {

    void leadingIn(JSONObject jsonObject,String userId);

    Map<String,Object> excelImport(MultipartFile file, String userId) throws IOException;

    String add(JSONObject jsonObject, String userId);

    void update(JSONObject jsonObject,String userId);

    void deleteByIds(List<Integer> ids);


    String findOrganizationId(String userId);

    PageData findFirstByPage(int pageNum, int pageSize, String organizationId);

    PageData findVillage(String villageCode,int pageNum, int pageSize);

    PageData findRegion(String regionCode,int pageNum, int pageSize);

    PageData findBuilding(String buildingCode,int pageNum, int pageSize);

    PageData findParkingSpaceCode(String ParkinSpaceCode,int pageNum, int pageSize);



}
