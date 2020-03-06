package com.rbi.admin.service.connect;

import com.rbi.admin.dao.connect.ChartDAO;
import com.rbi.admin.entity.dto.ChartDTO;
import com.rbi.admin.entity.edo.RoomDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChartService {
    @Autowired(required = false)
    ChartDAO chartDAO;

    public List<ChartDTO> nowChart(String roomCode, String organizationId){
        List<ChartDTO> chartDTOS = new ArrayList<>();
        Double wyf = null;
        RoomDO roomDO = chartDAO.findRoomByRoomCode(roomCode);
        if (roomDO.getRoomType() == 1){
            wyf = chartDAO.singleSum("1",organizationId,roomCode);
            ChartDTO chartDTO1  = new ChartDTO();
            chartDTO1.setName("住宅物业费");
            chartDTO1.setValue(wyf);
            chartDTOS.add(chartDTO1);
        }
        if (roomDO.getRoomType() == 2){
            wyf = chartDAO.singleSum("3",organizationId,roomCode);
            ChartDTO chartDTO2  = new ChartDTO();
            chartDTO2.setName("办公楼物业费");
            chartDTO2.setValue(wyf);
            chartDTOS.add(chartDTO2);
        }
        if (roomDO.getRoomType() == 3) {
            wyf = chartDAO.singleSum("2", organizationId, roomCode);
            ChartDTO chartDTO3  = new ChartDTO();
            chartDTO3.setName("商业物业费");
            chartDTO3.setValue(wyf);
            chartDTOS.add(chartDTO3);
        }

        Double bzj = chartDAO.singleSum("4",organizationId,roomCode);
        Double cwzlf = chartDAO.singleSum("5",organizationId,roomCode);
        Double cwglf = chartDAO.singleSum("6",organizationId,roomCode);
        Double df = chartDAO.singleSum("7",organizationId,roomCode);
        Double mjkf = chartDAO.singleSum("8",organizationId,roomCode);
        Double lscrzf = chartDAO.singleSum("9",organizationId,roomCode);

        Double zxglf = chartDAO.singleSum("12",organizationId,roomCode);
        Double ljqyf = chartDAO.singleSum("13",organizationId,roomCode);
        Double dskff = chartDAO.singleSum("14",organizationId,roomCode);
        Double trqkff = chartDAO.singleSum("15",organizationId,roomCode);
        Double dbkff = chartDAO.singleSum("16",organizationId,roomCode);
        Double sf = chartDAO.singleSum("17",organizationId,roomCode);
        Double qkxm = chartDAO.singleSum("18",organizationId,roomCode);


        ChartDTO chartDTO5  = new ChartDTO();
        chartDTO5.setName("车位租赁费");
        chartDTO5.setValue(cwzlf);
        chartDTOS.add(chartDTO5);

        ChartDTO chartDTO6  = new ChartDTO();
        chartDTO6.setName("车位管理费");
        chartDTO6.setValue(cwglf);
        chartDTOS.add(chartDTO6);

        ChartDTO chartDTO7  = new ChartDTO();
        chartDTO7.setName("电费");
        chartDTO7.setValue(df);
        chartDTOS.add(chartDTO7);


        ChartDTO chartDTO8  = new ChartDTO();
        chartDTO8.setName("门禁卡费");
        chartDTO8.setValue(mjkf);
        chartDTOS.add(chartDTO8);

        ChartDTO chartDTO9  = new ChartDTO();
        chartDTO9.setName("临时出入证费");
        chartDTO9.setValue(lscrzf);
        chartDTOS.add(chartDTO9);


        ChartDTO chartDTO12  = new ChartDTO();
        chartDTO12.setName("装修管理费");
        chartDTO12.setValue(zxglf);
        chartDTOS.add(chartDTO12);

        ChartDTO chartDTO13  = new ChartDTO();
        chartDTO13.setName("垃圾清运费");
        chartDTO13.setValue(ljqyf);
        chartDTOS.add(chartDTO13);

        ChartDTO chartDTO17  = new ChartDTO();
        chartDTO17.setName("水费");
        chartDTO17.setValue(sf);
        chartDTOS.add(chartDTO17);

        ChartDTO chartDTO18  = new ChartDTO();
        chartDTO18.setName("欠款项目");
        chartDTO18.setValue(qkxm);
        chartDTOS.add(chartDTO18);
        System.out.println("hh");
        return chartDTOS;
    }




    public List<ChartDTO> yearChart(String roomCode, String organizationId){
        List<ChartDTO> chartDTOS = new ArrayList<>();
//        Map<String,Object> map = new HashMap<>();
//        Double ze = chartDAO.yeartotalSum(roomCode);
//        ChartDTO chartDTO  = new ChartDTO();
//        chartDTO.setName("总额");
//        chartDTO.setValue(ze);
//        chartDTOS.add(chartDTO);

        Double wyf = null;
        RoomDO roomDO = chartDAO.findRoomByRoomCode(roomCode);
        if (roomDO.getRoomType() == 1){
            wyf = chartDAO.yearSum("1",organizationId,roomCode);
            ChartDTO chartDTO1  = new ChartDTO();
            chartDTO1.setName("住宅物业费");
            chartDTO1.setValue(wyf);
            chartDTOS.add(chartDTO1);
        }
        if (roomDO.getRoomType() == 2){
            wyf = chartDAO.yearSum("3",organizationId,roomCode);
            ChartDTO chartDTO2  = new ChartDTO();
            chartDTO2.setName("办公楼物业费");
            chartDTO2.setValue(wyf);
            chartDTOS.add(chartDTO2);
        }
        if (roomDO.getRoomType() == 3) {
            wyf = chartDAO.yearSum("2", organizationId, roomCode);
            ChartDTO chartDTO3  = new ChartDTO();
            chartDTO3.setName("商业物业费");
            chartDTO3.setValue(wyf);
            chartDTOS.add(chartDTO3);
        }

        Double bzj = chartDAO.yearSum("4",organizationId,roomCode);
        Double cwzlf = chartDAO.yearSum("5",organizationId,roomCode);
        Double cwglf = chartDAO.yearSum("6",organizationId,roomCode);
        Double df = chartDAO.yearSum("7",organizationId,roomCode);
        Double mjkf = chartDAO.yearSum("8",organizationId,roomCode);
        Double lscrzf = chartDAO.yearSum("9",organizationId,roomCode);

        Double zxglf = chartDAO.yearSum("12",organizationId,roomCode);
        Double ljqyf = chartDAO.yearSum("13",organizationId,roomCode);
        Double dskff = chartDAO.yearSum("14",organizationId,roomCode);
        Double trqkff = chartDAO.yearSum("15",organizationId,roomCode);
        Double dbkff = chartDAO.yearSum("16",organizationId,roomCode);
        Double sf = chartDAO.yearSum("17",organizationId,roomCode);
        Double qkxm = chartDAO.yearSum("18",organizationId,roomCode);


        ChartDTO chartDTO5  = new ChartDTO();
        chartDTO5.setName("车位租赁费");
        chartDTO5.setValue(cwzlf);
        chartDTOS.add(chartDTO5);

        ChartDTO chartDTO6  = new ChartDTO();
        chartDTO6.setName("车位管理费");
        chartDTO6.setValue(cwglf);
        chartDTOS.add(chartDTO6);

        ChartDTO chartDTO7  = new ChartDTO();
        chartDTO7.setName("电费");
        chartDTO7.setValue(df);
        chartDTOS.add(chartDTO7);


        ChartDTO chartDTO8  = new ChartDTO();
        chartDTO8.setName("门禁卡费");
        chartDTO8.setValue(mjkf);
        chartDTOS.add(chartDTO8);

        ChartDTO chartDTO9  = new ChartDTO();
        chartDTO9.setName("临时出入证费");
        chartDTO9.setValue(lscrzf);
        chartDTOS.add(chartDTO9);

        ChartDTO chartDTO12  = new ChartDTO();
        chartDTO12.setName("装修管理费");
        chartDTO12.setValue(zxglf);
        chartDTOS.add(chartDTO12);

        ChartDTO chartDTO13  = new ChartDTO();
        chartDTO13.setName("垃圾清运费");
        chartDTO13.setValue(ljqyf);
        chartDTOS.add(chartDTO13);

        ChartDTO chartDTO17  = new ChartDTO();
        chartDTO17.setName("水费");
        chartDTO17.setValue(sf);
        chartDTOS.add(chartDTO17);

        ChartDTO chartDTO18  = new ChartDTO();
        chartDTO18.setName("欠款项目");
        chartDTO18.setValue(qkxm);
        chartDTOS.add(chartDTO18);
        return chartDTOS;
    }


    public List<ChartDTO> totalChart(String roomCode, String organizationId){
        List<ChartDTO> chartDTOS = new ArrayList<>();
        Double wyf = null;
        RoomDO roomDO = chartDAO.findRoomByRoomCode(roomCode);
        if (roomDO.getRoomType() == 1){
            Double wyf1 = chartDAO.singleSum("1",organizationId,roomCode);
            if (null == wyf1){
                wyf1 = 0d;
            }
            Double wyf2 = chartDAO.PastSingleSum("amount_of_property_fee",organizationId,roomCode);
            if (null == wyf2){
                wyf2 = 0d;
            }
            wyf = wyf1 + wyf2;
            ChartDTO chartDTO1  = new ChartDTO();
            chartDTO1.setName("住宅物业费");
            chartDTO1.setValue(wyf);
            chartDTOS.add(chartDTO1);
        }
        if (roomDO.getRoomType() == 2){
            Double wyf1 = chartDAO.singleSum("2",organizationId,roomCode);
            if (null == wyf1){
                wyf1 = 0d;
            }
            Double wyf2 = chartDAO.PastSingleSum("amount_of_property_fee",organizationId,roomCode);
            if (null == wyf2){
                wyf2 = 0d;
            }
            wyf = wyf1 + wyf2;
            wyf = chartDAO.singleSum("3",organizationId,roomCode);
            ChartDTO chartDTO2  = new ChartDTO();
            chartDTO2.setName("办公楼物业费");
            chartDTO2.setValue(wyf);
            chartDTOS.add(chartDTO2);
        }

        if (roomDO.getRoomType() == 3) {
            Double wyf1 = chartDAO.singleSum("2",organizationId,roomCode);
            if (null == wyf1){
                wyf1 = 0d;
            }
            Double wyf2 = chartDAO.PastSingleSum("amount_of_property_fee",organizationId,roomCode);
            if (null == wyf2){
                wyf2 = 0d;
            }
            wyf = wyf1 + wyf2;
            ChartDTO chartDTO3  = new ChartDTO();
            chartDTO3.setName("商业物业费");
            chartDTO3.setValue(wyf);
            chartDTOS.add(chartDTO3);
        }

        Double bzj = chartDAO.singleSum("4",organizationId,roomCode);
        Double cwzlf = chartDAO.singleSum("5",organizationId,roomCode);
        Double cwglf = chartDAO.singleSum("6",organizationId,roomCode);
        Double df = chartDAO.singleSum("7",organizationId,roomCode);
        Double mjkf = chartDAO.singleSum("8",organizationId,roomCode);
        Double lscrzf = chartDAO.singleSum("9",organizationId,roomCode);

        Double zxglf1 = chartDAO.singleSum("12",organizationId,roomCode);
        if (null == zxglf1){
            zxglf1 = 0d;
        }
        Double zxglf2 = chartDAO.PastSingleSum("decoration_management_fee",organizationId,roomCode);
        if (null == zxglf2){
            zxglf2 = 0d;
        }
        Double zxglf = zxglf1+zxglf2;


        Double ljqyf = chartDAO.singleSum("13",organizationId,roomCode);
        Double dskff = chartDAO.singleSum("14",organizationId,roomCode);
        Double trqkff = chartDAO.singleSum("15",organizationId,roomCode);
        Double dbkff = chartDAO.singleSum("16",organizationId,roomCode);
        Double sf = chartDAO.singleSum("17",organizationId,roomCode);
        Double qkxm = chartDAO.singleSum("18",organizationId,roomCode);


        ChartDTO chartDTO5  = new ChartDTO();
        chartDTO5.setName("车位租赁费");
        chartDTO5.setValue(cwzlf);
        chartDTOS.add(chartDTO5);

        ChartDTO chartDTO6  = new ChartDTO();
        chartDTO6.setName("车位管理费");
        chartDTO6.setValue(cwglf);
        chartDTOS.add(chartDTO6);

        ChartDTO chartDTO7  = new ChartDTO();
        chartDTO7.setName("电费");
        chartDTO7.setValue(df);
        chartDTOS.add(chartDTO7);


        ChartDTO chartDTO8  = new ChartDTO();
        chartDTO8.setName("门禁卡费");
        chartDTO8.setValue(mjkf);
        chartDTOS.add(chartDTO8);

        ChartDTO chartDTO9  = new ChartDTO();
        chartDTO9.setName("临时出入证费");
        chartDTO9.setValue(lscrzf);
        chartDTOS.add(chartDTO9);


        ChartDTO chartDTO12  = new ChartDTO();
        chartDTO12.setName("装修管理费");
        chartDTO12.setValue(zxglf);
        chartDTOS.add(chartDTO12);

        ChartDTO chartDTO13  = new ChartDTO();
        chartDTO13.setName("垃圾清运费");
        chartDTO13.setValue(ljqyf);
        chartDTOS.add(chartDTO13);

        ChartDTO chartDTO17  = new ChartDTO();
        chartDTO17.setName("水费");
        chartDTO17.setValue(sf);
        chartDTOS.add(chartDTO17);

        ChartDTO chartDTO18  = new ChartDTO();
        chartDTO18.setName("欠款项目");
        chartDTO18.setValue(qkxm);
        chartDTOS.add(chartDTO18);
        System.out.println("hh");
        return chartDTOS;
    }
}
