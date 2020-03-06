package com.rbi.admin.service.connect;

import com.rbi.admin.dao.structure.StructureDAO;
import com.rbi.admin.entity.dto.structure.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StructureService {
    @Autowired(required = false)
    StructureDAO structureDAO;


    public List<OrganizationStructureDTO> findOrganizationId2(){
        List<OrganizationStructureDTO>  organizationStructureDTOS = structureDAO.findOrganizationId2();
        return organizationStructureDTOS;
    }

    public List<OrganizationStructureDTO> findSonOrganizationId(String pid){
        List<OrganizationStructureDTO> organizationStructureDTOS = structureDAO.findSonOrganizationId(pid);
        return organizationStructureDTOS;
    }

    public List<DepartmentDTO> findDepartmentId(){
        List<DepartmentDTO>  departmentDTOS = structureDAO.findDepartmentId();
        return departmentDTOS;
    }

    public List<DepartmentDTO> findSonDepartmentId(String pid){
        List<DepartmentDTO> departmentDTOS = structureDAO.findSonDepartmentId(pid);
        return departmentDTOS;
    }


    public List<VillageStructureDTO> findByOrganizationId(String organizationId){
        List<VillageStructureDTO> villageStructureDTOS = structureDAO.findByOrganizationId(organizationId);
        return villageStructureDTOS;
    }

    public String findOrganizationId(String userId){
        String organizationId = structureDAO.findOrganizationId(userId);
        return organizationId;
    }

    public String findOrganizationName(String userId){
        String organizationId = structureDAO.findOrganizationName(userId);
        return organizationId;
    }

    public List<RegionStructureDTO> findByVillageCode(String villageCode){
        List<RegionStructureDTO> regionStructureDTOS = structureDAO.findByVillaegCode(villageCode);
        return regionStructureDTOS;
    }

    public List<BuildingStructureDTO> findByRegionCode(String regionCode){
        List<BuildingStructureDTO> buildingStructureDTOS = structureDAO.findByRegionCode(regionCode);
        return buildingStructureDTOS;
    }

    public List<UnitStructureDTO> findByBuidingCode(String buildingCode){
        List<UnitStructureDTO> unitStructureDTOS = structureDAO.findByBuidingCode(buildingCode);
        return unitStructureDTOS;
    }

    public List<RoomStructureDTO> findByUnitCode(String unitCode){
        List<RoomStructureDTO> roomStructureDTOS = structureDAO.findByUnitCode(unitCode);
        return roomStructureDTOS;
    }

    public String findNameByUserId(@Param("userId") String userId){
        return structureDAO.findNameByUserId(userId);
    }

    public List<ProvinceDTO>chooseProvince(){
        List<ProvinceDTO> provinceDTOS = structureDAO.chooseProvince();
        return provinceDTOS;
    }

    public List<CityDTO>chooseCity(String provinceCode){
        List<CityDTO> cityDTOS = structureDAO.choozeCity(provinceCode);
        return cityDTOS;
    }

    public List<DistrictDTO>chooseDistrict(String cityCode){
        List<DistrictDTO> districtDTOS = structureDAO.choozeDistrict(cityCode);
        return districtDTOS;
    }





}
