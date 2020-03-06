package com.rbi.service.interactive;

public interface PermissionAuthenticationService {
    public Boolean permitAuth(String url,String token) throws Exception;
}
