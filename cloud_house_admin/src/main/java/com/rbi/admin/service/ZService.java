package com.rbi.admin.service;

import com.rbi.admin.util.ExcelUtils;
import com.rbi.admin.util.POIUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ZService {
    public String dealeExcel(MultipartFile file) throws IOException {
        List<String> list = POIUtil.readExcel(file, 6);
        List<String> list2 = new ArrayList<>();
        for (int i =0;i<list.size();i++) {
            List<String> list1 = Arrays.asList(StringUtils.split(list.get(i), ","));
            String roomCode = list1.get(4);
            list2.add(roomCode);
//            System.out.println(roomCode);
        }
//        System.out.println(list2);

        String filepath = "E://newExcel.xlsx";
        String sheetName = "sheet1";
        List<String> titles = new ArrayList<>();
        titles.add("楼层");

        List<Map<String, Object>> values = new ArrayList<>();
        for (int i=0;i<list2.size();i++){
            Map<String, Object> map = new HashMap<>();
            String roomCode = list2.get(i);
            if (roomCode.length() == 3){
                roomCode = roomCode.substring(0,1);
            }else {
                roomCode = roomCode.substring(0,2);
            }
            System.out.println("房间号："+roomCode);
            map.put("楼层",roomCode);
            values.add(map);
        }
        ExcelUtils.writeExcel(filepath, sheetName,titles,values);
        return null;
    }

    public static void main(String[] args) {
        String floor = null;
        System.out.println(Integer.valueOf(floor));
    }
}
