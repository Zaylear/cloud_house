package com.rbi.admin.service.connect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.rbi.admin.dao.OrganizationDAO;
import com.rbi.admin.dao.SystemSettingDAO;
import com.rbi.admin.dao.structure.VillageChooseDAO;
import com.rbi.admin.entity.edo.OrganizationDO;
import com.rbi.admin.entity.edo.RoomTreeDTO;
import com.rbi.admin.util.Constants;
import com.rbi.admin.util.EncapsulationTreeUtil;
import com.rbi.admin.util.RedisUtil;
import com.rbi.admin.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VillageChooseService {

    private final static Logger logger = LoggerFactory.getLogger(VillageChooseService.class);

    @Autowired(required = false)
    VillageChooseDAO villageChooseDAO;
    @Autowired(required = false)
    SystemSettingDAO systemSettingDAO;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    OrganizationDAO organizationDAO;



    public JSONArray roomTree(String organizationId) throws Exception {
//        try {
//            String tree = redisUtil.get(Constants.REDISKEY_PROJECT + Constants.VILLAGE_TREE + organizationId).toString();
//            if (StringUtils.isNotBlank(tree) && null != tree && !"null".equals(tree) && !"".equals(tree)) {
//                JSONArray villageTree = JSONArray.parseArray(tree);
//                return villageTree;
//            }
//        } catch (Exception e) {
            List<RoomTreeDTO> roomTreeDTOS = villageChooseDAO.findRoom(organizationId);
            List<Map<String, Object>> data = new ArrayList<>();
            String villageCode = "";
            String regionCode = "";
            String buildingCode = "";
            String unitCode = "";
            String roomCode = "";

            for (RoomTreeDTO roomTreeDTO : roomTreeDTOS) {
                if (!villageCode.equals(roomTreeDTO.getVillageCode())) {
                    villageCode = roomTreeDTO.getVillageCode();
                    Map<String, Object> map = new HashMap<>();
                    map.put("code", roomTreeDTO.getVillageCode());
                    map.put("pid", "");
                    map.put("name", roomTreeDTO.getVillageName());
                    map.put("level", "1");
                    data.add(map);
                }
            }
            for (RoomTreeDTO roomTreeDTO : roomTreeDTOS) {
                if (!regionCode.equals(roomTreeDTO.getRegionCode())) {
                    regionCode = roomTreeDTO.getRegionCode();
                    Map<String, Object> map = new HashMap<>();
                    map.put("code", roomTreeDTO.getRegionCode());
                    map.put("pid", roomTreeDTO.getVillageCode());
                    map.put("name", roomTreeDTO.getRegionName());
                    map.put("level", "2");
                    data.add(map);
                }
            }
            for (RoomTreeDTO roomTreeDTO : roomTreeDTOS) {
                if (!buildingCode.equals(roomTreeDTO.getBuildingCode())) {
                    buildingCode = roomTreeDTO.getBuildingCode();
                    Map<String, Object> map = new HashMap<>();
                    map.put("code", roomTreeDTO.getBuildingCode());
                    map.put("pid", roomTreeDTO.getRegionCode());
                    map.put("name", roomTreeDTO.getBuildingName());
                    map.put("level", "3");
                    data.add(map);
                }
            }
            for (RoomTreeDTO roomTreeDTO : roomTreeDTOS) {
                if (!unitCode.equals(roomTreeDTO.getUnitCode())) {
                    unitCode = roomTreeDTO.getUnitCode();
                    Map<String, Object> map = new HashMap<>();
                    map.put("code", roomTreeDTO.getUnitCode());
                    map.put("pid", roomTreeDTO.getBuildingCode());
                    map.put("name", roomTreeDTO.getUnitName());
                    map.put("level", "4");
                    data.add(map);
                }
            }
            for (RoomTreeDTO roomTreeDTO : roomTreeDTOS) {
                if (!roomCode.equals(roomTreeDTO.getRoomCode())) {
                    roomCode = roomTreeDTO.getRoomCode();
                    Map<String, Object> map = new HashMap<>();
                    map.put("code", roomTreeDTO.getRoomCode());
                    map.put("pid", roomTreeDTO.getUnitCode());
                    map.put("name", roomTreeDTO.getRoomCode());
                    map.put("level", "5");
                    data.add(map);
                }
            }
            JSONArray result = EncapsulationTreeUtil.listToTree(JSONArray.parseArray(JSON.toJSONString(data)), "code", "pid", "villageChoose2DTO");
//            redisUtil.set(Constants.REDISKEY_PROJECT + Constants.VILLAGE_TREE + organizationId, result);
            return result;
        }
//        return null;
//    }



    public JSONArray busineseTree(String organizationId) throws Exception {
//        try {
//            String tree = redisUtil.get(Constants.REDISKEY_PROJECT + Constants.VILLAGE_TREE + organizationId).toString();
//            if (StringUtils.isNotBlank(tree) && null != tree && !"null".equals(tree) && !"".equals(tree)) {
//                JSONArray villageTree = JSONArray.parseArray(tree);
//                return villageTree;
//            }
//        } catch (Exception e) {
            List<RoomTreeDTO> roomTreeDTOS = villageChooseDAO.findBusinese(organizationId);
            List<Map<String, Object>> data = new ArrayList<>();
            String villageCode = "";
            String regionCode = "";
            String buildingCode = "";
            String unitCode = "";
            String roomCode = "";

            for (RoomTreeDTO roomTreeDTO : roomTreeDTOS) {
                if (!villageCode.equals(roomTreeDTO.getVillageCode())) {
                    villageCode = roomTreeDTO.getVillageCode();
                    Map<String, Object> map = new HashMap<>();
                    map.put("code", roomTreeDTO.getVillageCode());
                    map.put("pid", "");
                    map.put("name", roomTreeDTO.getVillageName());
                    map.put("level", "1");
                    data.add(map);
                }
            }
            for (RoomTreeDTO roomTreeDTO : roomTreeDTOS) {
                if (!regionCode.equals(roomTreeDTO.getRegionCode())) {
                    regionCode = roomTreeDTO.getRegionCode();
                    Map<String, Object> map = new HashMap<>();
                    map.put("code", roomTreeDTO.getRegionCode());
                    map.put("pid", roomTreeDTO.getVillageCode());
                    map.put("name", roomTreeDTO.getRegionName());
                    map.put("level", "2");
                    data.add(map);
                }
            }
            for (RoomTreeDTO roomTreeDTO : roomTreeDTOS) {
                if (!buildingCode.equals(roomTreeDTO.getBuildingCode())) {
                    buildingCode = roomTreeDTO.getBuildingCode();
                    Map<String, Object> map = new HashMap<>();
                    if (!"X".equals(roomTreeDTO.getBuildingName())){
                        map.put("code", roomTreeDTO.getBuildingCode());
                        map.put("pid", roomTreeDTO.getRegionCode());
                        map.put("name", roomTreeDTO.getBuildingName());
                        map.put("level", "3");
                        data.add(map);
                    }
                }
            }
            for (RoomTreeDTO roomTreeDTO : roomTreeDTOS) {
                if (!unitCode.equals(roomTreeDTO.getUnitCode())) {
                    unitCode = roomTreeDTO.getUnitCode();
                    Map<String, Object> map = new HashMap<>();
                    if (!"X".equals(roomTreeDTO.getBuildingName())){
                        map.put("code", roomTreeDTO.getUnitCode());
                        map.put("pid", roomTreeDTO.getBuildingCode());
                        map.put("name", roomTreeDTO.getUnitName());
                        map.put("level", "4");
                        data.add(map);
                    }
                }
            }
            for (RoomTreeDTO roomTreeDTO : roomTreeDTOS) {
                if (!roomCode.equals(roomTreeDTO.getRoomCode())) {
                    roomCode = roomTreeDTO.getRoomCode();
                    Map<String, Object> map = new HashMap<>();
                    if ("X".equals(roomTreeDTO.getBuildingName())){
                        map.put("code", roomTreeDTO.getRoomCode());
                        map.put("pid", roomTreeDTO.getRegionCode());
                        map.put("name", roomTreeDTO.getRoomCode());
                        map.put("level", "5");
                    }else {
                        map.put("code", roomTreeDTO.getRoomCode());
                        map.put("pid", roomTreeDTO.getUnitCode());
                        map.put("name", roomTreeDTO.getRoomCode());
                        map.put("level", "5");
                    }
                    data.add(map);
                }
            }
            JSONArray result = EncapsulationTreeUtil.listToTree(JSONArray.parseArray(JSON.toJSONString(data)), "code", "pid", "villageChoose2DTO");
//            redisUtil.set(Constants.REDISKEY_PROJECT + Constants.VILLAGE_TREE + organizationId, result);
            return result;
        }
//        return null;
//    }















    public JSONArray findTreeAll(String organizationId) throws Exception {
        List<RoomTreeDTO> roomTreeDTOS = villageChooseDAO.findAllRoom(organizationId);
        List<Map<String,Object>> data = new ArrayList<>();
        String villageCode = "";
        String regionCode = "";
        String buildingCode = "";
        String unitCode = "";
        String roomCode = "";

        for (RoomTreeDTO roomTreeDTO:roomTreeDTOS) {
            if (!villageCode.equals(roomTreeDTO.getVillageCode())){
                villageCode = roomTreeDTO.getVillageCode();
                Map<String,Object> map = new HashMap<>();
                map.put("code",roomTreeDTO.getVillageCode());
                map.put("pid","");
                map.put("name",roomTreeDTO.getVillageName());
                map.put("level","1");
                data.add(map);
            }
        }
        for (RoomTreeDTO roomTreeDTO:roomTreeDTOS) {
            if (!regionCode.equals(roomTreeDTO.getRegionCode())){
                regionCode=roomTreeDTO.getRegionCode();
                Map<String,Object> map = new HashMap<>();
                map.put("code",roomTreeDTO.getRegionCode());
                map.put("pid",roomTreeDTO.getVillageCode());
                map.put("name",roomTreeDTO.getRegionName());
                map.put("level","2");
                data.add(map);
            }
        }
        for (RoomTreeDTO roomTreeDTO:roomTreeDTOS) {
            if (!buildingCode.equals(roomTreeDTO.getBuildingCode())){
                buildingCode=roomTreeDTO.getBuildingCode();
                Map<String,Object> map = new HashMap<>();
                map.put("code",roomTreeDTO.getBuildingCode());
                map.put("pid",roomTreeDTO.getRegionCode());
                map.put("name",roomTreeDTO.getBuildingName());
                map.put("level","3");
                data.add(map);
            }
        }
        for (RoomTreeDTO roomTreeDTO:roomTreeDTOS) {
            if (!unitCode.equals(roomTreeDTO.getUnitCode())){
                unitCode=roomTreeDTO.getUnitCode();
                Map<String,Object> map = new HashMap<>();
                map.put("code",roomTreeDTO.getUnitCode());
                map.put("pid",roomTreeDTO.getBuildingCode());
                map.put("name",roomTreeDTO.getUnitName());
                map.put("level","4");
                data.add(map);
            }
        }
        for (RoomTreeDTO roomTreeDTO:roomTreeDTOS) {
            if (!roomCode.equals(roomTreeDTO.getRoomCode())){
                roomCode=roomTreeDTO.getRoomCode();
                Map<String,Object> map = new HashMap<>();
                map.put("code",roomTreeDTO.getRoomCode());
                map.put("pid",roomTreeDTO.getUnitCode());
                map.put("name",roomTreeDTO.getRoomCode());
                map.put("level","5");
                data.add(map);
            }
        }
        JSONArray result = EncapsulationTreeUtil.listToTree(JSONArray.parseArray(JSON.toJSONString(data)),"code","pid","villageChoose2DTO");
        return result;
    }


    public void saveAllVillageTree(){
        List<OrganizationDO> organizationDOS = organizationDAO.findAll();
        for (OrganizationDO organizationDO:organizationDOS) {
            try {
                JSONArray jsonArray = findTreeAll(organizationDO.getOrganizationId());
                if (0<jsonArray.size()){
                    redisUtil.set(Constants.REDISKEY_PROJECT+Constants.VILLAGE_TREE+organizationDO.getOrganizationId(),jsonArray);
                }
            } catch (Exception e) {
                logger.error("【小区树服务类】保存小区树异常，ERROR：{}",e);
                continue;
            }
        }
    }




















//    public List<VillageChoose2DTO> findTree(String organizationId) throws Exception {
//        String villageCodes = null;
//        String regionCodes = null;
//        String buildingCodes = null;
//        String unitCodes = null;
//
//        List<VillageChooseDTO> villageChooseDTOS = villageChooseDAO.findVillage(organizationId);
//        String temp1 = "";
//        for (int i = 0; i < villageChooseDTOS.size(); i++) {
//            String temp = villageChooseDTOS.get(i).getCode();
//            String gg  = "'" + temp + "'"+",";
//            temp1 = temp1+gg;
//        }
//        villageCodes = temp1.substring(0,temp1.length()-1);
//
//        List<VillageChooseDTO> villageChooseDTOS2 = villageChooseDAO.findRegion(villageCodes);
//        String temp2 = "";
//        for (int i = 0; i < villageChooseDTOS2.size(); i++) {
//            String temp = villageChooseDTOS2.get(i).getCode();
//            String gg  = "'" + temp + "'"+",";
//            temp2 = temp2+gg;
//        }
//        regionCodes = temp2.substring(0,temp2.length()-1);
//
//        List<VillageChooseDTO> villageChooseDTOS3 = villageChooseDAO.findBuilding(regionCodes);
//        String temp3 = "";
//        for (int i = 0; i < villageChooseDTOS3.size(); i++) {
//            String temp = villageChooseDTOS3.get(i).getCode();
//            String gg  = "'" + temp + "'"+",";
//            temp3 = temp3+gg;
//        }
//        buildingCodes = temp3.substring(0,temp3.length()-1);
//
//        List<VillageChooseDTO> villageChooseDTOS4 = villageChooseDAO.findUnit(buildingCodes);
//        String temp4 = "";
//        for (int i = 0; i < villageChooseDTOS4.size(); i++) {
//            String temp = villageChooseDTOS4.get(i).getCode();
//            String gg  = "'" + temp + "'"+",";
//            temp4 = temp4+gg;
//        }
//        unitCodes = temp4.substring(0,temp4.length()-1);
//
//        List<VillageChooseDTO> villageChooseDTOS5 = villageChooseDAO.findRoom(unitCodes);
//        for (int i = 0;i<villageChooseDTOS2.size();i++){
//            villageChooseDTOS.add(villageChooseDTOS2.get(i));
//        }
//        for (int i = 0;i<villageChooseDTOS3.size();i++){
//            villageChooseDTOS.add(villageChooseDTOS3.get(i));
//        }
//        for (int i = 0;i<villageChooseDTOS4.size();i++){
//            villageChooseDTOS.add(villageChooseDTOS4.get(i));
//        }
//        for (int i = 0;i<villageChooseDTOS5.size();i++){
//            villageChooseDTOS.add(villageChooseDTOS5.get(i));
//        }
//        List<VillageChoose2DTO> villageChoose2DTOS = JSONObject.parseArray(JSONArray.toJSON(villageChooseDTOS).toString(), VillageChoose2DTO.class);
//        return EncapsulationTreeUtil.getTree(villageChoose2DTOS,"code","pid","villageChoose2DTO");
//    }




}
