package com.rbi.interactive.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.rbi.interactive.abnormal.DueTimeException;
import com.rbi.interactive.dao.*;
import com.rbi.interactive.entity.*;
import com.rbi.interactive.entity.dto.BillsBackDTO;
import com.rbi.interactive.entity.dto.RefundAndApplicationDTO;
import com.rbi.interactive.service.PropertyFeeDueTimeService;
import com.rbi.interactive.service.RefundService;
import com.rbi.interactive.service.ThisSystemOrderIdService;
import com.rbi.interactive.service.impl.charge.PropertyFeeCostImpl;
import com.rbi.interactive.service.impl.strategy.ICostStrategy;
import com.rbi.interactive.service.impl.strategy.ICostStrategyImpl;
import com.rbi.interactive.utils.*;
import org.springframework.beans.BeanUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RefundServiceImpl implements RefundService {

    @Autowired
    ThisSystemOrderIdService thisSystemOrderIdService;

    @Autowired
    CustomerInfoDAO customerInfoDAO;

    @Autowired
    ChargeItemDAO chargeItemDAO;

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    OrganizationDAO organizationDAO;

    @Autowired
    RefundDAO refundDAO;

    @Autowired(required = false)
    IRefundDAO iRefundDAO;

    @Autowired(required = false)
    IBillDAO iBillDAO;

    @Autowired
    RoomDAO roomDAO;

    @Autowired
    BillDetailedDAO billDetailedDAO;

    @Autowired
    BillDAO billDAO;

    @Autowired
    OriginalBillDAO originalBillDAO;

    @Autowired
    RefundApplicationDAO refundApplicationDAO;

    @Autowired(required = false)
    IRefundApplicationDAO iRefundApplicationDAO;

    @Autowired
    VillageDAO villageDAO;

    @Autowired
    PropertyFeeDueTimeService propertyFeeDueTimeService;

    @Autowired(required = false)
    IChargeDAO iChargeDAO;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    EventRecordDAO eventRecordDAO;

    @Override
    public Map findCustomerByPhone(String mobilePhone) {
        List<CustomerInfoDO> customerInfoDOS = customerInfoDAO.findByMobilePhone(mobilePhone);
        Map map = new HashMap();
        if (0==customerInfoDOS.size()){
            map.put("status","1003");
            return map;
        }else {
            map.put("status","1000");
            map.put("value",customerInfoDOS.get(0));
            return map;
        }

    }

    @Override
    public List<ChargeItemDO> findChargeItem(String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(userInfoDO.getOrganizationId());
        List<ChargeItemDO> chargeItemDOS = chargeItemDAO.findByOrganizationIdAndRefund(organizationDO.getOrganizationId(),1);
        return chargeItemDOS;
    }

    @Override
    @Transactional
    public void add(JSONObject jsonObject,String userIda) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userIda);
        String organizationId = userInfoDO.getOrganizationId();
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);
        String orderId = DateUtil.timeStamp().toString()+(int)((Math.random()*9+1)*1000);
