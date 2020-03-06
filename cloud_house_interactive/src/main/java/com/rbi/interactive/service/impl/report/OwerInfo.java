package com.rbi.interactive.service.impl.report;

import com.rbi.interactive.dao.CustomerDAO;
import com.rbi.interactive.dao.RoomAndCustomerDAO;
import com.rbi.interactive.dao.RoomDAO;
import com.rbi.interactive.entity.CustomerInfoDO;
import com.rbi.interactive.entity.RoomAndCustomerDO;
import com.rbi.interactive.entity.RoomDO;
import com.rbi.interactive.entity.dto.HouseAndProprietorDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OwerInfo {

    @Autowired
    RoomDAO roomDAO;

    @Autowired
    RoomAndCustomerDAO roomAndCustomerDAO;

    @Autowired
    CustomerDAO customerDAO;

    public List<HouseAndProprietorDTO> findAllOwerInfo(String organizationId){
        List<HouseAndProprietorDTO> houseAndProprietorDTOList = new ArrayList<>();
        List<RoomDO> roomDOS = roomDAO.findAll();
        for (RoomDO roomDO:roomDOS) {
            HouseAndProprietorDTO houseAndProprietorDTO = new HouseAndProprietorDTO();
            String surnames = "";
            String mobilePhones = "";
            String idNumbers = "";
            List<RoomAndCustomerDO> roomAndCustomerDOS = roomAndCustomerDAO.findAllByRoomCodeAndLoggedOffStateAndIdentityAndOrganizationId(roomDO.getRoomCode(),0,1,organizationId);
            if (roomAndCustomerDOS.size()>0){
                for (RoomAndCustomerDO roomAndCustomerDO:roomAndCustomerDOS) {
                    CustomerInfoDO customerInfoDO = customerDAO.findByUserId(roomAndCustomerDO.getCustomerUserId());
                    surnames = surnames +";"+ customerInfoDO.getSurname();
                    mobilePhones = mobilePhones+";"+customerInfoDO.getMobilePhone();
                    idNumbers = idNumbers+";"+customerInfoDO.getIdNumber();
                }
                BeanUtils.copyProperties(houseAndProprietorDTO,roomAndCustomerDOS.get(0));
                houseAndProprietorDTO.setSurname(surnames);
                houseAndProprietorDTO.setMobilePhone(mobilePhones);
                houseAndProprietorDTO.setIdNumber(idNumbers);
                houseAndProprietorDTOList.add(houseAndProprietorDTO);
            }
        }
        return houseAndProprietorDTOList;
    }

}
