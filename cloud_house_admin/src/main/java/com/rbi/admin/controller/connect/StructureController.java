package com.rbi.admin.controller.connect;


import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.entity.dto.structure.*;
import com.rbi.admin.service.connect.StructureService;
import com.rbi.admin.util.JwtToken;
import com.rbi.admin.util.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/structure")
public class StructureController {
    @Autowired
    StructureService structureService;


    @RequestMapping(value = "/findStructureOrganizationId",method = RequestMethod.POST)
    public ResponseVo<List<OrganizationStructureDTO>> findOrganizationId2(){
        try {
            List<OrganizationStructureDTO> organizationStructureDTOS = structureService.findOrganizationId2();
            return ResponseVo.build("1000", "查询组织成功",organizationStructureDTOS);
        }catch (Exception e){
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/findSonOrganizationId",method = RequestMethod.POST)
    public ResponseVo<List<OrganizationStructureDTO>> findSonOrganizationId(@RequestBody JSONObject json){
        try {
            String pid = json.getString("pid");
            List<OrganizationStructureDTO> organizationStructureDTOS = structureService.findSonOrganizationId(pid);
            return ResponseVo.build("1000", "查询成功",organizationStructureDTOS);
        }catch (Exception e){
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/findDepartmentId",method = RequestMethod.POST)
    public ResponseVo<List<DepartmentDTO>> findDepartmentId(){
        try {
            List<DepartmentDTO> departmentDTOS = structureService.findDepartmentId();
            return ResponseVo.build("1000", "查询组织成功",departmentDTOS);
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/findSonDepartmentId",method = RequestMethod.POST)
    public ResponseVo<List<DepartmentDTO>> findSonDepartmentId(@RequestBody JSONObject json){
        try {
            String pid = json.getString("pid");
            List<DepartmentDTO> departmentDTOS = structureService.findSonDepartmentId(pid);
            return ResponseVo.build("1000", "查询成功",departmentDTOS);
        }catch (Exception e){
            System.out.println(e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }





    //*************************************************************************************************************
    @RequestMapping(value = "/findByOrganizationId",method = RequestMethod.POST)
    public ResponseVo<List<VillageStructureDTO>> findByOrganizationId(HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            String organizationId = structureService.findOrganizationId(userId);
            List<VillageStructureDTO> villageStructureDTOS = structureService.findByOrganizationId(organizationId);
            return ResponseVo.build("1000", "查询成功",villageStructureDTOS);
        }catch (Exception e){
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/findByVillageCode",method = RequestMethod.POST)
    public ResponseVo<List<RegionStructureDTO>> findByVillageCode(@RequestBody JSONObject request){
        try {
            String villageCode = request.getString("villageCode");
            List<RegionStructureDTO> regionStructureDTOS = structureService.findByVillageCode(villageCode);
            return ResponseVo.build("1000", "查询成功",regionStructureDTOS);
        }catch (Exception e){
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/findByRegionCode",method = RequestMethod.POST)
    public ResponseVo<List<BuildingStructureDTO>> findByRegionCode(@RequestBody JSONObject request){
        try {
            String regionCode = request.getString("regionCode");
            List<BuildingStructureDTO> buildingStructureDTOS = structureService.findByRegionCode(regionCode);
            return ResponseVo.build("1000", "查询成功",buildingStructureDTOS);
        }catch (Exception e){
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/findByBuildingCode",method = RequestMethod.POST)
    public ResponseVo<List<UnitStructureDTO>> findByBuildingCode(@RequestBody JSONObject request){
        try {
            String buildingCode = request.getString("buildingCode");
            List<UnitStructureDTO> unitStructureDTOS = structureService.findByBuidingCode(buildingCode);
            return ResponseVo.build("1000", "查询成功",unitStructureDTOS);
        }catch (Exception e){
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/findByUnitCode",method = RequestMethod.POST)
    public ResponseVo<List<RoomStructureDTO>> findByUnitCode(@RequestBody JSONObject request){
        try {
            String unitCode = request.getString("unitCode");
            List<RoomStructureDTO> roomStructureDTOS = structureService.findByUnitCode(unitCode);
            return ResponseVo.build("1000", "查询成功",roomStructureDTOS);
        }catch (Exception e){
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/chooseProvince",method = RequestMethod.POST)
    public ResponseVo<List<ProvinceDTO>> chooseProvince(){
        try {
            List<ProvinceDTO> provinceDTOS = structureService.chooseProvince();
            return ResponseVo.build("1000", "查询成功",provinceDTOS);
        }catch (Exception e){
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/chooseCity",method = RequestMethod.POST)
    public ResponseVo<List<CityDTO>> choozeCity(@RequestBody JSONObject request){
        try {
            String provinceCode = request.getString("provinceCode");
            List<CityDTO> cityDTOS = structureService.chooseCity(provinceCode);
            return ResponseVo.build("1000", "查询成功",cityDTOS);
        }catch (Exception e){
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

    @RequestMapping(value = "/chooseDistrict",method = RequestMethod.POST)
    public ResponseVo<List<DistrictDTO>> chooseDistrict(@RequestBody JSONObject request){
        try {
            String cityCode = request.getString("cityCode");
            List<DistrictDTO> districtDTOS = structureService.chooseDistrict(cityCode);
            return ResponseVo.build("1000", "查询成功",districtDTOS);
        }catch (Exception e){
            return ResponseVo.build("1002","服务器处理异常");
        }
    }

}
