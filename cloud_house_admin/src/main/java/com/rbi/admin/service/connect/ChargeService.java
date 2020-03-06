package com.rbi.admin.service.connect;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.dao.ChargeItemDAO;
import com.rbi.admin.dao.IdDeleteDAO;
import com.rbi.admin.dao.IdsDeleteDAO;
import com.rbi.admin.dao.RoomAndChargeItemsDAO;
import com.rbi.admin.dao.connect.ChargeDAO;
import com.rbi.admin.entity.edo.ChargeDetailDO;
import com.rbi.admin.entity.edo.ChargeItemDO;
import com.rbi.admin.entity.edo.RoomAndChargeItemsDO;
import com.rbi.admin.entity.dto.*;
import com.rbi.admin.util.DateUtil;
import com.rbi.admin.util.PageData;
import com.rbi.admin.util.PinYin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ChargeService {
    @Autowired(required = false)
    ChargeDAO chargeDAO;
    @Autowired(required = false)
    ChargeItemDAO chargeItemDAO;
    @Autowired(required = false)
    IdsDeleteDAO idsDeleteDAO;
    @Autowired(required = false)
    IdDeleteDAO idDeleteDAO;
    @Autowired(required = false)
    RoomAndChargeItemsDAO roomAndChargeItemsDAO;


    public PropertyChargeDTO findPropertyCharge(String chargeCode, int datedif) {
        PropertyChargeDTO propertyChargeDTO = chargeDAO.findPropertyCharge(chargeCode,datedif);
        return propertyChargeDTO;
    }

    public List<ChargeItemDO> findByChargeCodes(String chargeCodes) {
        String chargeC[] = chargeCodes.split(",");
        List<String> list = new ArrayList<>();
        String temp;
        for (int i = 0; i < chargeC.length; i++) {
            temp = "'" + chargeC[i] + "'";
            list.add(temp);
        }
        String str = String.join(",",list);
        List<ChargeItemDO> chargeDTOS = chargeDAO.findByChargeCodes(str);
        return chargeDTOS;
    }

    public List<ChargeCodesDTO> chooseChargeCode(String organizationId){
        List<ChargeCodesDTO> chargeCodesDTOS = chargeDAO.chooseChargeCode(organizationId);
        return chargeCodesDTOS;
    }

    public Map<String,Object> findChargeDetail(String chargeCode){
        Map<String,Object> map = new HashMap<>();
        List<ChargeDetailDO> chargeDetailDOS  = chargeDAO.findChargeDetail(chargeCode);
        ChargeItemDO chargeItemDO = chargeDAO.findChargeItem(chargeCode);
        map.put("chargeItem",chargeItemDO);
        map.put("chargeDetail",chargeDetailDOS);
        return map;
    }

    public String
    add(JSONObject result,String organizationId) {
        JSONObject json = result.getJSONObject("chargeItem");
        String chargeName=json.getString("chargeName");
        Integer chargeType=json.getInteger("chargeType");
        String chargeUnit=json.getString("chargeUnit");
        Double chargeStandard=json.getDouble("chargeStandard");
        Integer refund=json.getInteger("refund");
        Integer mustPay=json.getInteger("mustPay");
        if (null == chargeName || null == chargeType || null == chargeUnit || null == refund || null == mustPay){
            return "收费项目名称、类型、单位、是否可退款、是否必缴不能为空！";
        }
        if ("".equals(chargeName) || "".equals(chargeType) || "".equals(chargeUnit) || "".equals(refund) || "".equals(mustPay)){
            return "收费项目名称、类型、单位、是否可退款、是否必缴不能为空！";
        }
        String chargeCode =organizationId+"-"+PinYin.getPinYinHeadChar(chargeName).toUpperCase();
        int num0 = chargeDAO.findChargeCodeNum(chargeCode,organizationId);
        if (num0 !=0){
            return "收费项目编号生成重复，请修改收费项目名称";
        }
        int num = chargeDAO.findChargeItemNum(chargeType,organizationId);
        if (num !=0){
            return "不能添加重复的收费项目类型";
        }

        int chargeWay = 0;
        if (chargeType ==1 ||chargeType ==2||chargeType ==3||chargeType ==6||chargeType ==11){
            chargeWay = 1;
        }
        if(chargeType ==8 ||chargeType ==9){
            chargeWay = 2;
        }
        if (chargeType ==14 ||chargeType ==15||chargeType ==16){
            chargeWay = 3;
        }
        if (chargeType ==4){
            chargeWay = 5;
        }
        if (chargeType ==7 || chargeType ==17 || chargeType ==5 || chargeType ==10 || chargeType ==12 || chargeType ==13 || chargeType == 4){
            chargeWay = 0;
        }

        int sortState = 0;
        if (chargeType ==1 ||chargeType ==2||chargeType ==3){
            sortState = 1;
        }
        if (chargeType ==4){
            sortState = 2;
        }
        if (chargeType ==12){
            sortState = 3;
        }
        if (chargeType ==13){
            sortState = 4;
        }
        if (chargeType ==8 || chargeType ==9){
            sortState = 6;
        }
        if (chargeType ==14 ||chargeType ==15||chargeType ==16){
            sortState = 8;
        }
        if (chargeType ==18){
            sortState = 11;
        }
        if (chargeType ==7){
            sortState = 12;
        }
        if (chargeType ==5){
            sortState = 20;
        }
        if (chargeType ==6){
            sortState = 21;
        }

        String idt = DateUtil.date(DateUtil.FORMAT_PATTERN);
        chargeDAO.addItem(organizationId,chargeCode,chargeName,chargeType,chargeUnit,
                chargeStandard,chargeWay,refund,mustPay,1,idt,sortState);
        JSONArray jsonArray = result.getJSONArray("chargeDetail");
        if (jsonArray.size()!=0){
            for (int i = 0; i < jsonArray.size(); i++) {

                JSONObject obj = (JSONObject) jsonArray.get(i);
                Integer areaMin =obj.getInteger("areaMin");
                Integer areaMax=obj.getInteger("areaMax");
                Double money=obj.getDouble("money");
                Integer datedif=obj.getInteger("datedif");
                Double discount=obj.getDouble("discount");
                String parkingSpacePlace = obj.getString("parkingSpacePlace");
                String parkingSpaceType = obj.getString("parkingSpaceType");
                chargeDAO.addDetail(chargeCode,areaMax,areaMin,money,datedif,discount,parkingSpacePlace,parkingSpaceType);
            }
        }
        if (mustPay ==1){
            if (chargeType == 1){
                List<RoomAndChargeItemsDO> roomAndChargeItemsDOS = chargeDAO.findHouseRoomByOrganizationId(organizationId);
                for (int i =0; i< roomAndChargeItemsDOS.size();i++) {
                    roomAndChargeItemsDOS.get(i).setIdt(idt);
                    roomAndChargeItemsDOS.get(i).setChargeCode(chargeCode);
                    roomAndChargeItemsDOS.get(i).setSurplus(0d);
                    roomAndChargeItemsDAO.save(roomAndChargeItemsDOS.get(i));
                }
            }else if (chargeType == 2){
                List<RoomAndChargeItemsDO> roomAndChargeItemsDOS = chargeDAO.findBusinessHouseRoomByOrganizationId(organizationId);
                for (int i =0; i< roomAndChargeItemsDOS.size();i++) {
                    roomAndChargeItemsDOS.get(i).setIdt(idt);
                    roomAndChargeItemsDOS.get(i).setChargeCode(chargeCode);
                    roomAndChargeItemsDOS.get(i).setSurplus(0d);
                    roomAndChargeItemsDAO.save(roomAndChargeItemsDOS.get(i));
                }
            }else if (chargeType == 3){
                List<RoomAndChargeItemsDO> roomAndChargeItemsDOS = chargeDAO.findWorkRoomByOrganizationId(organizationId);
                for (int i =0; i< roomAndChargeItemsDOS.size();i++) {
                    roomAndChargeItemsDOS.get(i).setIdt(idt);
                    roomAndChargeItemsDOS.get(i).setChargeCode(chargeCode);
                    roomAndChargeItemsDOS.get(i).setSurplus(0d);
                    roomAndChargeItemsDAO.save(roomAndChargeItemsDOS.get(i));
                }
            }else {
                List<RoomAndChargeItemsDO> roomAndChargeItemsDOS = chargeDAO.findRoomByOrganizationId(organizationId);
                for (int i =0; i< roomAndChargeItemsDOS.size();i++) {
                    roomAndChargeItemsDOS.get(i).setIdt(idt);
                    roomAndChargeItemsDOS.get(i).setChargeCode(chargeCode);
                    roomAndChargeItemsDOS.get(i).setSurplus(0d);
                    roomAndChargeItemsDAO.save(roomAndChargeItemsDOS.get(i));
                }
            }
        }
        return "10000";
    }


    public String update(JSONObject result,String organizationId) {
        JSONObject json = result.getJSONObject("chargeItem");
        String chargeName=json.getString("chargeName");
        String chargeCode =json.getString("chargeCode");
        Integer chargeType=json.getInteger("chargeType");
        String chargeUnit=json.getString("chargeUnit");
        Double chargeStandard=json.getDouble("chargeStandard");
        Integer refund=json.getInteger("refund");
        Integer enable=json.getInteger("enable");
        Integer mustPay=json.getInteger("mustPay");
        Integer chargeWay = json.getInteger("chargeWay");
        if (null == chargeName || null == chargeType || null == chargeUnit || null == refund || null == mustPay){
            return "收费项目名称、类型、单位、是否可退款、是否必缴不能为空！";
        }
        if ("".equals(chargeName) || "".equals(chargeType) || "".equals(chargeUnit) || "".equals(refund) || "".equals(mustPay)){
            return "收费项目名称、类型、单位、是否可退款、是否必缴不能为空！";
        }
        String udt = DateUtil.date(DateUtil.FORMAT_PATTERN);
        ChargeItemDO chargeItemDO = chargeDAO.findChargeItem(chargeCode);
        chargeDAO.updateItem(chargeName,chargeType,chargeUnit,chargeStandard,chargeWay,
                refund,mustPay,enable,udt,chargeCode);
        JSONArray jsonArray = result.getJSONArray("chargeDetail");
        if (jsonArray.size()!=0){
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                Integer id=obj.getInteger("id");
                Integer areaMax=obj.getInteger("areaMax");
                Integer areaMin =obj.getInteger("areaMin");
                Double money=obj.getDouble("money");
                Integer datedif=obj.getInteger("datedif");
                Double discount=obj.getDouble("discount");
                String parkingSpacePlace = obj.getString("parkingSpacePlace");
                String parkingSpaceType=obj.getString("parkingSpaceType");
                if (id==null){
                    chargeDAO.addDetail(chargeCode,areaMax,areaMin,money,datedif,discount,parkingSpacePlace,parkingSpaceType);
                }
                chargeDAO.updateDetail(areaMax,areaMin,money,datedif,discount,parkingSpacePlace,parkingSpaceType,id);
            }
        }
        if (mustPay ==1 && chargeItemDO.getMustPay() == 0){
            String idt = DateUtil.date(DateUtil.FORMAT_PATTERN);
            if (chargeType == 1){
                List<RoomAndChargeItemsDO> roomAndChargeItemsDOS = chargeDAO.findHouseRoomByOrganizationId(organizationId);
                for (int i =0; i< roomAndChargeItemsDOS.size();i++) {
                    roomAndChargeItemsDOS.get(i).setIdt(idt);
                    roomAndChargeItemsDOS.get(i).setChargeCode(chargeCode);
                    roomAndChargeItemsDOS.get(i).setSurplus(0d);
                    roomAndChargeItemsDAO.save(roomAndChargeItemsDOS.get(i));
                }
            }else if (chargeType == 2){
                List<RoomAndChargeItemsDO> roomAndChargeItemsDOS = chargeDAO.findBusinessHouseRoomByOrganizationId(organizationId);
                for (int i =0; i< roomAndChargeItemsDOS.size();i++) {
                    roomAndChargeItemsDOS.get(i).setIdt(idt);
                    roomAndChargeItemsDOS.get(i).setChargeCode(chargeCode);
                    roomAndChargeItemsDOS.get(i).setSurplus(0d);
                    roomAndChargeItemsDAO.save(roomAndChargeItemsDOS.get(i));
                }
            }else if (chargeType == 3){
                List<RoomAndChargeItemsDO> roomAndChargeItemsDOS = chargeDAO.findWorkRoomByOrganizationId(organizationId);
                for (int i =0; i< roomAndChargeItemsDOS.size();i++) {
                    roomAndChargeItemsDOS.get(i).setIdt(idt);
                    roomAndChargeItemsDOS.get(i).setChargeCode(chargeCode);
                    roomAndChargeItemsDOS.get(i).setSurplus(0d);
                    roomAndChargeItemsDAO.save(roomAndChargeItemsDOS.get(i));
                }
            }else {
                List<RoomAndChargeItemsDO> roomAndChargeItemsDOS = chargeDAO.findRoomByOrganizationId(organizationId);
                for (int i =0; i< roomAndChargeItemsDOS.size();i++) {
                    roomAndChargeItemsDOS.get(i).setIdt(idt);
                    roomAndChargeItemsDOS.get(i).setChargeCode(chargeCode);
                    roomAndChargeItemsDOS.get(i).setSurplus(0d);
                    roomAndChargeItemsDAO.save(roomAndChargeItemsDOS.get(i));
                }
            }

        }
        if (mustPay == 0 && chargeItemDO.getMustPay() ==1){
           String chargeCode2 = "('"+chargeCode+"')";
            chargeDAO.deleRoomItem(chargeCode2);
        }
        return "10000";
    }


    public void deleteByIds(JSONArray result) {
        String temp = "";
        List<String> list = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            JSONObject obj = (JSONObject) result.get(i);
            String id = obj.getString("chargeCode");
            temp = "'" + id + "'";
            list.add(temp);
        }
        String str = String.join(",", list);
        String ids = "("+str+")";
        idsDeleteDAO.deleteChargeItem(ids);
        idsDeleteDAO.deleteChargeDetail(ids);
        chargeDAO.deleRoomItem(ids);
    }

    public void deleteChargeDetail(int id){
        idDeleteDAO.deleteChargeDetail(id);
    }

    public PageData findByChargeName(String chargeName,String organizationId, int pageNum, int pageSize){
        int pageNo = pageSize * (pageNum - 1);
        String chargeName2 = "'%" + chargeName + "%'";
        List<ChargeItemDO> chargeItemDOS  = chargeDAO.findByChargeName(organizationId,chargeName2,pageNo,pageSize);
        int totalPage = 0;
        int count = chargeDAO.findNum2(organizationId,chargeName2);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum,pageSize,totalPage,count,chargeItemDOS);
    }
}