//        Integer orderIdsCount = iBillDAO.findOrderIdsByorganizationId(organizationId,time);
//        if (orderIdsCount<9){
//            orderIdsCount = orderIdsCount+1;
//            orderId = time+"000"+orderIdsCount;
//        }else if (orderIdsCount>=9&&orderIdsCount<99){
//            orderIdsCount = orderIdsCount+1;
//            orderId = time+"00"+orderIdsCount;
//        }else if (orderIdsCount>=99&&orderIdsCount<999){
//            orderIdsCount = orderIdsCount+1;
//            orderId = time+"0"+orderIdsCount;
//        }else {
//            orderIdsCount = orderIdsCount+1;
//            orderId = time+orderIdsCount;
//        }

        String tollCollectorId = userInfoDO.getUserId();
        String payerPhone = jsonObject.getString("payerPhone");
        String payerName = jsonObject.getString("payerName");
        String chargeType = jsonObject.getString("chargeType");
        String paymentMethod = jsonObject.getString("paymentMethod");
        Integer refundStatus = 0;
        Integer invalidState = 0;
        String villageCode = jsonObject.getString("villageCode");
        String villageName = jsonObject.getString("villageName");
        String regionCode = jsonObject.getString("regionCode");
        String regionName = jsonObject.getString("regionName");
        String buildingCode = jsonObject.getString("buildingCode");
        String buildingName = jsonObject.getString("buildingName");
        String unitCode = jsonObject.getString("unitCode");
        String unitName = jsonObject.getString("unitName");
        String customerUserId = jsonObject.getString("customerUserId");
        String surname = jsonObject.getString("surname");
        String idNumber = jsonObject.getString("idNumber");
        String mobilePhone = jsonObject.getString("mobilePhone");
        String roomCode = jsonObject.getString("roomCode");
        RoomDO roomDO = roomDAO.findByRoomCode(roomCode);
        Double roomSize = roomDO.getRoomSize();
        String chargeCode = jsonObject.getString("chargeCode");
        String chargeName = jsonObject.getString("chargeName");
        Double actualMoneyCollection = jsonObject.getDouble("chargeStandard");
        Double mortgageAmount = 0d;
        String reasonForDeduction;
        Double refundableAmount = jsonObject.getDouble("chargeStandard");
        String chargeUnit = jsonObject.getString("chargeUnit");
        String startTime = jsonObject.getString("startTime");
        String dueTime = jsonObject.getString("dueTime");
        Integer delayTime = 0;
        String delayReason;
        String personLiable = jsonObject.getString("personLiable");
        String personLiablePhone = jsonObject.getString("personLiablePhone");
        String responsibleAgencies = jsonObject.getString("responsibleAgencies");
        String remark = jsonObject.getString("remark");
        String idt = DateUtil.date(DateUtil.FORMAT_PATTERN);
        String udt = DateUtil.date(DateUtil.FORMAT_PATTERN);
        RefundHistoryDO refundHistoryDO = new RefundHistoryDO();
        refundHistoryDO.setOrganizationId(organizationId);
        refundHistoryDO.setOrganizationName(organizationDO.getOrganizationName());
        refundHistoryDO.setOrderId(orderId);
        refundHistoryDO.setActualMoneyCollection(actualMoneyCollection);
        refundHistoryDO.setBuildingCode(buildingCode);
        refundHistoryDO.setBuildingName(buildingName);
        refundHistoryDO.setChargeCode(chargeCode);
        refundHistoryDO.setChargeName(chargeName);
        refundHistoryDO.setChargeUnit(chargeUnit);
        refundHistoryDO.setDelayTime(delayTime);
        refundHistoryDO.setDueTime(dueTime);
        refundHistoryDO.setIdt(idt);
        refundHistoryDO.setInvalidState(invalidState);
        refundHistoryDO.setMobilePhone(mobilePhone);
        refundHistoryDO.setIdNumber(idNumber);
        refundHistoryDO.setMortgageAmount(mortgageAmount);
        refundHistoryDO.setPayerName(payerName);
        refundHistoryDO.setPayerPhone(payerPhone);
        refundHistoryDO.setPaymentMethod(paymentMethod);
        refundHistoryDO.setChargeType(chargeType);
        refundHistoryDO.setPersonLiable(personLiable);
        refundHistoryDO.setPersonLiablePhone(personLiablePhone);
        refundHistoryDO.setRefundableAmount(refundableAmount);
        refundHistoryDO.setRefundStatus(refundStatus);
        refundHistoryDO.setRegionCode(regionCode);
        refundHistoryDO.setRegionName(regionName);
        refundHistoryDO.setRemark(remark);
        refundHistoryDO.setResponsibleAgencies(responsibleAgencies);
        refundHistoryDO.setRoomCode(roomCode);
        refundHistoryDO.setRoomSize(roomSize);
        refundHistoryDO.setStartTime(startTime);
        refundHistoryDO.setSurname(surname);
        refundHistoryDO.setTollCollectorId(tollCollectorId);
        refundHistoryDO.setUdt(udt);
        refundHistoryDO.setUnitCode(unitCode);
        refundHistoryDO.setUnitName(unitName);
        refundHistoryDO.setCustomerUserId(customerUserId);
        refundHistoryDO.setVillageCode(villageCode);
        refundHistoryDO.setVillageName(villageName);
        refundDAO.save(refundHistoryDO);
    }

    @Override
    public PageData findByPage(int pageNum, int pageSize, String userId,String villageCode,String regionCode, String buildingCode, String unitCode, String roomCode, String mobilePhone,String idNumber,String surname) {

//        int pageNo = (pageNum-1)*pageSize;
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnable(organizationId, Constants.VILLAGE_ENABLE);
        if (villageDOS.size()==0){
            VillageDO villageDO = new VillageDO();
            villageDO.setVillageCode(Constants.VILLAGE_CODE_SET);
            villageDOS.add(villageDO);
        }

        Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<RefundHistoryDO> page = refundDAO.findAll(new Specification<RefundHistoryDO>() {
            @Override
            public Predicate toPredicate(Root<RefundHistoryDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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
                }
                if (StringUtils.isNotBlank(unitCode)){
                    predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("unitCode"),unitCode)));
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
        List<RefundAndApplicationDTO> refundAndApplicationDTOS = new ArrayList<>();
        List<RefundHistoryDO> refundHistoryDOS = pageData.getContents();

        for (RefundHistoryDO refundHistoryDO:refundHistoryDOS) {
            RefundAndApplicationDTO refundAndApplicationDTO = new RefundAndApplicationDTO();
            BeanUtils.copyProperties(refundHistoryDO,refundAndApplicationDTO);
            RefundApplicationDO refundApplicationDO = refundApplicationDAO.findByOrderId(refundHistoryDO.getOrderId());
            if (null == refundApplicationDO){
                refundAndApplicationDTOS.add(refundAndApplicationDTO);
                continue;
            }
            BeanUtils.copyProperties(refundApplicationDO,refundAndApplicationDTO);
            refundAndApplicationDTOS.add(refundAndApplicationDTO);
        }
        pageData.setContents(refundAndApplicationDTOS);
        return pageData;
    }

    @Override
    public PageData findNotByPage(int pageNum,int pageSize,String userId,String villageCode,String regionCode, String buildingCode, String unitCode, String roomCode, String mobilePhone,String idNumber,String surname) {
//        int pageNo = (pageNum-1)*pageSize;
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnable(organizationId, Constants.VILLAGE_ENABLE);
        if (villageDOS.size()==0){
            VillageDO villageDO = new VillageDO();
            villageDO.setVillageCode(Constants.VILLAGE_CODE_SET);
            villageDOS.add(villageDO);
        }

        Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<RefundHistoryDO> page = refundDAO.findAll(new Specification<RefundHistoryDO>() {
            @Override
            public Predicate toPredicate(Root<RefundHistoryDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"), userInfoDO.getOrganizationId())));
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("invalidState"), 0)));
//                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("refundStatus"), 0)));
//                List<Integer> refundStatus = new ArrayList<>();
//                refundStatus.add(0);
//                refundStatus.add(2);
                predicateList.add(criteriaBuilder.not(criteriaBuilder.equal(root.get("refundStatus"), 1)));
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
        List<RefundAndApplicationDTO> refundAndApplicationDTOS = new ArrayList<>();
        List<RefundHistoryDO> refundHistoryDOS = pageData.getContents();

        for (RefundHistoryDO refundHistoryDO:refundHistoryDOS) {
            RefundAndApplicationDTO refundAndApplicationDTO = new RefundAndApplicationDTO();
            BeanUtils.copyProperties(refundHistoryDO,refundAndApplicationDTO);
            RefundApplicationDO refundApplicationDO = refundApplicationDAO.findByOrderId(refundHistoryDO.getOrderId());
            if (null == refundApplicationDO){
                refundAndApplicationDTOS.add(refundAndApplicationDTO);
                continue;
            }
            BeanUtils.copyProperties(refundApplicationDO,refundAndApplicationDTO);
            refundAndApplicationDTOS.add(refundAndApplicationDTO);
        }
        pageData.setContents(refundAndApplicationDTOS);
        return pageData;
    }

    @Override
    public PageData findAlreadyByPage(int pageNum,int pageSize,String userId,String villageCode,String regionCode, String buildingCode, String unitCode, String roomCode, String mobilePhone,String idNumber,String surname) {

        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnable(organizationId, Constants.VILLAGE_ENABLE);
        if (villageDOS.size()==0){
            VillageDO villageDO = new VillageDO();
            villageDO.setVillageCode(Constants.VILLAGE_CODE_SET);
            villageDOS.add(villageDO);
        }
        int refundStatus = 1;

        Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<RefundHistoryDO> page = refundDAO.findAll(new Specification<RefundHistoryDO>() {
            @Override
            public Predicate toPredicate(Root<RefundHistoryDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"), userInfoDO.getOrganizationId())));
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("invalidState"), 0)));
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("refundStatus"), 1)));
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
                if (StringUtils.isNotBlank(mobilePhone)){
                    predicateList.add(criteriaBuilder.like(root.get("mobilePhone").as(String.class),"%"+mobilePhone+"%"));
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
        List<RefundAndApplicationDTO> refundAndApplicationDTOS = new ArrayList<>();
        List<RefundHistoryDO> refundHistoryDOS = pageData.getContents();

        for (RefundHistoryDO refundHistoryDO:refundHistoryDOS) {
            RefundAndApplicationDTO refundAndApplicationDTO = new RefundAndApplicationDTO();
            BeanUtils.copyProperties(refundHistoryDO,refundAndApplicationDTO);
            RefundApplicationDO refundApplicationDO = refundApplicationDAO.findByOrderId(refundHistoryDO.getOrderId());
            BeanUtils.copyProperties(refundApplicationDO,refundAndApplicationDTO);
            refundAndApplicationDTOS.add(refundAndApplicationDTO);
        }
        pageData.setContents(refundAndApplicationDTOS);
        return pageData;
    }




    @Override
    @Transactional
    public void update(RefundHistoryDO refundHistoryDO) throws ParseException {
        refundHistoryDO.setRefundableAmount(refundHistoryDO.getRefundableAmount()-refundHistoryDO.getMortgageAmount());
        if (null!=refundHistoryDO.getStartTime()&&!"".equals(refundHistoryDO.getStartTime())&&null!=refundHistoryDO.getDelayTime()){
            refundHistoryDO.setDueTime(DateUtil.getAfterDay(refundHistoryDO.getDueTime(),refundHistoryDO.getDelayTime()));
        }
        refundHistoryDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        refundDAO.saveAndFlush(refundHistoryDO);
    }

    @Override
    public void delete(List<String> ids) {
        String idList = Joiner.on(",").join(ids).replaceAll("'","");
        iRefundDAO.deleteData(idList);
    }

    @Override
    @Transactional
    public void application(RefundApplicationDO refundApplicationDO) {
        refundApplicationDO.setMortgageAmount(refundApplicationDO.getActualMoneyCollection()-refundApplicationDO.getRefundableAmount());
        refundApplicationDO.setAuditStatus(1);
        refundApplicationDO.setSurplusDeductibleMoney(0d);
        refundApplicationDO.setDeductibledMoney(0d);
        refundApplicationDO.setDeductibleMoney(0d);
        refundApplicationDO.setAmountDeductedThisTime(0d);
        refundApplicationDO.setDeductionRecord("0");
        refundApplicationDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        refundApplicationDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        RefundHistoryDO refundHistoryDO = refundDAO.findByOrderId(refundApplicationDO.getOrderId());
        refundHistoryDO.setRefundStatus(2);
        refundApplicationDAO.save(refundApplicationDO);
        refundDAO.saveAndFlush(refundHistoryDO);
    }

    @Override
    @Transactional
    public void preliminaryExaminationPass(int id) {
        RefundApplicationDO refundApplicationDO = refundApplicationDAO.findById(id);
        refundApplicationDO.setAuditStatus(2);
        refundApplicationDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        refundApplicationDAO.saveAndFlush(refundApplicationDO);
    }

    @Override
    @Transactional
    public void reviewPass(int id,String userId) throws Exception{
        RefundApplicationDO refundApplicationDO = refundApplicationDAO.findById(id);
        refundApplicationDO.setAuditStatus(3);
        refundApplicationDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        RefundHistoryDO refundHistoryDO = refundDAO.findByOrderId(refundApplicationDO.getOrderId());
        refundHistoryDO.setRefundStatus(2);
        /**
         * 处理退款任务
         */
        String organizationId = refundHistoryDO.getOrganizationId();
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);
        String orderId = thisSystemOrderIdService.thisSystemOrderId(organizationId);
