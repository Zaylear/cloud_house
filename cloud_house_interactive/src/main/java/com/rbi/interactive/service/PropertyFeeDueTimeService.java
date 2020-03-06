package com.rbi.interactive.service;

public interface PropertyFeeDueTimeService {

    String propertyFeeDueTime(String roomCode,String organizationId);

    String findPropertyFeeDueTime(String roomCode,String organizationId);

    void findAllPropertyFeeDueTime();

}
