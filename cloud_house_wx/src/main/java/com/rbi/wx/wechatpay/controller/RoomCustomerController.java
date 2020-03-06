package com.rbi.wx.wechatpay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rbi.wx.wechatpay.dto.RoomInfo;
import com.rbi.wx.wechatpay.dto.RoomUserDTO;
import com.rbi.wx.wechatpay.dto.houseinfo.HouseInfoDTO;
import com.rbi.wx.wechatpay.dto.houseinfo.UserDTO;
import com.rbi.wx.wechatpay.dto.indexroom.IndexRoomDTO;
import com.rbi.wx.wechatpay.dto.indexroom.UpdateRoomUserDTO;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.dto.User;
import com.rbi.wx.wechatpay.dto.UserIdentityEntity;
import com.rbi.wx.wechatpay.mapper.RoomCustomerMapper;
import com.rbi.wx.wechatpay.requestentity.roomcustomerrequestentity.*;
import com.rbi.wx.wechatpay.service.impl.RoomCustomerServiceImpl;
import com.rbi.wx.wechatpay.util.IpUtil;
import io.swagger.annotations.*;
import org.apache.ibatis.annotations.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@Api(tags = "RoomActionAll")
public class RoomCustomerController {
    @Autowired
    private RoomCustomerServiceImpl roomCustomerService;


    @Autowired
    private RoomCustomerMapper roomCustomerMapper;

    @ApiOperation(value="/mine/tenantinfo&&/mine/deputyinfo&&/chargepay/tenant&&/chargepay/tenant&&  查询业主还是副业主", notes="根据userId和roomCode查询")
    @ApiImplicitParams({
            @ApiImplicitParam(defaultValue = "D9313E7909CEA4CD9DE7A026DD20517B",name = "APPKEY", value = "加密过后的userId", required = true, dataType = "String",paramType = "Header")
    })
    @PostMapping("/wx/roomuser")
    @ResponseBody
    /**
     * identity 查询的是副业主还是租客
     * userId 当前用户的 userid
     * 获取个人页面
     * 副业主
     * 或者
     * 租客信息
     */
    public JsonEntityUtil<List<RoomInfo>> getRoomUser(@RequestBody  @ApiParam(value = "roomCode和identity",required = true) WxRoomUserRequestEntity wxRoomUserRequestEntity, HttpServletRequest request){
        return this.roomCustomerService.getRoomUser(request.getHeader("APPKEY"),wxRoomUserRequestEntity.getIdentity(),wxRoomUserRequestEntity.getRoomCode()==null?null:wxRoomUserRequestEntity.getRoomCode());
    }
    @ApiOperation(value="租客副业主信息获取", notes="根据userId查询")
    @ApiImplicitParams({
            @ApiImplicitParam(defaultValue = "D9313E7909CEA4CD9DE7A026DD20517B",name = "APPKEY", value = "加密过后的userId", required = true, dataType = "String",paramType = "Header")
    })
    @PostMapping("/wx/indexroomuser")
    @ResponseBody
    public JsonEntityUtil getIndexRoomUser(@ApiParam(value = "实体")@RequestBody IndexRoomUserRequestEntity indexRoomUserRequestEntity,HttpServletRequest request){
        return this.roomCustomerService.getIndexRoomUser(request.getHeader("APPKEY"),indexRoomUserRequestEntity.getIdentity()
                ,indexRoomUserRequestEntity.getPageSize(),indexRoomUserRequestEntity.getPageNum());

    }