//        String time = DateUtil.date(DateUtil.DATE_FORMAT_PATTERN);
//        Integer orderIdsCount = iBillDAO.findOrderIdsByorganizationId(organizationId,time);
//
//        if (orderIdsCount<9){
//            orderIdsCount = orderIdsCount+1;
//            orderId = time+"000"+orderIdsCount;
//        }else if (orderIdsCount>=9&&orderIdsCount<99){
//            orderIdsCount = orderIdsCount+1;
//            orderId = time+"00"+orderIdsCount;
//        }else if (orderIdsCount>=99&&orderIdsCount<999){
//            orderIdsCount = orderIdsCount+1;
//            orderId = time+"0"+orderIdsCount;
//        }else {
//            orderIdsCount = orderIdsCount+1;
//            orderId = time+orderIdsCount;
//        }

        RoomDO roomDO = roomDAO.findByRoomCode(refundHistoryDO.getRoomCode());
        String chargeCode = iBillDAO.findChargeCodeByRoomCode(refundHistoryDO.getRoomCode(),id);
        ChargeItemDO chargeItemDO = chargeItemDAO.findByChargeCodeAndOrganizationId(chargeCode,organizationId);

        JSONObject json = new JSONObject();
        json.put("roomSize",roomDO.getRoomSize());
        json.put("datedif",1);
        json.put("chargeCode",chargeCode);
        json.put("chargeName",chargeItemDO.getChargeName());
        json.put("chargeType",chargeItemDO.getChargeType());
