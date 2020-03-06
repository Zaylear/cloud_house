package com.rbi.interactive.service.impl;

import com.google.common.collect.Lists;
import com.rbi.interactive.dao.*;
import com.rbi.interactive.entity.*;
import com.rbi.interactive.service.BaseCofingService;
import com.rbi.interactive.utils.Constants;
import com.rbi.interactive.utils.DateUtil;
import com.rbi.interactive.utils.PageData;
import com.rbi.interactive.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class BaseCofingServiceImpl implements BaseCofingService {

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    OrganizationDAO organizationDAO;

    @Autowired
    ChargeItemDAO chargeItemDAO;

    @Autowired
    RoomAndChargeItemsDAO roomAndChargeItemsDAO;

    @Autowired(required = false)
    IRoomAndChargeItemsDAO iRoomAndChargeItemsDAO;

    @Autowired
    VillageDAO villageDAO;

    @Autowired
    RoomDAO roomDAO;

    @Autowired(required = false)
    IChargeDAO iChargeDAO;

    @Override
    public List<ChargeItemDO> findChargeItemByOrganizationId(String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        List<ChargeItemDO> chargeItemDOS = chargeItemDAO.findByOrganizationId(userInfoDO.getOrganizationId());
        return chargeItemDOS;
    }

    @Override
    public void bindChargeItemAndRoom(RoomAndChargeItemsDO roomAndChargeItemsDO, String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);
        ChargeItemDO chargeItemDO = chargeItemDAO.findByChargeCodeAndOrganizationId(roomAndChargeItemsDO.getChargeCode(),organizationId);

        if (1==chargeItemDO.getChargeType()||2==chargeItemDO.getChargeType()||3==chargeItemDO.getChargeType()){
            iChargeDAO.deleteChargeItemConfigInfo(organizationId,roomAndChargeItemsDO.getRoomCode());
            List<Integer> chargeTypes = new ArrayList<>();
            chargeTypes.add(1);
            chargeTypes.add(2);
            chargeTypes.add(3);
            List<ChargeItemDO> chargeItemDOS = chargeItemDAO.findByOrganizationIdAndEnableAndChargeTypeIn(organizationId,1,chargeTypes);
            List<String> chargeCodes = new ArrayList<>();
            for (ChargeItemDO chargeItemDO1:chargeItemDOS) {
                chargeCodes.add(chargeItemDO1.getChargeCode());
            }
            roomAndChargeItemsDAO.deleteAllByRoomCodeAndOrganizationIdAndChargeCodeIn(roomAndChargeItemsDO.getRoomCode(),organizationId,chargeCodes);
            RoomDO roomDO = roomDAO.findByRoomCode(roomAndChargeItemsDO.getRoomCode());
            if (1==roomDO.getRoomType()){
                for (ChargeItemDO chargeItemDO1:chargeItemDOS){
                    if (chargeItemDO1.getChargeType()==1){
                        roomAndChargeItemsDO.setChargeCode(chargeItemDO1.getChargeCode());
                        roomAndChargeItemsDO.setSurplus(0d);
                        roomAndChargeItemsDO.setOrganizationId(organizationId);
                        roomAndChargeItemsDO.setOrganizationName(organizationDO.getOrganizationName());
                        roomAndChargeItemsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        roomAndChargeItemsDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        roomAndChargeItemsDAO.save(roomAndChargeItemsDO);
                    }
                }
            }else if (2==roomDO.getRoomType()){
                for (ChargeItemDO chargeItemDO1:chargeItemDOS){
                    if (chargeItemDO1.getChargeType()==3){
                        roomAndChargeItemsDO.setChargeCode(chargeItemDO1.getChargeCode());
                        roomAndChargeItemsDO.setSurplus(0d);
                        roomAndChargeItemsDO.setOrganizationId(organizationId);
                        roomAndChargeItemsDO.setOrganizationName(organizationDO.getOrganizationName());
                        roomAndChargeItemsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        roomAndChargeItemsDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        roomAndChargeItemsDAO.save(roomAndChargeItemsDO);
                    }
                }
            }else if (3==roomDO.getRoomType()){
                for (ChargeItemDO chargeItemDO1:chargeItemDOS){
                    if (chargeItemDO1.getChargeType()==2){
                        roomAndChargeItemsDO.setChargeCode(chargeItemDO1.getChargeCode());
                        roomAndChargeItemsDO.setSurplus(0d);
                        roomAndChargeItemsDO.setOrganizationId(organizationId);
                        roomAndChargeItemsDO.setOrganizationName(organizationDO.getOrganizationName());
                        roomAndChargeItemsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        roomAndChargeItemsDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                        roomAndChargeItemsDAO.save(roomAndChargeItemsDO);
                    }
                }
            }

        }else {
            RoomAndChargeItemsDO roomAndChargeItemsDO1 = roomAndChargeItemsDAO.findByRoomCodeAndUnitCodeAndBuildingCodeAndRegionCodeAndVillageCodeAndOrganizationIdAndChargeCode(roomAndChargeItemsDO.getRoomCode(),roomAndChargeItemsDO.getUnitCode(),roomAndChargeItemsDO.getBuildingCode(),roomAndChargeItemsDO.getRegionCode(),roomAndChargeItemsDO.getVillageCode(),organizationId,roomAndChargeItemsDO.getChargeCode());
            if (null==roomAndChargeItemsDO1){
                roomAndChargeItemsDO.setSurplus(0d);
                roomAndChargeItemsDO.setOrganizationId(organizationId);
                roomAndChargeItemsDO.setOrganizationName(organizationDO.getOrganizationName());
                roomAndChargeItemsDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                roomAndChargeItemsDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                roomAndChargeItemsDAO.save(roomAndChargeItemsDO);
            }
        }



    }

    @Override
    public PageData findRoomAndChargeItemByPage(int pageNum, int pageSize, String userId,String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();

        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnable(organizationId, Constants.VILLAGE_ENABLE);

        if (villageDOS.size()==0){
            VillageDO villageDO = new VillageDO();
            villageDO.setVillageCode(Constants.VILLAGE_CODE_SET);
            villageDOS.add(villageDO);
        }

        Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.ASC, "roomCode"));
        Page<RoomAndChargeItemsDO> page = roomAndChargeItemsDAO.findAll(new Specification<RoomAndChargeItemsDO>() {
            @Override
            public Predicate toPredicate(Root<RoomAndChargeItemsDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"), organizationId)));
                if (StringUtils.isNotBlank(villageCode)){
                    predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("villageCode"),villageCode)));
                }
                if (StringUtils.isNotBlank(regionCode)){
                    predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("regionCode"),regionCode)));
                }
                if (StringUtils.isNotBlank(buildingCode)){
                    predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("buildingCode"),buildingCode)));
                }
                if (StringUtils.isNotBlank(unitCode)){
                    predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("unitCode"),unitCode)));
                }
                if (StringUtils.isNotBlank(roomCode)){
                    predicateList.add(criteriaBuilder.like(root.get("roomCode").as(String.class),"%"+roomCode+"%"));
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

        return new PageData(page);
    }

    @Override
    public void updateRoomAndChargeItem(RoomAndChargeItemsDO roomAndChargeItemsDO, String userId) {
        roomAndChargeItemsDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        RoomDO roomDO = roomDAO.findByRoomCode(roomAndChargeItemsDO.getRoomCode());
        ChargeItemDO chargeItemDO = chargeItemDAO.findByChargeCodeAndOrganizationId(roomAndChargeItemsDO.getChargeCode(),organizationId);
        RoomAndChargeItemsDO roomAndChargeItemsDO1 = roomAndChargeItemsDAO.findByRoomCodeAndUnitCodeAndBuildingCodeAndRegionCodeAndVillageCodeAndOrganizationIdAndChargeCode(roomAndChargeItemsDO.getRoomCode(),roomAndChargeItemsDO.getUnitCode(),roomAndChargeItemsDO.getBuildingCode(),roomAndChargeItemsDO.getRegionCode(),roomAndChargeItemsDO.getVillageCode(),organizationId,roomAndChargeItemsDO.getChargeCode());
        if (null==roomAndChargeItemsDO1){
            if (1==chargeItemDO.getChargeType() && 1 == roomDO.getRoomType()){
                roomAndChargeItemsDAO.saveAndFlush(roomAndChargeItemsDO);
            }else if (2==chargeItemDO.getChargeType() && 3 == roomDO.getRoomType()){
                roomAndChargeItemsDAO.saveAndFlush(roomAndChargeItemsDO);
            }else if (3==chargeItemDO.getChargeType() && 2 == roomDO.getRoomType()){
                roomAndChargeItemsDAO.saveAndFlush(roomAndChargeItemsDO);
            }else {
                roomAndChargeItemsDAO.saveAndFlush(roomAndChargeItemsDO);
            }
        }
    }

    @Override
    public void deleteRoomAndChargeItem(List<Integer> ids) {
        roomAndChargeItemsDAO.deleteByIdIn(ids);
    }
}
