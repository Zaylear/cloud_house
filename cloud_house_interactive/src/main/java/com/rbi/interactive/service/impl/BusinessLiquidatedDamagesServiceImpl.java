package com.rbi.interactive.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.rbi.interactive.dao.*;
import com.rbi.interactive.entity.*;
import com.rbi.interactive.entity.dto.RoomCodeAndMobilePhoneDTO;
import com.rbi.interactive.service.BusinessLiquidatedDamagesService;
import com.rbi.interactive.service.LiquidatedDamagesService;
import com.rbi.interactive.utils.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.*;

@Service
public class BusinessLiquidatedDamagesServiceImpl implements BusinessLiquidatedDamagesService {

    private final static Logger logger = LoggerFactory.getLogger(BusinessLiquidatedDamagesServiceImpl.class);

    @Autowired
    LiquidatedDamagesService liquidatedDamagesService;

    @Autowired
    LiquidatedDamagesDAO liquidatedDamagesDAO;

    @Autowired(required = false)
    ILiquidatedDamagesDAO iLiquidatedDamagesDAO;

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    VillageDAO villageDAO;

    @Autowired
    LiquidatedDamageLogDAO liquidatedDamageLogDAO;

    @Autowired
    OrganizationDAO organizationDAO;

    @Autowired(required = false)
    IOriginalBillDAO iOriginalBillDAO;