//        List<String> chargeCodeList = iFrontOfficeCashierDao.findChargeCodesByRoomCodeAndChargeStatus(roomAndCouponDO.getRoomCode(),1);
//        String chargeCodes = Joiner.on(",").join(chargeCodeList);
//        String dueTime = iFrontOfficeCashierDao.findMaxDueTime(roomAndCouponDO.getRoomCode(),chargeCodes);
//
//        if ("null".equals(dueTime)||null==dueTime||"".equals(dueTime)){
//            RoomAndCustomerDO roomAndCustomerDO = roomAndCustomerDAO.findByUserIdAndRoomCodeAndLoggedOffState(roomAndCouponDO.getUserId(),roomAndCouponDO.getRoomCode(),0);
////                查询房子开始计物业费时间
//            dueTime = roomAndCustomerDO.getStartBillingTime();
//        }

        /**
         * 统计物业费到期时间
         */
        String dueTime = propertyFeeDueTimeService.propertyFeeDueTime(refundHistoryDO.getRoomCode(),refundHistoryDO.getOrganizationId());

        if (!DateUtil.isFirstDayOfMonth(DateUtil.strToDateLong(dueTime,DateUtil.YEAR_MONTH_DAY))){
            throw new DueTimeException();
        }

        json.put("startTime",dueTime);

        BillDetailedDO billDetailedDO = iChargeDAO.findPropertyCharge(chargeCode,1);

        ICostStrategy iCostStrategy = new PropertyFeeCostImpl();
        ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
