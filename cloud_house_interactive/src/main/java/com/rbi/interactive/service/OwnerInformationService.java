package com.rbi.interactive.service;

import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.Map;

public interface OwnerInformationService {

    Map<String,Object> ownerInformation(MultipartFile multipartFile, String userId) throws ParseException;

}