    /**
     * 获取业主
     * @param
     * @param
     * @return
     */
    @PostMapping("/wx/indexroomusergetporperty")
    @ResponseBody
    public JsonEntityUtil getPorperty(@RequestBody JSONObject jsonObject){
        return this.roomCustomerService.getPorperty(jsonObject.getString("roomCode"));
    }
    @ApiOperation(value="/mine/deputyadd&&/mine/tenantadd  新增副业主或者租客", notes="roomlist和手机号和身份绑定")
    @PostMapping("/wx/adduser")
    @ResponseBody
    public JsonEntityUtil addUser(@RequestBody @ApiParam(value = "信息的实体" ,required = true) AddUserRequestEntity addUserRequestEntity,HttpServletRequest request){
        return this.roomCustomerService.addUser(
                addUserRequestEntity.getRoomList(),
                addUserRequestEntity.getUser(),
                addUserRequestEntity.getUserIdentityEntity(),
                addUserRequestEntity.getVerificationCode(),
                request.getHeader("APPKEY")
        );
    }
    @PostMapping("/wx/updatetenantroom")
    @ResponseBody
    public JsonEntityUtil updateTenant(@RequestBody UpdateTenantDTO updateTenantDTO){
        return   this.roomCustomerService.updateTenant(updateTenantDTO);
    }
    @ApiOperation(value="  新增租客", notes="roomlist和手机号和身份绑定")
    @PostMapping("/wx/addtenantuser")
    @ResponseBody
    public JsonEntityUtil addTenantUser(@RequestBody @ApiParam(value = "信息的实体" ,required = true) AddTenantUserDTO addTenantUserDTO){
      return this.roomCustomerService.addTenantUser(addTenantUserDTO);
    }
    @ApiOperation(value="  获取信息租客", notes="roomlist和手机号和身份绑定")
    @PostMapping("/wx/gettenantroom")
    @ResponseBody
    public JsonEntityUtil getTenantRoom(@RequestBody @ApiParam(value = "信息的实体" ,required = true) JSONObject jsonObject,HttpServletRequest request){
        return this.roomCustomerService.getTenantUser(jsonObject.getString("userId"),request.getHeader("APPKEY"));
    }
    @ApiOperation(value="/mine/tenantinfo&&/mine/changedeputy  删除按钮", notes="根据userId和roomCode删除关联")
    @PostMapping("/deputy/delete")
    @ResponseBody
    public JsonEntityUtil deleteDeputy(@RequestBody DeputyDeleteRequestEntity deputyDeleteRequestEntity, HttpServletRequest request){
        return this.roomCustomerService.deleteDeputy(deputyDeleteRequestEntity.getUserId(),deputyDeleteRequestEntity.getRoomCode(),
                deputyDeleteRequestEntity.getIdentity(),
                request.getHeader("APPKEY"),
                deputyDeleteRequestEntity.getVerificationCode());
    }



    /**
     * 房屋页面的数据获取
     * userId 当前用户的ID
     * @return
     */
    @ApiOperation(value="/tab/home  房屋信息获取", notes="根据userId查询")
    @ApiImplicitParams({
            @ApiImplicitParam(defaultValue = "D9313E7909CEA4CD9DE7A026DD20517B",name = "APPKEY", value = "加密过后的userId", required = true, dataType = "String",paramType = "Header")
    })
    @PostMapping("/indexroom")
    @ResponseBody
    public JsonEntityUtil<List<IndexRoomDTO>> getIndexRoom(HttpServletRequest request){
        System.out.println(IpUtil.getIpAddr(request));
        System.out.println(request.getRemoteAddr());
        return this.roomCustomerService.getIndexRoom(request.getHeader("APPKEY"));
    }
    /**
     * 获取房屋页面的详情信息
     * 获取房屋基本信息
     * @param houseInfoRequestEntity
     * @return
     */
    @ApiOperation(value="/chargepay/roominfo 房屋详情获取", notes="根据roomCode查询")
    @ApiImplicitParams({
            @ApiImplicitParam(defaultValue = "D9313E7909CEA4CD9DE7A026DD20517B",name = "APPKEY", value = "提高准确度 加密过后的userId", required = true, dataType = "String",paramType = "Header")
    })
    @PostMapping("/houseinfo")
    @ResponseBody
    public JsonEntityUtil<HouseInfoDTO<UserDTO,RoomUserDTO>> getHouseInfo(@RequestBody  @ApiParam(value = "房间编号",required = true) HouseInfoRequestEntity houseInfoRequestEntity, HttpServletRequest request){
        System.out.println(IpUtil.getIpAddr(request));
        return this.roomCustomerService.houseInfo(houseInfoRequestEntity.getRoomCode(),houseInfoRequestEntity.getOrganizationId(),request.getHeader("APPKEY"));
    }

