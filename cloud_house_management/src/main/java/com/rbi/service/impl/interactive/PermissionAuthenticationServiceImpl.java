package com.rbi.service.impl.interactive;

import com.rbi.dao.IPermissionAuthenticationDao;
import com.rbi.service.interactive.PermissionAuthenticationService;
import com.rbi.util.JwtToken;
import com.rbi.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionAuthenticationServiceImpl implements PermissionAuthenticationService {

    @Autowired(required = false)
    IPermissionAuthenticationDao iPermissionAuthenticationDao;

    @Override
    public Boolean permitAuth(String url,String token) throws Exception {
        String userId = JwtToken.getClaim(token,"userId");
        List<String> permisUrlS = iPermissionAuthenticationDao.findPermisUrlS(userId);
        if(permisUrlS.size()>0&Tools.ifInclude(permisUrlS,url)){
            return true;
        }else {
            return false;
        }
    }
}
