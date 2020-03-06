package com.rbi.interactive.service.impl;

import com.rbi.interactive.dao.IParkingSpaceCostDAO;
import com.rbi.interactive.entity.dto.RealGenerationTimeAndContractNumberDTO;
import com.rbi.interactive.service.PinkingSpaceContractNumberService;
import com.rbi.interactive.utils.DateUtil;
import com.rbi.interactive.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PinkingSpaceContractNumberServiceImpl implements PinkingSpaceContractNumberService {

    @Autowired(required = false)
    IParkingSpaceCostDAO iParkingSpaceCostDAO;

    @Override
    public String contractNumber(int rentalRenewalStatus,String roomCode,String licensePlateNumber,String organizationId) {
        String time = DateUtil.date(DateUtil.YEAR_MONTH_DAY)+"%";
        if (1==rentalRenewalStatus){
            //验证该客户是否有租赁记录
            RealGenerationTimeAndContractNumberDTO realGenerationTimeAndContractNumberDTO = iParkingSpaceCostDAO.queryContractNumber(organizationId,roomCode,licensePlateNumber);
            if (null==realGenerationTimeAndContractNumberDTO){
                return "NAN";
            }else {
                int count = iParkingSpaceCostDAO.queryRentalParkingSpaceCount(organizationId,roomCode,licensePlateNumber)+1;
                return realGenerationTimeAndContractNumberDTO.getContractNumber()+"-"+count;
            }
        }else {
            //获取当前时间
            String ti = DateUtil.date(DateUtil.DATE_FORMAT_PATTERN);
            //查询当天租车数量
            int renewalParkingSpaceCount = iParkingSpaceCostDAO.queryNonRentalParkingSpaceCountBySameDay(organizationId,time)+1;
            return ti+ Tools.frontCompWithZore(renewalParkingSpaceCount,3);
        }
    }
}
