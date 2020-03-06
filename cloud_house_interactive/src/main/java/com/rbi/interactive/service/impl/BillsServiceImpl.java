package com.rbi.interactive.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.rbi.interactive.dao.*;
import com.rbi.interactive.entity.*;
import com.rbi.interactive.service.BillsService;
import com.rbi.interactive.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.*;

@Service
public class BillsServiceImpl implements BillsService {

    @Autowired
    BillDAO billDAO;

    @Autowired(required = false)
    IBillDAO iBillDAO;

    @Autowired
    BillDetailedDAO billDetailedDAO;

    @Autowired
    ParkingSpaceCostDetailDAO parkingSpaceCostDetailDAO;

    @Autowired
    CostDeductionDAO costDeductionDAO;

    @Autowired
    OrganizationDAO organizationDAO;

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    VillageDAO villageDAO;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public PageData<BillDO> findBillPage(int pageNo, int pageSize,String userId,String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode,String mobilePhone,String idNumber,String surname) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);

        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnable(userInfoDO.getOrganizationId(), Constants.VILLAGE_ENABLE);

        if (villageDOS.size()==0){
            VillageDO villageDO = new VillageDO();
            villageDO.setVillageCode(Constants.VILLAGE_CODE_SET);
            villageDOS.add(villageDO);
        }

        Pageable pageable = new PageRequest(pageNo - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<BillDO> page = billDAO.findAll(new Specification<BillDO>() {
            @Override
            public Predicate toPredicate(Root<BillDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"), userInfoDO.getOrganizationId())));
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("invalidState"), 0)));
                if (StringUtils.isNotBlank(villageCode)){
                    predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("villageCode"),villageCode)));
                }
                if (StringUtils.isNotBlank(regionCode)){
                    predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("regionCode"),regionCode)));
                }
                if (StringUtils.isNotBlank(buildingCode)){
                    predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("buildingCode"),buildingCode)));
                    if (StringUtils.isNotBlank(mobilePhone)){
                        predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("mobilePhone"),mobilePhone)));
                    }
                }
                if (StringUtils.isNotBlank(unitCode)){
                    predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("unitCode"),unitCode)));
                    if (StringUtils.isNotBlank(mobilePhone)){
                        predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("mobilePhone"),mobilePhone)));
                    }
                }
                if (StringUtils.isNotBlank(roomCode)){
                    predicateList.add(criteriaBuilder.like(root.get("roomCode").as(String.class),"%"+roomCode+"%"));
                }
                if (StringUtils.isNotBlank(mobilePhone)){
                    predicateList.add(criteriaBuilder.like(root.get("mobilePhone").as(String.class),"%"+mobilePhone+"%"));
                }
                if (StringUtils.isNotBlank(idNumber)){
                    predicateList.add(criteriaBuilder.like(root.get("idNumber").as(String.class),"%"+idNumber+"%"));
                }
                if (StringUtils.isNotBlank(surname)){
                    predicateList.add(criteriaBuilder.like(root.get("surname").as(String.class),"%"+surname+"%"));
                }
                Expression expression = root.get("villageCode");
                expression.in(villageDOS.get(0).getVillageCode());
                List<String> villageCodes = new ArrayList<>();
                for (VillageDO villageDO:villageDOS) {
                    villageCodes.add(villageDO.getVillageCode());
                }
                predicateList.add(expression.in(villageCodes));
                return criteriaQuery.where(criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]))).getRestriction();
            }
        },pageable);
        PageData pageData = new PageData(page);
        List<BillDO> billDOS = pageData.getContents();
        for (BillDO billDO:billDOS) {
            List<BillDetailedDO> billDetailedDOList = billDetailedDAO.findAllByOrderIdAndOrganizationId(billDO.getOrderId(),billDO.getOrganizationId());
            String costInvolved = null;
            for (BillDetailedDO billDetailedDO:billDetailedDOList) {
                if ("1".equals(billDetailedDO.getChargeType())||"2".equals(billDetailedDO.getChargeType())||"3".equals(billDetailedDO.getChargeType())){
                    costInvolved = "物业费";
                }
            }
            List<ParkingSpaceCostDetailDO> parkingSpaceCostDetailDOS = parkingSpaceCostDetailDAO.findAllByOrderIdAndOrganizationId(billDO.getOrderId(),billDO.getOrganizationId());
            for (ParkingSpaceCostDetailDO parkingSpaceCostDetailDO:parkingSpaceCostDetailDOS) {
                if ("5".equals(parkingSpaceCostDetailDO.getChargeType())){
                    if (null==costInvolved||"".equals(costInvolved)){
                        costInvolved = "车位租赁费";
                    }else {
                        costInvolved = costInvolved+"；车位租赁费";
                    }
                }
                if ("6".equals(parkingSpaceCostDetailDO.getChargeType())){
                    if (null==costInvolved||"".equals(costInvolved)){
                        costInvolved = "车位管理费";
                    }else {
                        costInvolved = costInvolved+"；车位管理费";
                    }
                }
            }
            if (null==costInvolved||"".equals(costInvolved)){
                costInvolved = "其他费用";
            }
            billDO.setCostInvolved(costInvolved);
        }
        pageData.setContents(billDOS);
        return pageData;

    }

    @Override
    public Map<String, Object> findBillDetail(String userId, String orderId) throws ParseException {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        BillDO billDO = new BillDO();
        billDO = billDAO.findByOrderIdAndOrganizationId(orderId,userInfoDO.getOrganizationId());

        List<BillDetailedDO> billDetailedDOS = new ArrayList<>();
        billDetailedDOS = billDetailedDAO.findAllByOrderIdAndOrganizationId(orderId,userInfoDO.getOrganizationId());
        for (BillDetailedDO billDetailedDO:billDetailedDOS) {
            if (null!=billDetailedDO.getDueTime()){
                billDetailedDO.setDueTime(DateUtil.getAfterDay(billDetailedDO.getDueTime(),-1));
            }
        }

        List<CostDeductionDO> costDeductionDOS = new ArrayList<>();
        costDeductionDOS = costDeductionDAO.findAllByOrderIdAndOrganizationId(orderId,userInfoDO.getOrganizationId());

        List<ParkingSpaceCostDetailDO> parkingSpaceCostDetailDOS = parkingSpaceCostDetailDAO.findAllByOrderIdAndOrganizationId(orderId,userInfoDO.getOrganizationId());
        for (ParkingSpaceCostDetailDO parkingSpaceCostDetailDO:parkingSpaceCostDetailDOS) {
            if (null!=parkingSpaceCostDetailDO.getDueTime()){
                parkingSpaceCostDetailDO.setDueTime(DateUtil.getAfterDay(parkingSpaceCostDetailDO.getDueTime(),-1));
            }
        }

        Map<String,Object> map = new HashMap<>();
        map.put("bill",billDO);
        map.put("billDetailedDOS",billDetailedDOS);
        map.put("costDeductionDOS",costDeductionDOS);
        map.put("parkingSpaceCostDetailDOS",parkingSpaceCostDetailDOS);
        return map;
    }


    @Override
    @Transactional
    public void deleteBill(JSONObject jsonObject, String userId){
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        List<String> idList = new ArrayList<>(Arrays.asList(jsonObject.getString("ids").split(",")));

        for (String id:idList) {
            BillDO billDO = billDAO.findByIdAndOrganizationId(Integer.parseInt(id),userInfoDO.getOrganizationId());
            billDO.setInvalidState(1);
            billDAO.saveAndFlush(billDO);
            redisUtil.set(Constants.REDISKEY_PROJECT + Constants.PROPERTY_DUE_TIME +userInfoDO.getOrganizationId()+"-"+ billDO.getRoomCode(), "NAN");
        }
    }

    @Override
    public void updateBill(JSONObject jsonObject) {
        BillDO billDO = JSONObject.parseObject(jsonObject.toString(),BillDO.class);
        billDAO.saveAndFlush(billDO);
        redisUtil.set(Constants.REDISKEY_PROJECT + Constants.PROPERTY_DUE_TIME +billDO.getOrganizationId()+"-"+ billDO.getRoomCode(), "NAN");
    }

    @Override
    public void updateBillDetail(JSONObject jsonObject) {
        BillDetailedDO billDetailedDO = JSONObject.parseObject(jsonObject.toString(),BillDetailedDO.class);
        billDetailedDAO.saveAndFlush(billDetailedDO);
    }

    @Override
    public void updateParkingSpaceBillDetail(JSONObject jsonObject) {
        ParkingSpaceCostDetailDO parkingSpaceCostDetailDO = JSONObject.parseObject(jsonObject.toString(),ParkingSpaceCostDetailDO.class);
        parkingSpaceCostDetailDAO.saveAndFlush(parkingSpaceCostDetailDO);
    }

}
