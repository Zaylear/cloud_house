package com.rbi.interactive.controller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.interactive.abnormal.*;
import com.rbi.interactive.dao.UserInfoDAO;
import com.rbi.interactive.entity.ParkingSpaceCostDetailDO;
import com.rbi.interactive.entity.UserInfoDO;
import com.rbi.interactive.entity.dto.BillsBackDTO;
import com.rbi.interactive.entity.dto.ChargeItemBackDTO;
import com.rbi.interactive.entity.dto.HouseAndProprietorDTO;
import com.rbi.interactive.service.FrontOfficeCashierService;
import com.rbi.interactive.utils.JwtToken;
import com.rbi.interactive.utils.PageData;
import com.rbi.interactive.utils.ResponseVo;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/cash/register")
public class FrontOfficeCashierController {

    private final static Logger logger = LoggerFactory.getLogger(FrontOfficeCashierController.class);

    @Autowired
    FrontOfficeCashierService frontOfficeCashierService;

    @Autowired
    UserInfoDAO userInfoDAO;
    /**
     * 分页查询客户住房信息
     * @param jsonObject
     * @param request
     * @return
     */
    @PostMapping("/findHousePage")
    public ResponseVo<PageData> findHousePage(@RequestBody JSONObject jsonObject, HttpServletRequest request){

        int pageNo = jsonObject.getInteger("pageNo");
        int pageSize = jsonObject.getInteger("pageSize");
        String token = request.getHeader("appkey");
        String villageCode = jsonObject.getString("villageCode");
        String regionCode = jsonObject.getString("regionCode");
        String buildingCode = jsonObject.getString("buildingCode");
        String unitCode = jsonObject.getString("unitCode");
        String roomCode = jsonObject.getString("roomCode");
        String mobilePhone = jsonObject.getString("mobilePhone");
        String idNumber = jsonObject.getString("idNumber");
        String surname = jsonObject.getString("surname");

        try {
            PageData pageData = frontOfficeCashierService.findHousePageS(pageNo,pageSize,token,villageCode,regionCode,buildingCode,unitCode,roomCode,mobilePhone,idNumber,surname);
            return ResponseVo.build("1000","请求成功",pageData);
        } catch (RoomConfigException e){
            logger.error("【收费项目查询】房间号配置信息错误 ERROR：{}",e);
            return ResponseVo.build("1002",e.getMessage()+"收费项目配置信息错误！");
        } catch (Exception e) {
            logger.error("【收费项目查询】服务器处理异常 ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }


    /**
     * 根据房间号查询客户住房信息
     * @param jsonObject
     * @param request
     * @return
     */
    @RequestMapping(value = "/findHouseByRoomCode",method = RequestMethod.POST)
    public ResponseVo<List<HouseAndProprietorDTO>> findHouseByRoomCode(@RequestBody JSONObject jsonObject,HttpServletRequest request){

        try {
            String token = request.getHeader("appkey");
            List<HouseAndProprietorDTO> houseAndProprietorDTOS = frontOfficeCashierService.findHouseByRoomCode(jsonObject,token);
            return ResponseVo.build("1000","请求成功",houseAndProprietorDTOS);
        } catch (Exception e){
            logger.error("【根据房间号查询客户住房信息】服务器处理异常 ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 根据房间编号编号查询关联收费项目
     * @param jsonObject
     * @param request
     * @return
     */
    @RequestMapping(value = "/findChargeItem",method = RequestMethod.POST)
    public ResponseVo<List<ChargeItemBackDTO>> findChargeItem(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        String token = request.getHeader("appkey");

        String roomCode = jsonObject.getString("roomCode");
        try {
            List<ChargeItemBackDTO> chargeItemBackDTOS = frontOfficeCashierService.findChargeItem(roomCode,token);
            logger.info("【收费项目查询】查询成功data={}",chargeItemBackDTOS);
            return ResponseVo.build("1000","请求成功",chargeItemBackDTOS);
        } catch (NullPointerException e){
            logger.error("【收费项目查询】该房间未绑定收费项目！ERROR：{}",e);
            return ResponseVo.build("1005","该房间未绑定收费项目！");
        }catch (Exception e) {
            logger.error("【收费项目查询】服务器处理异常！ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 计算车位租赁费
     * @param jsonObject
     * @param request
     * @return
     */
    @RequestMapping(value = "/calculate_parking_space_rental_fee",method = RequestMethod.POST)
    public ResponseVo<List<ParkingSpaceCostDetailDO>> calculateParkingSpaceRentalFee(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
            List<ParkingSpaceCostDetailDO> parkingSpaceCostDetailDOList = frontOfficeCashierService.parkingSpaceRentalFeeCost(jsonObject,userInfoDO.getOrganizationId());
            return ResponseVo.build("1000","请求成功",parkingSpaceCostDetailDOList);
        } catch (RentalParkingSpaceException e){
            logger.error("【费用查询】未找到该客户以前的租车位信息，e：{}",e);
            return ResponseVo.build("1005","未找到该客户以前的租车位信息！");
        }catch (Exception e) {
            logger.error("【费用查询】服务器处理失败，e：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 计算车位租赁费
     * @param jsonObject
     * @param request
     * @return
     */
    @RequestMapping(value = "/calculate_parking_space_management_fee",method = RequestMethod.POST)
    public ResponseVo<ParkingSpaceCostDetailDO> calculateParkingSpaceManagementFee(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
            ParkingSpaceCostDetailDO parkingSpaceCostDetailDO = frontOfficeCashierService.parkingManagementFeeCost(jsonObject,userInfoDO.getOrganizationId());
            return ResponseVo.build("1000","请求成功",parkingSpaceCostDetailDO);
        } catch (ParkingSpaceDueTimeException e){
            logger.error("【费用查询】未找到车位费到期时间，e：{}",e);
            return ResponseVo.build("1005","未找到车位费到期时间！");
        }catch (Exception e) {
            logger.error("【费用查询】服务器处理失败，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 根据收费规则计算查询收取费用
     * @return
     */
    @RequestMapping(value = "/findCost",method = RequestMethod.POST)
    public ResponseVo<Map<String,Object>> findCost(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");

            Map<String,Object> map = frontOfficeCashierService.findCost(jsonObject,userId);
            return ResponseVo.build("1000","请求成功",map);
        }catch (NullPointerException e){
            logger.error("【费用查询】未找到车位费到期时间，e：{}",e);
            return ResponseVo.build("1005","收费项目配置错误");
        }catch (ParkingSpaceDueTimeException e){
            logger.error("【费用查询】未找到车位费到期时间，e：{}",e);
            return ResponseVo.build("1005","未找到车位费到期时间");
        }catch (Exception e) {
            logger.error("【费用查询】服务器处理失败，e：{}",e);
            return ResponseVo.build("1002","服务器处理失败");
        }
    }

    /**
     * 添加账单即收款存库
     * @return
     */
    @RequestMapping(value = "/addBill",method = RequestMethod.POST)
    public ResponseVo<List<BillsBackDTO> > addBill(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        try {
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            List<BillsBackDTO> billsBackDTOS = frontOfficeCashierService.addBill(jsonObject,userId);
            return ResponseVo.build("1000","请求成功！",billsBackDTOS);
        }catch (PaymentMethodException e){
            logger.error("【客户缴费添加请求类】支付方式选择异常，应选择三通费抵扣单！");
            return ResponseVo.build("1002","支付方式选择异常，应选择三通费抵扣单！");
        }catch (ParkingSpaceDueTimeException e){
            logger.error("【客户缴费添加请求类】车位到期时间验证失败！");
            return ResponseVo.build("1002","车位到期时间验证失败！");
        } catch (DueTimeException e){
            logger.error("【客户缴费添加请求类】物业费到期时间异常！");
            return ResponseVo.build("1002","物业费到期时间异常，请检查该客户是否已进入正常缴费！");
        }catch (ValidationDataAbnormalException e){
            logger.error("【客户缴费添加请求类】数据效验异常！");
            return ResponseVo.build("1002","数据效验失败，请重新计算费用！");
        }catch (Exception e) {
            logger.error("【客户缴费添加请求类】服务器处理异常！ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理失败！");
        }
    }

    /**
     * 打印收费单据
     * @param jsonObject
     * @return
     */
    @PostMapping("/printBilles")
    public ResponseVo<String> printBilles(@RequestBody JSONObject jsonObject){
        try {
            return ResponseVo.build("1000","请求成功",frontOfficeCashierService.printBilles(jsonObject));
        }catch (Exception e){
            logger.error("【单据打印类】打印单据异常，ERROR：{}",e);
            return ResponseVo.build("1002","打印单据异常");
        }
    }

    /**
     * 导入旧账单，通过表格导入
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/importOldBills")
    public ResponseVo importOldBills(MultipartFile file, HttpServletRequest request){
        try {
            if (file.getOriginalFilename().indexOf("xls")>-1||file.getOriginalFilename().indexOf("xlsx")>-1) {

            }else {
                return ResponseVo.build("1005","文件类型错误，请使用Excel文件！");
            }
            String token = request.getHeader("appkey");
            String userId = JwtToken.getClaim(token,"userId");
            Map<String,Object> map = frontOfficeCashierService.importOldBills(file,userId);
            return ResponseVo.build("1000","请求成功",map);
        } catch (Exception e) {
            logger.error("【收费请求类】批量导入旧账单失败！，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理异常，ERROR：{}",e);
        }

    }

    @PostMapping("/expense_deduction_re_sum")
    public ResponseVo expenseDeductionAndReSumController(@RequestBody JSONObject jsonObject){
        try {
            Map<String,Object> map = frontOfficeCashierService.expenseDeductionAndReSum(jsonObject);
            return ResponseVo.build("1000","请求成功",map);
        }catch (NullPointerException e){
            logger.error("【收费请求类】请先计算车位租赁费！，ERROR：{}",e);
            return ResponseVo.build("1005","请先计算车位租赁费");
        }catch (Exception e){
            logger.error("【收费请求类】成算实收金额失败！，ERROR：{}",e);
            return ResponseVo.build("1002","服务器处理异常");
        }
    }
}
