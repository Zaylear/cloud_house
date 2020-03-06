package com.rbi.interactive.service;

import com.rbi.interactive.utils.PageData;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.Map;

public interface HistoryDataService {

    Map<String,Object> importHistoryData(MultipartFile multipartFile, String userId);

    Map<String,Object> importHistoryDataCumulative(MultipartFile multipartFile, String userId);

    Map<String,Object> importExclusiveParkingSpaceHistoryData(MultipartFile multipartFile, String userId) throws ParseException;

    Map<String,Object> importExclusiveParkingSpaceHistoryDataCumulative(MultipartFile multipartFile, String userId) throws ParseException;

    PageData findByPage(int pageNum, int pageSize, String userId,String roomCode);

    PageData findExclusiveParkingSpaceHistoryDataByPage(int pageNum, int pageSize, String userId,String parkingSpaceCode);

}
