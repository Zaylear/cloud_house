package com.rbi.interactive.service;

import com.rbi.interactive.utils.PageData;

public interface EventService {

    PageData findByPage(String userId,Integer pageNo,Integer pageSize);

}
