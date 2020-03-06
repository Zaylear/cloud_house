package com.rbi.service.interactive;


import com.rbi.entity.dto.PermitDTO;

import java.util.List;

public interface PermissionService {

    List<PermitDTO> findByParentCode(String parentCode,String userId);

}