//        try {
        BillDetailedDO billDetailedBackDO = iCostStrategyImpl.costCalculationMethod(json,billDetailedDO);
        double money = refundApplicationDO.getDeductionPropertyFee();
        refundApplicationDO.setDeductibleMoney(money);
        double amountReceivable1 = billDetailedBackDO.getAmountReceivable();

        /**
         * 计算物业费到期时间
         */
        int datedif = (int) (money/amountReceivable1);
        billDetailedBackDO.setDatedif(datedif);
        String dueTime0 = DateUtil.getAfterMonth(billDetailedBackDO.getStartTime(), datedif);

        billDetailedBackDO.setDueTime(dueTime0);
        if(datedif>0) {
            double amountReceivable = billDetailedBackDO.getAmountReceivable() * datedif;
            double surplusDeductibleMoney = money - amountReceivable;
            refundApplicationDO.setDeductibledMoney(amountReceivable);
            if (amountReceivable > 0) {
                refundApplicationDO.setDeductionRecord(String.valueOf(amountReceivable));
            } else {
                refundApplicationDO.setDeductionRecord("0");
            }
            /**
             * 计算应收实收金额
             */
            billDetailedBackDO.setActualMoneyCollection(amountReceivable);
            billDetailedBackDO.setAmountReceivable(amountReceivable);
            billDetailedBackDO.setSplitState(0);
            billDetailedBackDO.setStateOfArrears(0);
            billDetailedBackDO.setPayerName(refundHistoryDO.getSurname());
            billDetailedBackDO.setPayerUserId(refundHistoryDO.getCustomerUserId());
            billDetailedBackDO.setOrderId(orderId);
            billDetailedBackDO.setDeductionRecord("0");
            billDetailedBackDO.setDeductibledMoney(0d);
            billDetailedBackDO.setDeductibleMoney(0d);
//            billDetailedBackDO.setOriginalStateOfArrears(0);
            billDetailedBackDO.setCode(Integer.parseInt(Tools.random(100000,999999)));
            billDetailedBackDO.setParentCode(0);
            billDetailedBackDO.setPayerPhone(refundHistoryDO.getPayerPhone());
            billDetailedBackDO.setSurplusDeductibleMoney(0d);
            billDetailedBackDO.setAmountDeductedThisTime(0d);
            billDetailedBackDO.setOrganizationId(organizationId);
            billDetailedBackDO.setArrearsType(billDetailedBackDO.getChargeType());
            billDetailedBackDO.setDeductibleSurplus(surplusDeductibleMoney);
            Integer stateOfArrears = 0;
            Integer refundStatus = 0;
            Integer invalidState = 0;

            BillDO billDO = new BillDO();
            billDO.setOrderId(orderId);
            billDO.setRealGenerationTime(DateUtil.date(DateUtil.FORMAT_PATTERN));
            billDO.setCustomerUserId(refundHistoryDO.getCustomerUserId());
            billDO.setRoomSize(roomDO.getRoomSize());
            billDO.setOrganizationId(organizationId);
            billDO.setOrganizationName(organizationDO.getOrganizationName());
            billDO.setVillageCode(refundHistoryDO.getVillageCode());
            billDO.setVillageName(refundHistoryDO.getVillageName());
            billDO.setRegionCode(refundHistoryDO.getRegionCode());
            billDO.setRegionName(refundHistoryDO.getRegionName());
            billDO.setBuildingCode(refundHistoryDO.getBuildingCode());
            billDO.setBuildingName(refundHistoryDO.getBuildingName());
            billDO.setUnitCode(refundHistoryDO.getUnitCode());
            billDO.setUnitName(refundHistoryDO.getUnitName());
            billDO.setRoomCode(refundHistoryDO.getRoomCode());
            billDO.setSurname(refundHistoryDO.getSurname());
            billDO.setMobilePhone(refundHistoryDO.getMobilePhone());
            billDO.setPaymentMethod("32");
            billDO.setTollCollectorId(userId);
            billDO.setTollCollectorName(userInfoDO.getRealName());
//            billDO.setStateOfArrears(stateOfArrears);
            billDO.setRefundStatus(refundStatus);
            billDO.setInvalidState(invalidState);
            billDO.setCorrectedAmount(0d);
            billDO.setRemark(refundHistoryDO.getChargeName()+"退款，退款金额为："+amountReceivable+"元,剩余金额为："+surplusDeductibleMoney+"元,剩余金额会自动存入账户，下次缴物业费时作抵扣。");
            billDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
            billDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));


            JSONArray jsonArray = new JSONArray();
            jsonArray.add(billDetailedBackDO);
