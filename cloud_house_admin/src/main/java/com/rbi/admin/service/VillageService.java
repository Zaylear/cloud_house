package com.rbi.admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.dao.IdsDeleteDAO;
import com.rbi.admin.dao.VillageDao;
import com.rbi.admin.dao.connect.Village2DAO;
import com.rbi.admin.entity.edo.VillageDO;
import com.rbi.admin.util.DateUtil;
import com.rbi.admin.util.PageData;
import com.rbi.admin.util.PinYin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class VillageService {
    @Autowired
    VillageDao villageDao;

    @Autowired(required = false)
    IdsDeleteDAO idsDeleteDAO;

    @Autowired(required = false)
    Village2DAO village2DAO;

    public PageData findByPage(int pageNum, int pageSize) {
        int pageNo = pageSize * (pageNum - 1);
        List<VillageDO> villageDOS = villageDao.findByPage(pageNo, pageSize);
        int totalPage = 0;
        int count = villageDao.findNum();
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum, pageSize, totalPage, count, villageDOS);
    }



    public void deleteByIds(JSONArray result) {
        String temp = "";
        List<String> list = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            JSONObject obj = (JSONObject) result.get(i);
            String id = obj.getString("id");
            temp = "'" + id + "'";
            list.add(temp);
        }
        String str = String.join(",", list);
        String ids = "(" + str + ")";
        idsDeleteDAO.deleteVillage(ids);
    }

    public void update(Integer id,String villageName, String organizationId, String organizationName,
                       Double constructionArea, Double greeningRate, Double publicArea, String districtCode,
                       String districtName,String enable, String idt, MultipartFile headPhotoFile) throws IOException {

        String timestamps = String.valueOf(DateUtil.timeStamp());

        String villageCode = PinYin.getPinYinHeadChar(villageName).toUpperCase();

        String udt = DateUtil.date(DateUtil.FORMAT_PATTERN);

        String contentType = headPhotoFile.getContentType();
        if (contentType.startsWith("image")) {
            String newFileName = timestamps + "" + new Random().nextInt(10000) + ".jpg";
            FileUtils.copyInputStreamToFile(headPhotoFile.getInputStream(), new File("D:\\", newFileName));//http://192.168.28.151/complain-img
            village2DAO.update(villageCode, villageName,
                    organizationId,organizationName,
                    constructionArea, greeningRate,
                    publicArea, districtCode,
                    districtName,enable,
                    idt, udt,
                    newFileName,id);
        }else {
            village2DAO.update(villageCode, villageName,
                    organizationId,organizationName,
                    constructionArea, greeningRate,
                    publicArea, districtCode,
                    districtName,enable,
                    idt, udt,
                    null,id);
        }
    }

    public void add(String villageCode,String villageName, String organizationId,String organizationName,
                       Double constructionArea, Double greeningRate, Double publicArea, String districtCode,
                       String districtName,MultipartFile headPhotoFile) throws IOException {

        String timestamps = String.valueOf(DateUtil.timeStamp());
        String time = DateUtil.date(DateUtil.FORMAT_PATTERN);
        int enable = 1;

        String contentType = headPhotoFile.getContentType();
        if (contentType.startsWith("image")) {
            String newFileName = timestamps + "" + new Random().nextInt(10000) + ".jpg";
            FileUtils.copyInputStreamToFile(headPhotoFile.getInputStream(), new File("/var/www/html/complaint-img", newFileName));//http://192.168.28.151/complain-img
            String cc ="http://120.78.156.30/complaint-img/"+newFileName;
                    village2DAO.addVillage(villageCode,villageName,
                    organizationId,organizationName,
                    constructionArea, greeningRate,
                    publicArea, districtCode,
                    districtName,time,cc, enable);
            System.out.println(newFileName);
        }else {
            village2DAO.addVillage(villageCode,villageName,
                    organizationId,organizationName,
                    constructionArea, greeningRate,
                    publicArea, districtCode,
                    districtName,time,null,enable);
        }
    }
}
