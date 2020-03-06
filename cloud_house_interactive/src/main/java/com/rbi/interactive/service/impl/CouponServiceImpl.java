package com.rbi.interactive.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.rbi.interactive.abnormal.DueTimeException;
import com.rbi.interactive.dao.*;
import com.rbi.interactive.entity.*;
import com.rbi.interactive.entity.dto.BillsBackDTO;
import com.rbi.interactive.entity.dto.ChargeItemCostDTO;
import com.rbi.interactive.service.CouponService;
import com.rbi.interactive.service.ThisSystemOrderIdService;
import com.rbi.interactive.service.impl.strategy.ICostStrategy;
import com.rbi.interactive.service.PropertyFeeDueTimeService;
import com.rbi.interactive.service.impl.charge.PropertyFeeCostImpl;
import com.rbi.interactive.service.impl.strategy.ICostStrategyImpl;
import com.rbi.interactive.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    private final static Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Autowired
    ThisSystemOrderIdService thisSystemOrderIdService;

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired(required = false)
    IRoomAndCouponDAO iRoomAndCouponDAO;

    @Autowired
    OrganizationDAO organizationDAO;

    @Autowired
    RoomAndCouponDAO roomAndCouponDAO;

    @Autowired
    CouponDAO couponDAO;

    @Autowired(required = false)
    IBillDAO iBillDAO;

    @Autowired
    RoomDAO roomDAO;

    @Autowired
    ChargeItemDAO chargeItemDAO;

    @Autowired(required = false)
    IFrontOfficeCashierDao iFrontOfficeCashierDao;

    @Autowired
    RoomAndCustomerDAO roomAndCustomerDAO;

    @Autowired(required = false)
    IChargeDAO iChargeDAO;

    @Autowired
    BillDAO billDAO;

    @Autowired
    OriginalBillDAO originalBillDAO;

    @Autowired
    VillageDAO villageDAO;

    @Autowired
    PropertyFeeDueTimeService propertyFeeDueTimeService;

    @Autowired
    BillDetailedDAO billDetailedDAO;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public PageData findCouponPage(int pageNum, int pageSize, String userId,String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode,String mobilePhone,String idNumber,String surname) {

        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();

        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnable(organizationId, Constants.VILLAGE_ENABLE);

        if (villageDOS.size()==0){
            VillageDO villageDO = new VillageDO();
            villageDO.setVillageCode(Constants.VILLAGE_CODE_SET);
            villageDOS.add(villageDO);
        }

        Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<RoomAndCouponDO> page = roomAndCouponDAO.findAll(new Specification<RoomAndCouponDO>() {
            @Override
            public Predicate toPredicate(Root<RoomAndCouponDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"), userInfoDO.getOrganizationId())));

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

        return new PageData(page);
    }

    @Override
    public PageData findWaitAuditPage(int pageNum, int pageSize, String userId,String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode,String mobilePhone,String idNumber,String surname) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnable(organizationId, Constants.VILLAGE_ENABLE);
        if (villageDOS.size()==0){
            VillageDO villageDO = new VillageDO();
            villageDO.setVillageCode(Constants.VILLAGE_CODE_SET);
            villageDOS.add(villageDO);
        }

        int auditStatus = 1;
        Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<RoomAndCouponDO> page = roomAndCouponDAO.findAll(new Specification<RoomAndCouponDO>() {
            @Override
            public Predicate toPredicate(Root<RoomAndCouponDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"), userInfoDO.getOrganizationId())));
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("auditStatus"), auditStatus)));
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

        return new PageData(page);
    }

    @Override
    public PageData findWaitAgainAuditPage(int pageNum, int pageSize, String userId,String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode,String mobilePhone,String idNumber,String surname) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnable(organizationId, Constants.VILLAGE_ENABLE);
        if (villageDOS.size()==0){
            VillageDO villageDO = new VillageDO();
            villageDO.setVillageCode(Constants.VILLAGE_CODE_SET);
            villageDOS.add(villageDO);
        }

        int auditStatus = 2;
        Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<RoomAndCouponDO> page = roomAndCouponDAO.findAll(new Specification<RoomAndCouponDO>() {
            @Override
            public Predicate toPredicate(Root<RoomAndCouponDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"), userInfoDO.getOrganizationId())));
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("auditStatus"), auditStatus)));
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

        return new PageData(page);
    }

    @Override
    public PageData findAlreadyAuditPage(int pageNum, int pageSize, String userId,String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode,String mobilePhone,String idNumber,String surname) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnable(organizationId, Constants.VILLAGE_ENABLE);
        if (villageDOS.size()==0){
            VillageDO villageDO = new VillageDO();
            villageDO.setVillageCode(Constants.VILLAGE_CODE_SET);
            villageDOS.add(villageDO);
        }

        int auditStatus = 3;
        Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<RoomAndCouponDO> page = roomAndCouponDAO.findAll(new Specification<RoomAndCouponDO>() {
            @Override
            public Predicate toPredicate(Root<RoomAndCouponDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"), userInfoDO.getOrganizationId())));
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("auditStatus"), auditStatus)));
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

        return new PageData(page);
    }

    @Override
    public List<CouponDO> findByOrganizationId(String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        List<CouponDO> couponDOS = couponDAO.findByOrganizationId(userInfoDO.getOrganizationId());
        return couponDOS;
    }

    @Override
    public CouponDO findByCouponCode(String couponCode,String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        return couponDAO.findByCouponCodeAndOrganizationId(couponCode,userInfoDO.getOrganizationId());
    }


    @Override
    @Transactional
    public void add(JSONObject jsonObject, String userId) throws ParseException {
        RoomAndCouponDO roomAndCouponDO = JSONObject.parseObject(jsonObject.toJSONString(),RoomAndCouponDO.class);
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(userInfoDO.getOrganizationId());
        CouponDO couponDO = couponDAO.findByCouponCodeAndOrganizationId(roomAndCouponDO.getCouponCode(),organizationDO.getOrganizationId());
        roomAndCouponDO.setChargeCode(couponDO.getChargeCode());
        roomAndCouponDO.setOrganizationName(organizationDO.getOrganizationName());
        roomAndCouponDO.setOrganizationId(organizationDO.getOrganizationId());
        roomAndCouponDO.setAuditStatus(1);
        roomAndCouponDO.setPropertyFee(0d);
        roomAndCouponDO.setSurplusDeductibleMoney(roomAndCouponDO.getMoney());
        roomAndCouponDO.setUsageState(0);
        roomAndCouponDO.setSurplusDeductibleMoney(0d);
        roomAndCouponDO.setAmountDeductedThisTime(0d);
        roomAndCouponDO.setDeductibledMoney(0d);
        roomAndCouponDO.setDeductibleMoney(0d);
        roomAndCouponDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        roomAndCouponDO.setStartTime(DateUtil.date(DateUtil.YEAR_MONTH_DAY));
        if (roomAndCouponDO.getEffectiveTime()!=0d){
            String endTime = DateUtil.stampToDate(String.valueOf(DateUtil.timeStamp()+roomAndCouponDO.getEffectiveTime().longValue()*24*60*60*1000l));
            roomAndCouponDO.setEndTime(endTime);
        }
        roomAndCouponDO.setPastDue(0);
        roomAndCouponDO.setUdt(DateUtil.date(DateUtil.YEAR_MONTH_DAY));
        roomAndCouponDAO.save(roomAndCouponDO);
    }

    @Override
    public void delete(List<Integer> ids) {
        roomAndCouponDAO.deleteByIdIn(ids);
    }

    @Override
    @Transactional
    public void auditNoPass(int id) {
        RoomAndCouponDO roomAndCouponDO = roomAndCouponDAO.findById(id);
        roomAndCouponDO.setAuditStatus(0);
        roomAndCouponDAO.saveAndFlush(roomAndCouponDO);
    }

    @Override
    @Transactional
    public void audit(int id) {
        RoomAndCouponDO roomAndCouponDO = roomAndCouponDAO.findById(id);
        roomAndCouponDO.setAuditStatus(2);
        roomAndCouponDAO.saveAndFlush(roomAndCouponDO);
    }

    @Override
    @Transactional
    public BillsBackDTO againAudit(int id, HttpServletRequest request) throws Exception {
        RoomAndCouponDO roomAndCouponDO = roomAndCouponDAO.findById(id);

        /**
         * 处理扣费情况
         */
        String organizationId = roomAndCouponDO.getOrganizationId();
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

        RoomDO roomDO = roomDAO.findByRoomCode(roomAndCouponDO.getRoomCode());
        ChargeItemDO chargeItemDO = chargeItemDAO.findByChargeCodeAndOrganizationId(roomAndCouponDO.getChargeCode(),organizationId);

        JSONObject json = new JSONObject();
        json.put("roomSize",roomDO.getRoomSize());
        json.put("datedif",1);
        json.put("chargeCode",roomAndCouponDO.getChargeCode());
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
        String dueTime = propertyFeeDueTimeService.propertyFeeDueTime(roomAndCouponDO.getRoomCode(),roomAndCouponDO.getOrganizationId());

        //判断不是月份的第一天抛出异常
        if (!DateUtil.isFirstDayOfMonth(DateUtil.strToDateLong(dueTime,DateUtil.YEAR_MONTH_DAY))){
            throw new DueTimeException();
        }

        json.put("startTime",dueTime);
        BillDetailedDO billDetailedDO = iChargeDAO.findPropertyCharge(roomAndCouponDO.getChargeCode(),1);

        ICostStrategy iCostStrategy = new PropertyFeeCostImpl();
        ICostStrategyImpl iCostStrategyImpl = new ICostStrategyImpl(iCostStrategy);
        BillsBackDTO billsBackDTO = new BillsBackDTO();
//        try {
        BillDetailedDO billDetailedBackDO = iCostStrategyImpl.costCalculationMethod(json,billDetailedDO);
        double money = roomAndCouponDO.getMoney();
        roomAndCouponDO.setDeductibleMoney(money);
        double amountReceivable1 = billDetailedBackDO.getAmountReceivable();

        /**
         * 计算物业费到期时间
         */
        int datedif = (int) (money/amountReceivable1);
        billDetailedBackDO.setDatedif(datedif);
        String dueTime0 = DateUtil.getAfterMonth(billDetailedBackDO.getStartTime(), datedif);

        billDetailedBackDO.setDueTime(dueTime0);
        if(datedif>0){
            String userId = JwtToken.getClaim(request.getHeader("appkey"),"userId");
            UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
            double amountReceivable = Tools.moneyHalfAdjust(billDetailedBackDO.getAmountReceivable()*datedif);
            double surplusDeductibleMoney = Tools.moneyHalfAdjust(roomAndCouponDO.getMoney()-amountReceivable);
            roomAndCouponDO.setDeductibledMoney(amountReceivable);
            if (amountReceivable>0){
                roomAndCouponDO.setDeductionRecord(String.valueOf(amountReceivable));
            }else {
                roomAndCouponDO.setDeductionRecord("0");
            }
            /**
             * 计算应收实收金额
             */
            billDetailedBackDO.setActualMoneyCollection(amountReceivable);
            billDetailedBackDO.setAmountReceivable(amountReceivable);
            billDetailedBackDO.setOrderId(orderId);
            billDetailedBackDO.setPayerPhone(roomAndCouponDO.getMobilePhone());
            billDetailedBackDO.setPayerName(roomAndCouponDO.getSurname());
            billDetailedBackDO.setPayerUserId(roomAndCouponDO.getCustomerUserId());
            billDetailedBackDO.setDeductionRecord("0");
            billDetailedBackDO.setDeductibledMoney(0d);
            billDetailedBackDO.setDeductibleMoney(0d);
//            billDetailedBackDO.setOriginalStateOfArrears(0);
            billDetailedBackDO.setCode(Integer.parseInt(Tools.random(100000,999999)));
            billDetailedBackDO.setParentCode(0);
            billDetailedBackDO.setSplitState(0);
            billDetailedBackDO.setStateOfArrears(0);
            billDetailedBackDO.setPayerName(roomAndCouponDO.getSurname());
            billDetailedBackDO.setPayerUserId(roomAndCouponDO.getCustomerUserId());
            billDetailedBackDO.setPayerPhone(roomAndCouponDO.getMobilePhone());
            billDetailedBackDO.setOrganizationId(organizationId);
            billDetailedBackDO.setSurplusDeductibleMoney(0d);
            billDetailedBackDO.setAmountDeductedThisTime(0d);
            billDetailedBackDO.setArrearsType(billDetailedBackDO.getChargeType());
            billDetailedBackDO.setDeductibleSurplus(surplusDeductibleMoney);
            Integer stateOfArrears = 0;
            Integer refundStatus = 0;
            Integer invalidState = 0;

            BillDO billDO = new BillDO();
            billDO.setOrderId(orderId);
            billDO.setIdNumber(roomAndCouponDO.getIdNumber());
            billDO.setRealGenerationTime(DateUtil.date(DateUtil.FORMAT_PATTERN));
            billDO.setCustomerUserId(roomAndCouponDO.getCustomerUserId());
            billDO.setRoomSize(roomDO.getRoomSize());
            billDO.setOrganizationId(organizationId);
            billDO.setOrganizationName(organizationDO.getOrganizationName());
            billDO.setVillageCode(roomAndCouponDO.getVillageCode());
            billDO.setVillageName(roomAndCouponDO.getVillageName());
            billDO.setRegionCode(roomAndCouponDO.getRegionCode());
            billDO.setRegionName(roomAndCouponDO.getRegionName());
            billDO.setBuildingCode(roomAndCouponDO.getBuildingCode());
            billDO.setBuildingName(roomAndCouponDO.getBuildingName());
            billDO.setUnitCode(roomAndCouponDO.getUnitCode());
            billDO.setUnitName(roomAndCouponDO.getUnitName());
            billDO.setRoomCode(roomAndCouponDO.getRoomCode());
            billDO.setSurname(roomAndCouponDO.getSurname());
            billDO.setMobilePhone(roomAndCouponDO.getMobilePhone());
            billDO.setPaymentMethod("31");
            billDO.setTollCollectorId(userId);
            billDO.setTollCollectorName(userInfoDO.getRealName());
//            billDO.setStateOfArrears(stateOfArrears);
            billDO.setRefundStatus(refundStatus);
            billDO.setInvalidState(invalidState);
            billDO.setCorrectedAmount(0d);
            billDO.setRemark(roomAndCouponDO.getCouponName()+"抵扣,抵扣金额为："+amountReceivable+"元,剩余金额为："+surplusDeductibleMoney+"元,剩余金额会自动存入账户，下次缴物业费时作抵扣。");
            billDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
            billDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));

            JSONArray jsonArray = new JSONArray();
            jsonArray.add(billDetailedBackDO);