//                billDO.setDetailed(jsonArray.toJSONString());
            billDO.setAmountTotalReceivable(amountReceivable);
            billDO.setActualTotalMoneyCollection(amountReceivable);
//                billDO.setPreferentialTotalAmount(0d);

            List<OriginalBillDO> originalBillDOS = new ArrayList<OriginalBillDO>();

            String startTime = billDetailedDO.getStartTime();
            for (int i = 0; i < datedif; i++) {
                OriginalBillDO originalBillDO = new OriginalBillDO();
                originalBillDO.setOrderId(orderId);
                originalBillDO.setCustomerUserId(billDO.getCustomerUserId());
                originalBillDO.setOrganizationId(organizationId);
                originalBillDO.setOrganizationName(organizationDO.getOrganizationName());
                originalBillDO.setVillageCode(refundHistoryDO.getVillageCode());
                originalBillDO.setVillageName(refundHistoryDO.getVillageName());
                originalBillDO.setRegionCode(refundHistoryDO.getRegionCode());
                originalBillDO.setRegionName(refundHistoryDO.getRegionName());
                originalBillDO.setBuildingCode(refundHistoryDO.getBuildingCode());
                originalBillDO.setBuildingName(refundHistoryDO.getBuildingName());
                originalBillDO.setUnitCode(refundHistoryDO.getUnitCode());
                originalBillDO.setUnitName(refundHistoryDO.getUnitName());
                originalBillDO.setRoomCode(refundHistoryDO.getRoomCode());
                originalBillDO.setRoomSize(roomDO.getRoomSize());
                originalBillDO.setSurname(refundHistoryDO.getSurname());
                originalBillDO.setMobilePhone(refundHistoryDO.getMobilePhone());
                originalBillDO.setPaymentMethod(refundHistoryDO.getChargeName());
                originalBillDO.setTollCollectorId(userId);
                originalBillDO.setStateOfArrears(stateOfArrears);
                originalBillDO.setRefundStatus(refundStatus);
                originalBillDO.setInvalidState(invalidState);
                originalBillDO.setRemark(refundHistoryDO.getChargeName()+"退款，退款金额为："+amountReceivable+"元,剩余金额为："+surplusDeductibleMoney+"元。");
                originalBillDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                originalBillDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));

                originalBillDO.setChargeCode(billDetailedDO.getChargeCode());
                originalBillDO.setChargeName(billDetailedDO.getChargeName());
                originalBillDO.setChargeUnit(billDetailedDO.getChargeUnit());
                originalBillDO.setChargeStandard(billDetailedDO.getChargeStandard());
                originalBillDO.setCurrentReadings(billDetailedDO.getCurrentReadings());
                originalBillDO.setLastReading(billDetailedDO.getLastReading());
                originalBillDO.setUsageAmount(billDetailedDO.getUsageAmount());
                originalBillDO.setDiscount(billDetailedDO.getDiscount());
                originalBillDO.setDatedif(billDetailedDO.getDatedif());
                originalBillDO.setChargeType(billDetailedDO.getChargeType());

                originalBillDO.setAmountReceivable(billDetailedBackDO.getAmountReceivable() / datedif);
                originalBillDO.setActualMoneyCollection(billDetailedBackDO.getAmountReceivable() / datedif);
                originalBillDO.setPreferentialAmount(0d);
                originalBillDO.setStartTime(startTime);
                if (null != startTime) {
                    startTime = DateUtil.getAfterMonth(startTime, 1);
                }
                originalBillDO.setDueTime(startTime);
                originalBillDOS.add(originalBillDO);
            }
            refundApplicationDO.setAmountDeductedThisTime(amountReceivable);
            refundApplicationDO.setSurplusDeductibleMoney(surplusDeductibleMoney);
            if (surplusDeductibleMoney > 0) {
                refundHistoryDO.setRefundStatus(2);
            } else {
                refundHistoryDO.setRefundStatus(1);
            }
            redisUtil.set(Constants.REDISKEY_PROJECT + Constants.PROPERTY_DUE_TIME +refundHistoryDO.getOrganizationId()+"-"+ refundHistoryDO.getRoomCode(), "NAN");
            billDetailedDAO.save(billDetailedBackDO);
            billDAO.save(billDO);
            originalBillDAO.saveAll(originalBillDOS);
            refundApplicationDAO.saveAndFlush(refundApplicationDO);
            refundDAO.saveAndFlush(refundHistoryDO);
            EventRecordDO eventRecordDO = new EventRecordDO();
            eventRecordDO.setOrganizationId(organizationId);
            eventRecordDO.setEventCode(orderId);
            eventRecordDO.setEventName("退款提醒");
            eventRecordDO.setEventDescripte("房间号："+refundHistoryDO.getRoomCode()+",业主："+refundHistoryDO.getSurname()+",退款复审通过！");
            eventRecordDO.setEventType("1");
            eventRecordDO.setOccurTime(DateUtil.date(DateUtil.FORMAT_PATTERN));
            eventRecordDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
            eventRecordDAO.save(eventRecordDO);
        }else {
            double surplusDeductibleMoney = money;
            refundApplicationDO.setDeductionRecord("0");
            refundApplicationDO.setAmountDeductedThisTime(0d);
            refundApplicationDO.setDeductibledMoney(0d);
            refundApplicationDO.setSurplusDeductibleMoney(surplusDeductibleMoney);
            if (surplusDeductibleMoney > 0) {
                refundHistoryDO.setRefundStatus(2);
            } else {
                refundHistoryDO.setRefundStatus(1);
            }

            refundDAO.saveAndFlush(refundHistoryDO);
            refundApplicationDAO.saveAndFlush(refundApplicationDO);
        }

    }

    @Override
    @Transactional
    public void examineNoPass(int id) {
        RefundApplicationDO refundApplicationDO = refundApplicationDAO.findById(id);
        refundApplicationDO.setAuditStatus(0);
        RefundHistoryDO refundHistoryDO = refundDAO.findByOrderId(refundApplicationDO.getOrderId());
//        refundHistoryDO.setRefundStatus(0);
        refundApplicationDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        refundApplicationDAO.saveAndFlush(refundApplicationDO);
        refundDAO.saveAndFlush(refundHistoryDO);
    }

    @Override
    public PageData findApplicationByPage(int pageNum,int pageSize,String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        int pageNo = (pageNum-1)*pageSize;
        List<RefundAndApplicationDTO> refundAndApplicationDTOS = iRefundApplicationDAO.findByPage(organizationId,pageNo,pageSize);
        int totalCount = iRefundApplicationDAO.findByPageCount(organizationId);
        int totalPage;
        if (totalCount%pageSize==0){
            totalPage = totalCount/pageSize;
        }else {
            totalPage = totalCount/pageSize+1;
        }
        return new PageData(pageNum,pageSize,totalPage,totalCount,refundAndApplicationDTOS);
    }

    @Override
    public PageData findApplicationByWaitAuditPage(int pageNum,int pageSize,String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        int pageNo = (pageNum-1)*pageSize;
        List<RefundAndApplicationDTO> refundAndApplicationDTOS = iRefundApplicationDAO.findByPageAndAudit(organizationId,1,pageNo,pageSize);
        int totalCount = iRefundApplicationDAO.findByPageCount(organizationId);
        int totalPage;
        if (totalCount%pageSize==0){
            totalPage = totalCount/pageSize;
        }else {
            totalPage = totalCount/pageSize+1;
        }
        return new PageData(pageNum,pageSize,totalPage,totalCount,refundAndApplicationDTOS);
    }

    @Override
    public PageData findApplicationByWaitReviewPage(int pageNum,int pageSize,String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        int pageNo = (pageNum-1)*pageSize;
        List<RefundAndApplicationDTO> refundAndApplicationDTOS = iRefundApplicationDAO.findByPageAndAudit(organizationId,2,pageNo,pageSize);
        int totalCount = iRefundApplicationDAO.findByPageCount(organizationId);
        int totalPage;
        if (totalCount%pageSize==0){
            totalPage = totalCount/pageSize;
        }else {
            totalPage = totalCount/pageSize+1;
        }
        return new PageData(pageNum,pageSize,totalPage,totalCount,refundAndApplicationDTOS);
    }

    @Override
    public PageData findApplicationByAlreadyAuditPage(int pageNum,int pageSize,String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        int pageNo = (pageNum-1)*pageSize;
        List<RefundAndApplicationDTO> refundAndApplicationDTOS = iRefundApplicationDAO.findByPageAndAudit(organizationId,3,pageNo,pageSize);
        int totalCount = iRefundApplicationDAO.findByPageCount(organizationId);
        int totalPage;
        if (totalCount%pageSize==0){
            totalPage = totalCount/pageSize;
        }else {
            totalPage = totalCount/pageSize+1;
        }
        return new PageData(pageNum,pageSize,totalPage,totalCount,refundAndApplicationDTOS);
    }

    @Override
    public void updateApplication(JSONObject jsonObject, String userId) {

        int id = jsonObject.getInteger("id");
        double deductionPropertyFee = jsonObject.getDouble("deductionPropertyFee");
        double transferCardAmount = jsonObject.getDouble("transferCardAmount");
        RefundApplicationDO refundApplicationDO = refundApplicationDAO.findById(id);
        refundApplicationDO.setDeductionPropertyFee(deductionPropertyFee);
        refundApplicationDO.setTransferCardAmount(transferCardAmount);
        String remarks = jsonObject.getString("remarks");
        refundApplicationDO.setRemark(remarks);
        refundApplicationDO.setAuditStatus(1);
        refundApplicationDAO.saveAndFlush(refundApplicationDO);
    }
}