    @PostMapping("/wx/updatepermission")
    @ResponseBody
    public JsonEntityUtil getPermission(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        return this.roomCustomerService.getPermission(request.getHeader("APPKEY"),jsonObject.getString("roomCode"));
    }

    @PostMapping("/wx/updategetroomcode")
    @ResponseBody
    public JsonEntityUtil updateGetRoomCode(HttpServletRequest request){
        return this.roomCustomerService.updateGetRoomCode(request.getHeader("APPKEY"));
    }
    @PostMapping("/wx/updaterroomuser")
    @ResponseBody
    public JsonEntityUtil updateRoomUser(@ApiParam(value = "实体")@RequestBody UpdateRoomUserRequestEntity updateRoomUserRequestEntity){
        return this.roomCustomerService.updateRoomUser(updateRoomUserRequestEntity);
    }
    @PostMapping("/wx/getroomcodesbyidentity")
    @ResponseBody
    private JsonEntityUtil getroomCodeByIdentity(@RequestBody JSONObject jsonObject){
        return this.roomCustomerService.getRoomCodesIdentity(jsonObject.getString("userId"),jsonObject.getString("identity"));
    }

    /**
     * 获取绑定房屋列表
     * @param request
     * @return
     */
    @PostMapping("/wx/getroomcodes")
    @ResponseBody
    public JsonEntityUtil getRoomCode(HttpServletRequest request){
        return this.roomCustomerService.getRoomCodes(request.getHeader("APPKEY"));
    }

    /**
     * 租客获取房间列表
     * @param request
     * @return
     */
    @PostMapping("/wx/tenantuser/getroomcodes")
    @ResponseBody
    public JsonEntityUtil getTenantuserRoomCode(@RequestBody JSONObject jsonObject,HttpServletRequest request){
        return this.roomCustomerService.getTenantuserRoomCode(jsonObject.getString("tenantuserId")
                ,request.getHeader("APPKEY"));
    }



    @PostMapping("/wx/addroomuserininedx")
    @ResponseBody
    public JsonEntityUtil addRoomUserInIndex(@RequestBody AddRoomUserInIndexDTO addRoomUserInIndexDTO){
      return   this.roomCustomerService.addRoomUserInIndex(addRoomUserInIndexDTO.getRoomCodes(),
                addRoomUserInIndexDTO.getIdentity(),
                addRoomUserInIndexDTO.getUserId(),
                addRoomUserInIndexDTO.getStartDate());
    }

    @PostMapping("/wx/roomusersubmit")
    @ResponseBody
    public JsonEntityUtil roomUserSubmit(@RequestBody AddUserRequestEntity addUserRequestEntity,HttpServletRequest request){
        return this.roomCustomerService.roomUserSubmit(addUserRequestEntity.getUser().getRealName(),
                addUserRequestEntity.getUser().getMobilePhone(),
                addUserRequestEntity.getUser().getSex(),
                addUserRequestEntity.getUser().getUserId(),
                addUserRequestEntity.getVerificationCode(),
                request.getHeader("APPKEY"),
                addUserRequestEntity.getRoomList(),
                addUserRequestEntity.getUserIdentityEntity().getIdentity()+"");
    }

    /**
     * 业主获取验证码
     * @param request
     * @return
     */
    @PostMapping("/wx/proprietorGetVerificationCode")
    @Options
    @ResponseBody
    public JsonEntityUtil proprietorGetVerificationCode(HttpServletRequest request){
        return this.roomCustomerService.proprietorGetVerificationCode(request.getHeader("APPKEY"));
    }

//    @PostMapping("/wx/addCustomerAndUser")
//    @ResponseBody
//    public JsonEntityUtil addCustomerAndUser(){
//
//    }
}
