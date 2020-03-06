package com.rbi.wx.wechatpay.controller;

import com.alibaba.fastjson.JSONObject;
import com.rbi.wx.wechatpay.dto.LoginUserInfoDTO;
import com.rbi.wx.wechatpay.requestentity.JsonEntityUtil;
import com.rbi.wx.wechatpay.requestentity.userinforequestentity.IndexUpdateUserNameEntity;
import com.rbi.wx.wechatpay.requestentity.userinforequestentity.LoginRequestEntity;
import com.rbi.wx.wechatpay.entity.UserBindingEntity;
import com.rbi.wx.wechatpay.redis.RedisUtil;
import com.rbi.wx.wechatpay.requestentity.userinforequestentity.UpadatePasswordRequestEntity;
import com.rbi.wx.wechatpay.requestentity.userinforequestentity.UserBindingRequestEntity;
import com.rbi.wx.wechatpay.service.impl.UserInfoServiceImpl;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
@Api(tags = "UserInfo")
public class UserInfoController {
    @Autowired
    private UserInfoServiceImpl userInfoService;
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value="缴费页面", notes="根据OpenId来进行查询是否可以登录")
    @PostMapping(value = "/wx/login",produces = "application/json")
    @ResponseBody
    public JsonEntityUtil getUserInfo(@RequestBody @ApiParam(value = "openId",required = true) LoginRequestEntity loginRequestEntity){
        if (loginRequestEntity.getOpenId()==null)
            return new JsonEntityUtil("1003","openid为空");
    return this.userInfoService.findByOpenId(loginRequestEntity.getOpenId());
    }

    /**
     * 用户绑定
     * @param userBindingRequestEntity
     * @return
     */
    @ApiOperation(value="用户绑定", notes="如果手机号存在就返回存在的usercode 不然新建一个")
    @PostMapping("/wx/userbinding")
    @ResponseBody
    public JsonEntityUtil userBinding(@RequestBody  @ApiParam(value = "用户绑定需要的东西",required = true) UserBindingRequestEntity userBindingRequestEntity){
        return this.userInfoService.userBinding(userBindingRequestEntity.getData(),userBindingRequestEntity.getOpenId());
    }

    /**
     * 绑定界面获取验证码
     * @param jsonObject
     * @return
     */
    @PostMapping("/wx/userbinding/getVerificationCode")
    @ResponseBody
    public JsonEntityUtil userBindingGetVerificationCode(@RequestBody JSONObject jsonObject){
        return this.userInfoService.userBindingGetVerificationCode(jsonObject.getString("mobilePhone"),jsonObject.getString("userName"));
    }



    /**
     * 获取用户首页的信息
     * @param request
     * @return
     */
    @ApiOperation(value="/tab/mine 获取用户首页的信息", notes="根据加密过后的APPKEY查询")
    @ApiImplicitParams({
            @ApiImplicitParam(defaultValue = "D9313E7909CEA4CD9DE7A026DD20517B",name = "APPKEY", value = "加密过后的userId", required = true, dataType = "String",paramType = "Header"),
    })
    @RequestMapping("/wx/index")
    @ResponseBody
    public JsonEntityUtil<LoginUserInfoDTO> getLoginUserInfo(HttpServletRequest request){
    return this.userInfoService.getIndexUserInfo(request.getHeader("APPKEY"));
    }


    @ApiOperation(value="/mine/persioninfo 个人资料更新头像的接口", notes="根据加密过后的APPKEY更新")
    @ApiImplicitParams({
            @ApiImplicitParam(defaultValue = "D9313E7909CEA4CD9DE7A026DD20517B",name = "APPKEY", value = "加密过后的userId", required = true, dataType = "String",paramType = "Header"),
    })
    @PostMapping("/wx/indexuploadphoto")
    @ResponseBody
    public JsonEntityUtil uploadPhoto(@Param("file")@ApiParam(value = "获取的图片",required = true) MultipartFile file, HttpServletRequest request){
        return this.userInfoService.uploadPhoto(request.getHeader("APPKEY"),file);
    }


    @ApiOperation(value="/mine/persioninfo 个人资料更新用户名的接口", notes="根据加密过后的APPKEY更新")
    @ApiImplicitParams({
            @ApiImplicitParam(defaultValue = "D9313E7909CEA4CD9DE7A026DD20517B",name = "APPKEY", value = "加密过后的userId", required = true, dataType = "String",paramType = "Header"),
    })
    @PostMapping("/wx/indexupdateusername")
    @ResponseBody
    public JsonEntityUtil updateUserName(@RequestBody @ApiParam(value = "获取用户名的实体",required = true) IndexUpdateUserNameEntity indexUpdateUserNameEntity, HttpServletRequest request){
        return this.userInfoService.updateUserName(request.getHeader("APPKEY"),indexUpdateUserNameEntity.getUserName());
    }

    @ApiOperation(value="/mine/changepsw 修改密码的接口", notes="根据加密过后的APPKEY更新")
    @ApiImplicitParams({
            @ApiImplicitParam(defaultValue = "D9313E7909CEA4CD9DE7A026DD20517B",name = "APPKEY", value = "加密过后的userId", required = true, dataType = "String",paramType = "Header"),
    })
    @PostMapping("/wx/indexupdatepassword")
    @ResponseBody
    public JsonEntityUtil updateUserName(@RequestBody @ApiParam(value = "接收密码的实体",required = true) UpadatePasswordRequestEntity upadatePasswordRequestEntity, HttpServletRequest request){
        return this.userInfoService.updatePassword(upadatePasswordRequestEntity.getNewPsw(),upadatePasswordRequestEntity.getOldPsw(),request.getHeader("APPKEY"));
    }



    @PostMapping("/wx/user/getinfo")
    @ResponseBody
    public JsonEntityUtil getuser(@RequestBody JSONObject jsonObject){
        return this.userInfoService.getuser(jsonObject.getString("userId"));
    }


}
