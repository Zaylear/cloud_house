package com.rbi.service.impl.interactive;

import com.rbi.dao.IPermissionDAO;
import com.rbi.entity.dto.PermitDTO;
import com.rbi.service.interactive.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired(required = false)
    IPermissionDAO iPermissionDAO;

    @Override
    public List<PermitDTO> findByParentCode(String parentCode,String userId) {
        List<PermitDTO> permitDTOS = iPermissionDAO.findByParentCode(parentCode,userId);
        return permitDTOS;
    }
}
