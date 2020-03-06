package com.rbi.interactive.service;

import com.rbi.interactive.entity.OriginalBillDO;
import com.rbi.interactive.utils.PageData;


public interface PrePaymentService {

    PageData<OriginalBillDO> findPrePaymentByPageAndUserIdDesc(int pageNum, int pageSize, String userId);

}