    @Override
    public Map<String,Object> batchProcessingLiquidatedDamages(MultipartFile multipartFile,String userId) throws ParseException {
        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String,String>> list = null;
        String cellData = null;
//        String filePath = "D:\\newExcel.xlsx";
        String columns[] = {"roomCode","mobilePhone","actualMoneyCollection","liquidatedDamageDueTime"};
        wb = ExcelPOI.readExcel(multipartFile);
        if(wb != null){
            //用来存放表中数据
            list = new ArrayList<Map<String,String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int colnum = row.getPhysicalNumberOfCells();
            for (int i = 1; i<rownum; i++) {
                Map<String,String> map = new LinkedHashMap<String,String>();
                row = sheet.getRow(i);
                if(row !=null){
                    for (int j=0;j<colnum;j++){
                        cellData = (String) ExcelPOI.getCellFormatValue(row.getCell(j));
                        map.put(columns[j], cellData);
                    }
                }else{
                    break;
                }
                list.add(map);
            }
        }
        logger.info("读取违约金数据成功！数据量：{}",list.size());
        //遍历解析出来的list
//        for (Map<String,String> map : list) {
//            for (Entry<String,String> entry : map.entrySet()) {
//                System.out.print(entry.getKey()+":"+entry.getValue()+",");
//            }
//        }
//        List<LiquidatedDamagesDO> liquidatedDamagesDOS = new ArrayList<>();

//        for (int i = 0; i < list.size(); i++) {
//            LiquidatedDamagesDO liquidatedDamagesDO = new LiquidatedDamagesDO();
//            System.out.println(list.get(i).get("liquidatedDamageDueTime"));
//            liquidatedDamagesDO = liquidatedDamagesService.liquidatedDamagesCalculation(list.get(i).get("roomCode"),
//                    Double.valueOf(list.get(i).get("actualMoneyCollection")),userId,list.get(i).get("mobilePhone"),
//                    list.get(i).get("liquidatedDamageDueTime"));
//            liquidatedDamagesDOS.add(liquidatedDamagesDO);
//        }

        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();

        String orderId = "";
        String time = DateUtil.date(DateUtil.DATE_FORMAT_PATTERN);
        Integer orderIdsCount = iLiquidatedDamagesDAO.orderIdCount(organizationId,time);
        if (orderIdsCount<9){
            orderIdsCount = orderIdsCount+1;
            orderId = time+"000"+orderIdsCount;
        }else if (orderIdsCount>=9&&orderIdsCount<99){
            orderIdsCount = orderIdsCount+1;
            orderId = time+"00"+orderIdsCount;
        }else if (orderIdsCount>=99&&orderIdsCount<999){
            orderIdsCount = orderIdsCount+1;
            orderId = time+"0"+orderIdsCount;
        }else {
            orderIdsCount = orderIdsCount+1;
            orderId = time+orderIdsCount;
        }

        orderId = "wyj-"+orderId;
        String finalOrderId = orderId;
        List<LiquidatedDamageLogDO> liquidatedDamageLogDOS = new ArrayList<>();

        int count = 0;

        for (int i=0;i<list.size();i++) {
            Map<String,String> map = new LinkedHashMap<String,String>();
            map = list.get(i);
            LiquidatedDamagesDO liquidatedDamagesDO = new LiquidatedDamagesDO();

            LiquidatedDamageLogDO liquidatedDamageLogDO = new LiquidatedDamageLogDO();

            try {
                liquidatedDamagesDO = liquidatedDamagesService.liquidatedDamagesCalculation(finalOrderId, map.get("roomCode"),
                        Double.parseDouble(map.get("actualMoneyCollection")), userId, map.get("mobilePhone"),
                        map.get("liquidatedDamageDueTime"));
                liquidatedDamagesDAO.save(liquidatedDamagesDO);

                /**
                 * 处理日志
                 */
                liquidatedDamageLogDO.setCode(liquidatedDamagesDO.getId());
                liquidatedDamageLogDO.setOrderId(finalOrderId);
                liquidatedDamageLogDO.setRoomCode(map.get("roomCode"));
                liquidatedDamageLogDO.setResult("导入成功");
                liquidatedDamageLogDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                liquidatedDamageLogDO.setOrganizationId(organizationId);
                liquidatedDamageLogDAO.save(liquidatedDamageLogDO);
                liquidatedDamageLogDOS.add(liquidatedDamageLogDO);
                count++;
            }catch (NullPointerException e){
                logger.error("【违约金计算服务类】查询房间号数据为空！,ERROR:{}",e);
                liquidatedDamageLogDO.setCode(liquidatedDamagesDO.getId());
                liquidatedDamageLogDO.setOrderId(finalOrderId);
                liquidatedDamageLogDO.setRoomCode(map.get("roomCode"));
                liquidatedDamageLogDO.setResult("导入失败");
                liquidatedDamageLogDO.setRemarks("第" + (i+2) + "行，房间号为：" + map.get("roomCode") + "的数据导入失败，房间号未找到！");
                liquidatedDamageLogDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                liquidatedDamageLogDO.setOrganizationId(organizationId);
                liquidatedDamageLogDAO.save(liquidatedDamageLogDO);
                liquidatedDamageLogDOS.add(liquidatedDamageLogDO);
            }catch (ParseException e) {
                logger.error("【违约金计算服务类】时间计算异常：ERROR：{},处理数据：{}", e, map.get("roomCode"));
                liquidatedDamageLogDO.setCode(liquidatedDamagesDO.getId());
                liquidatedDamageLogDO.setOrderId(finalOrderId);
                liquidatedDamageLogDO.setRoomCode(map.get("roomCode"));
                liquidatedDamageLogDO.setResult("导入失败");
                liquidatedDamageLogDO.setRemarks("第" + (i+2) + "行，房间号为：" + map.get("roomCode") + "的数据导入失败，该房间号请重新导入！");
                liquidatedDamageLogDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                liquidatedDamageLogDO.setOrganizationId(organizationId);
                liquidatedDamageLogDAO.save(liquidatedDamageLogDO);
                liquidatedDamageLogDOS.add(liquidatedDamageLogDO);
            } catch (ConnectException e) {
                logger.error("【违约金计算服务类】违约金计算异常：ERROR：{},处理数据：{}", e, map.get("roomCode"));
                liquidatedDamageLogDO.setCode(liquidatedDamagesDO.getId());
                liquidatedDamageLogDO.setOrderId(finalOrderId);
                liquidatedDamageLogDO.setRoomCode(map.get("roomCode"));
                liquidatedDamageLogDO.setResult("导入失败");
                liquidatedDamageLogDO.setRemarks("第" + (i+2) + "行，房间号为：" + map.get("roomCode") + "的数据导入失败，该房间号请重新导入！");
                liquidatedDamageLogDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                liquidatedDamageLogDO.setOrganizationId(organizationId);
                liquidatedDamageLogDAO.save(liquidatedDamageLogDO);
                liquidatedDamageLogDOS.add(liquidatedDamageLogDO);
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",list.size()); //导入数据条数
        map.put("realNumber",count); //导入成功条数
        map.put("liquidatedDamageLogDOS",liquidatedDamageLogDOS);
        logger.info("导入违约金完成！");
        return map;
    }

    @Override
    public PageData findByPage(int pageNum, int pageSize,String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode,String mobilePhone,String idNumber,String surname, String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();

        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnable(organizationId, Constants.VILLAGE_ENABLE);

        if (villageDOS.size()==0){
            VillageDO villageDO = new VillageDO();
            villageDO.setVillageCode(Constants.VILLAGE_CODE_SET);
            villageDOS.add(villageDO);
        }

        Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<LiquidatedDamagesDO> page = liquidatedDamagesDAO.findAll(new Specification<LiquidatedDamagesDO>() {
            @Override
            public Predicate toPredicate(Root<LiquidatedDamagesDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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


    /**
     * 修改两种方式
     * 一、前端请求全部参数
     * 二、前端给id和需要参数，+备注 remarks
     * @param jsonObject
     * @param userId
     * @throws ParseException
     */
    @Override
    public void update(JSONObject jsonObject,String userId) throws ParseException {
        int id = jsonObject.getInteger("id");
        LiquidatedDamagesDO liquidatedDamagesDO = liquidatedDamagesDAO.findById(id);
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);

        JSONArray jsonArray = liquidatedDamagesService.liquidatedDamagesImpl(liquidatedDamagesDO.getRoomCode(),
                userInfoDO.getOrganizationId(),liquidatedDamagesDO.getDueTime(),
                liquidatedDamagesDO.getOneMonthPropertyFeeAmount(),
                jsonObject.getString("liquidatedDamageDueTime"),
                liquidatedDamagesDO.getSuperfluousAmount(),
                liquidatedDamagesDO.getQuarterlyCycleTime());

        liquidatedDamagesDO.setLiquidatedDamages(jsonArray.toJSONString());
        Double amountTotalReceivable = 0d;
        for (int i = 0;i < jsonArray.size();i++){
            amountTotalReceivable+=JSONObject.parseObject(jsonArray.get(i).toString()).getDouble("amountMoney");
        }
        liquidatedDamagesDO.setAmountTotalReceivable(Tools.moneyHalfAdjust(amountTotalReceivable));
        liquidatedDamagesDO.setActualTotalMoneyCollection(Tools.moneyHalfAdjust(amountTotalReceivable));
        liquidatedDamagesDO.setReviserId(userId);
        liquidatedDamagesDO.setReviserName(userInfoDO.getRealName());
        liquidatedDamagesDO.setAuditStatus(1);
        if (StringUtils.isNotBlank(jsonObject.getString("remarks"))){
            liquidatedDamagesDO.setRemarks(liquidatedDamagesDO.getRemarks()+"\n"+DateUtil.date(DateUtil.FORMAT_PATTERN)+"\t"+jsonObject.getString("remarks"));
        }
        liquidatedDamagesDO.setLiquidatedDamageDueTime(jsonObject.getString("liquidatedDamageDueTime"));
        liquidatedDamagesDAO.saveAndFlush(liquidatedDamagesDO);
    }

    @Override
    public PageData findByNotByPage(int pageNum, int pageSize, String userId,String villageCode,String regionCode,String buildingCode,String unitCode, String roomCode, String mobilePhone,String idNumber,String surname) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();

        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnable(organizationId, Constants.VILLAGE_ENABLE);

        if (villageDOS.size()==0){
            VillageDO villageDO = new VillageDO();
            villageDO.setVillageCode(Constants.VILLAGE_CODE_SET);
            villageDOS.add(villageDO);
        }

        Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<LiquidatedDamagesDO> page = liquidatedDamagesDAO.findAll(new Specification<LiquidatedDamagesDO>() {
            @Override
            public Predicate toPredicate(Root<LiquidatedDamagesDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"), userInfoDO.getOrganizationId())));
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("auditStatus"), 0)));

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
    public PageData findByWaitAuditPage(int pageNum, int pageSize, String userId, String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode, String mobilePhone,String idNumber,String surname) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();

        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnable(organizationId, Constants.VILLAGE_ENABLE);

        if (villageDOS.size()==0){
            VillageDO villageDO = new VillageDO();
            villageDO.setVillageCode(Constants.VILLAGE_CODE_SET);
            villageDOS.add(villageDO);
        }

        Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<LiquidatedDamagesDO> page = liquidatedDamagesDAO.findAll(new Specification<LiquidatedDamagesDO>() {
            @Override
            public Predicate toPredicate(Root<LiquidatedDamagesDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"), userInfoDO.getOrganizationId())));
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("auditStatus"), 1)));

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
    public PageData findByWaitReviewPage(int pageNum, int pageSize, String userId, String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode, String mobilePhone,String idNumber,String surname) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();

        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnable(organizationId, Constants.VILLAGE_ENABLE);

        if (villageDOS.size()==0){
            VillageDO villageDO = new VillageDO();
            villageDO.setVillageCode(Constants.VILLAGE_CODE_SET);
            villageDOS.add(villageDO);
        }

        Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<LiquidatedDamagesDO> page = liquidatedDamagesDAO.findAll(new Specification<LiquidatedDamagesDO>() {
            @Override
            public Predicate toPredicate(Root<LiquidatedDamagesDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"), userInfoDO.getOrganizationId())));
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("auditStatus"), 2)));

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
    public PageData findByAlreadyAuditPage(int pageNum, int pageSize, String userId, String villageCode,String regionCode,String buildingCode,String unitCode,String roomCode, String mobilePhone,String idNumber,String surname) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();

        List<VillageDO> villageDOS = villageDAO.findAllByOrganizationIdAndEnable(organizationId, Constants.VILLAGE_ENABLE);

        if (villageDOS.size()==0){
            VillageDO villageDO = new VillageDO();
            villageDO.setVillageCode(Constants.VILLAGE_CODE_SET);
            villageDOS.add(villageDO);
        }

        Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<LiquidatedDamagesDO> page = liquidatedDamagesDAO.findAll(new Specification<LiquidatedDamagesDO>() {
            @Override
            public Predicate toPredicate(Root<LiquidatedDamagesDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"), userInfoDO.getOrganizationId())));
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("auditStatus"), 3)));

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
    public void auditPass(int id,String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        LiquidatedDamagesDO liquidatedDamagesDO = liquidatedDamagesDAO.findById(id);
        liquidatedDamagesDO.setAuditStatus(2);
        liquidatedDamagesDO.setAuditId(userId);
        liquidatedDamagesDO.setAuditName(userInfoDO.getRealName());
        liquidatedDamagesDAO.saveAndFlush(liquidatedDamagesDO);
    }

    @Override
    public void reviewPass(int id,String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        LiquidatedDamagesDO liquidatedDamagesDO = liquidatedDamagesDAO.findById(id);
        liquidatedDamagesDO.setAuditStatus(3);
        liquidatedDamagesDO.setAuditId(userId);
        liquidatedDamagesDO.setAuditName(userInfoDO.getRealName());
        liquidatedDamagesDAO.saveAndFlush(liquidatedDamagesDO);
    }

    @Override
    public void noPass(int id,String remarks,String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        LiquidatedDamagesDO liquidatedDamagesDO = liquidatedDamagesDAO.findById(id);
        liquidatedDamagesDO.setAuditStatus(0);
        liquidatedDamagesDO.setAuditId(userId);
        liquidatedDamagesDO.setAuditName(userInfoDO.getRealName());
        if (StringUtils.isNotBlank(remarks)){
            liquidatedDamagesDO.setRemarks(liquidatedDamagesDO.getRemarks()+"\n"+DateUtil.date(DateUtil.FORMAT_PATTERN)+"\t"+remarks);
        }
        liquidatedDamagesDAO.saveAndFlush(liquidatedDamagesDO);
    }

    @Override
    public void delete(List<Integer> idList) {
        liquidatedDamagesDAO.deleteByIdIn(idList);
    }

    @Override
    @Transactional
    public Map<String,Object> onekeyCalculationLiquidatedDamages(List<RoomCodeAndMobilePhoneDTO> roomCodeAndMobilePhoneDTOS, String startTime, String endTime, String liquidatedDamageDueTime, String userId) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        String organizationId = userInfoDO.getOrganizationId();
//        OrganizationDO organizationDO = organizationDAO.findByOrganizationId(organizationId);

        String orderId = "";
        String time = DateUtil.date(DateUtil.DATE_FORMAT_PATTERN);
        Integer orderIdsCount = iLiquidatedDamagesDAO.orderIdCount(organizationId,time);
        if (orderIdsCount<9){
            orderIdsCount = orderIdsCount+1;
            orderId = time+"000"+orderIdsCount;
        }else if (orderIdsCount>=9&&orderIdsCount<99){
            orderIdsCount = orderIdsCount+1;
            orderId = time+"00"+orderIdsCount;
        }else if (orderIdsCount>=99&&orderIdsCount<999){
            orderIdsCount = orderIdsCount+1;
            orderId = time+"0"+orderIdsCount;
        }else {
            orderIdsCount = orderIdsCount+1;
            orderId = time+orderIdsCount;
        }

        int count = 0;

        orderId = "wyj-"+orderId;
        String finalOrderId = orderId;
        List<LiquidatedDamageLogDO> liquidatedDamageLogDOS = new ArrayList<>();
        int i = -1;
        for (RoomCodeAndMobilePhoneDTO roomCodeAndMobilePhoneDTO: roomCodeAndMobilePhoneDTOS) {
            i++;
            Double actualMoneyCollection = iOriginalBillDAO.findAllByRoomCodeAndChargeStatusAndOrganizationIdAnd(roomCodeAndMobilePhoneDTO.getRoomCode(),organizationId,startTime,endTime);
            if (null == actualMoneyCollection){
                actualMoneyCollection = 0d;
            }
            LiquidatedDamagesDO liquidatedDamagesDO = new LiquidatedDamagesDO();

            LiquidatedDamageLogDO liquidatedDamageLogDO = new LiquidatedDamageLogDO();

            try {
                liquidatedDamagesDO = liquidatedDamagesService.liquidatedDamagesCalculation(finalOrderId, roomCodeAndMobilePhoneDTO.getRoomCode(),
                        Tools.moneyHalfAdjust(actualMoneyCollection), userId, roomCodeAndMobilePhoneDTO.getMobilePhone(),
                        liquidatedDamageDueTime);
                liquidatedDamagesDAO.save(liquidatedDamagesDO);

                /**
                 * 处理日志
                 */
                liquidatedDamageLogDO.setCode(liquidatedDamagesDO.getId());
                liquidatedDamageLogDO.setOrderId(finalOrderId);
                liquidatedDamageLogDO.setRoomCode(roomCodeAndMobilePhoneDTO.getRoomCode());
                liquidatedDamageLogDO.setResult("导入成功");
                liquidatedDamageLogDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                liquidatedDamageLogDAO.save(liquidatedDamageLogDO);
                liquidatedDamageLogDOS.add(liquidatedDamageLogDO);
                count++;
            }catch (NullPointerException e){
                logger.error("【违约金计算服务类】查询房间号数据为空！,ERROR:{}",e);
                liquidatedDamageLogDO.setCode(liquidatedDamagesDO.getId());
                liquidatedDamageLogDO.setOrderId(finalOrderId);
                liquidatedDamageLogDO.setRoomCode(roomCodeAndMobilePhoneDTO.getRoomCode());
                liquidatedDamageLogDO.setResult("导入失败");
                liquidatedDamageLogDO.setRemarks("第" + (i+2) + "行，房间号为：" + roomCodeAndMobilePhoneDTO.getRoomCode() + "的数据导入失败，房间号未找到！");
                liquidatedDamageLogDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                liquidatedDamageLogDAO.save(liquidatedDamageLogDO);
                liquidatedDamageLogDOS.add(liquidatedDamageLogDO);
            }catch (ParseException e) {
                logger.error("【违约金计算服务类】时间计算异常：ERROR：{},处理数据：{}", e, roomCodeAndMobilePhoneDTO.getRoomCode());
                liquidatedDamageLogDO.setCode(liquidatedDamagesDO.getId());
                liquidatedDamageLogDO.setOrderId(finalOrderId);
                liquidatedDamageLogDO.setRoomCode(roomCodeAndMobilePhoneDTO.getRoomCode());
                liquidatedDamageLogDO.setResult("导入失败");
                liquidatedDamageLogDO.setRemarks("第" + (i+2) + "行，房间号为：" + roomCodeAndMobilePhoneDTO.getRoomCode() + "的数据导入失败，该房间号请重新导入！");
                liquidatedDamageLogDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                liquidatedDamageLogDAO.save(liquidatedDamageLogDO);
                liquidatedDamageLogDOS.add(liquidatedDamageLogDO);
            } catch (ConnectException e) {
                logger.error("【违约金计算服务类】违约金计算异常：ERROR：{},处理数据：{}", e, roomCodeAndMobilePhoneDTO.getRoomCode());
                liquidatedDamageLogDO.setCode(liquidatedDamagesDO.getId());
                liquidatedDamageLogDO.setOrderId(finalOrderId);
                liquidatedDamageLogDO.setRoomCode(roomCodeAndMobilePhoneDTO.getRoomCode());
                liquidatedDamageLogDO.setResult("导入失败");
                liquidatedDamageLogDO.setRemarks("第" + (i+2) + "行，房间号为：" + roomCodeAndMobilePhoneDTO.getRoomCode() + "的数据导入失败，该房间号请重新导入！");
                liquidatedDamageLogDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
                liquidatedDamageLogDAO.save(liquidatedDamageLogDO);
                liquidatedDamageLogDOS.add(liquidatedDamageLogDO);
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",roomCodeAndMobilePhoneDTOS.size()); //导入数据条数
        map.put("realNumber",count); //导入成功条数
        map.put("liquidatedDamageLogDOS",liquidatedDamageLogDOS);
        return map;
    }
}