//                billDO.setDetailed(jsonArray.toJSONString());
            billDO.setAmountTotalReceivable(amountReceivable);
            billDO.setActualTotalMoneyCollection(amountReceivable);
//                billDO.setPreferentialTotalAmount(0d);

            List<OriginalBillDO> originalBillDOS = new ArrayList<OriginalBillDO>();

//            String startTime = billDetailedDO.getStartTime();
//            for (int i = 0; i < datedif; i++) {
//                OriginalBillDO originalBillDO = new OriginalBillDO();
//                originalBillDO.setOrderId(orderId);
//                originalBillDO.setCustomerUserId(billDO.getCustomerUserId());
//                originalBillDO.setOrganizationId(organizationId);
//                originalBillDO.setOrganizationName(organizationDO.getOrganizationName());
//                originalBillDO.setVillageCode(roomAndCouponDO.getVillageCode());
//                originalBillDO.setVillageName(roomAndCouponDO.getVillageName());
//                originalBillDO.setRegionCode(roomAndCouponDO.getRegionCode());
//                originalBillDO.setRegionName(roomAndCouponDO.getRegionName());
//                originalBillDO.setBuildingCode(roomAndCouponDO.getBuildingCode());
//                originalBillDO.setBuildingName(roomAndCouponDO.getBuildingName());
//                originalBillDO.setUnitCode(roomAndCouponDO.getUnitCode());
//                originalBillDO.setUnitName(roomAndCouponDO.getUnitName());
//                originalBillDO.setRoomCode(roomAndCouponDO.getRoomCode());
//                originalBillDO.setRoomSize(roomDO.getRoomSize());
//                originalBillDO.setSurname(roomAndCouponDO.getSurname());
//                originalBillDO.setMobilePhone(roomAndCouponDO.getMobilePhone());
//                originalBillDO.setPaymentMethod("6");
//                originalBillDO.setTollCollectorId(userId);
//                originalBillDO.setStateOfArrears(stateOfArrears);
//                originalBillDO.setRefundStatus(refundStatus);
//                originalBillDO.setInvalidState(invalidState);
//                originalBillDO.setRemark(roomAndCouponDO.getCouponName()+"抵扣,抵扣金额为："+amountReceivable+"元,剩余金额为："+surplusDeductibleMoney+"元。");
//                originalBillDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//                originalBillDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
//
//                originalBillDO.setChargeCode(billDetailedDO.getChargeCode());
//                originalBillDO.setChargeName(billDetailedDO.getChargeName());
//                originalBillDO.setChargeUnit(billDetailedDO.getChargeUnit());
//                originalBillDO.setChargeStandard(billDetailedDO.getChargeStandard());
//                originalBillDO.setCurrentReadings(billDetailedDO.getCurrentReadings());
//                originalBillDO.setLastReading(billDetailedDO.getLastReading());
//                originalBillDO.setUsageAmount(billDetailedDO.getUsageAmount());
//                originalBillDO.setDiscount(billDetailedDO.getDiscount());
//                originalBillDO.setDatedif(billDetailedDO.getDatedif());
//                originalBillDO.setChargeType(billDetailedDO.getChargeType());
//
//                originalBillDO.setAmountReceivable(billDetailedBackDO.getAmountReceivable()/datedif);
//                originalBillDO.setActualMoneyCollection(billDetailedBackDO.getAmountReceivable()/datedif);
//                originalBillDO.setPreferentialAmount(0d);
//                originalBillDO.setStartTime(startTime);
//
//                originalBillDO.setCouponCode(roomAndCouponDO.getCouponCode());
//                originalBillDO.setCouponName(roomAndCouponDO.getCouponName());
//                if (null != startTime) {
//                    startTime = DateUtil.getAfterMonth(startTime, 1);
//                }
//                originalBillDO.setDueTime(startTime);
//                originalBillDOS.add(originalBillDO);
//            }
            roomAndCouponDO.setPropertyFee(amountReceivable);
            roomAndCouponDO.setSurplusDeductibleMoney(surplusDeductibleMoney);
            if (0d!=roomAndCouponDO.getEffectiveTime()){
                roomAndCouponDO.setEndTime(roomAndCouponDO.getEndTime());
            }
            if (surplusDeductibleMoney>0){
                roomAndCouponDO.setUsageState(1);
            }else {
                roomAndCouponDO.setUsageState(2);
            }
            redisUtil.set(Constants.REDISKEY_PROJECT+Constants.PROPERTY_DUE_TIME+roomAndCouponDO.getOrganizationId()+"-"+roomAndCouponDO.getRoomCode(),"NAN");
            billDetailedDAO.save(billDetailedBackDO);
//            roomAndCouponDAO.saveAndFlush(roomAndCouponDO);
            billDAO.save(billDO);
            originalBillDAO.saveAll(originalBillDOS);

            billsBackDTO.setOrderId(billDO.getOrderId());
            billsBackDTO.setOrganizationId(billDO.getOrganizationId());
        }else {
            double surplusDeductibleMoney = money;
            roomAndCouponDO.setDeductionRecord("0");
            roomAndCouponDO.setAmountDeductedThisTime(0d);
            roomAndCouponDO.setDeductibledMoney(0d);
            roomAndCouponDO.setSurplusDeductibleMoney(surplusDeductibleMoney);
        }

        roomAndCouponDO.setAuditStatus(3);
        roomAndCouponDAO.saveAndFlush(roomAndCouponDO);
        return billsBackDTO;
    }
}
