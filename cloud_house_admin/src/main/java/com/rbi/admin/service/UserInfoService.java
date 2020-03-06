package com.rbi.admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rbi.admin.dao.IdsDeleteDAO;
import com.rbi.admin.dao.UserInfoDAO;
import com.rbi.admin.dao.connect.ChargeDAO;
import com.rbi.admin.dao.connect.UserDAO;
import com.rbi.admin.dao.connect.WxUserInfoDAO;
import com.rbi.admin.dao.other.CheckExistDAO;
import com.rbi.admin.entity.dto.CheckExist.UsernameDTO;
import com.rbi.admin.entity.dto.User2DTO;
import com.rbi.admin.entity.dto.WxUserInfoDTO;
import com.rbi.admin.entity.edo.UserInfoDO;
import com.rbi.admin.util.DateUtil;
import com.rbi.admin.util.Md5Util;
import com.rbi.admin.util.PageData;
import com.rbi.admin.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoService {
    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired(required = false)
    WxUserInfoDAO wxUserInfoDAO;

    @Autowired(required = false)
    ChargeDAO chargeDAO;

    @Autowired(required = false)
    UserDAO userDAO;

    @Autowired(required = false)
    IdsDeleteDAO idsDeleteDAO;

    @Autowired(required = false)
    CheckExistDAO checkExistDAO;

    public String addUserInfo(UserInfoDO userInfoDO) throws Exception {
        if (null == userInfoDO.getDepartmentId() || null == userInfoDO.getUsername() ||
                null == userInfoDO.getRealName() || null == userInfoDO.getSex() ||
                null == userInfoDO.getMobilePhone()){
            return "部门、用户名、姓名、性别、电话不能为空";
        }
        if ("" == userInfoDO.getDepartmentId() || "" == userInfoDO.getUsername() ||
                "" == userInfoDO.getRealName() || "" == userInfoDO.getSex() ||
                "" == userInfoDO.getMobilePhone()){
            return "部门、用户名、姓名、性别、电话不能为空";
        }
        String userId = DateUtil.timeStamp()+ Tools.random(10,99);
        userInfoDO.setUserId(userId);
        userInfoDO.setEnabled(1);
        userInfoDO.setLoginStatus(0);
        userInfoDO.setIdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        String salt = Tools.uuid();
        userInfoDO.setSalt(salt);
        String mobilePhone = userInfoDO.getMobilePhone();
        String password = mobilePhone.substring(5,11);
        userInfoDO.setPassword(Md5Util.getMD5(password,salt));
        String username = userInfoDO.getUsername();
        List<UsernameDTO> usernameDTOS = checkExistDAO.findUsername(username);
        if (usernameDTOS.size() == 0){
            userInfoDAO.save(userInfoDO);
            return "10000";
        }else{
            return "用户名重复，请从新填写用户名";
        }
    }


    public String updateUserInfo(UserInfoDO userInfoDO) {
        if (null == userInfoDO.getDepartmentId() || null == userInfoDO.getUsername() ||
                null == userInfoDO.getRealName() || null == userInfoDO.getSex() ||
                null == userInfoDO.getMobilePhone()){
            return "部门、用户名、姓名、性别、电话不能为空";
        }
        if ("" == userInfoDO.getDepartmentId() || "" == userInfoDO.getUsername() ||
                "" == userInfoDO.getRealName() || "" == userInfoDO.getSex() ||
                "" == userInfoDO.getMobilePhone()){
            return "部门、用户名、姓名、性别、电话不能为空";
        }
        userInfoDO.setUdt(DateUtil.date(DateUtil.FORMAT_PATTERN));
        userInfoDAO.saveAndFlush(userInfoDO);
        return "10000";
    }

    public void updateTheme(String theme,String userId) {
        System.out.println("开始");
        userDAO.updateTheme(theme,userId);
    }

    public PageData findByPage(String organizationId,int pageNum, int pageSize){
        int pageNo = pageSize * (pageNum - 1);
        List<UserInfoDO> userInfoDOS  = userInfoDAO.findByPage(organizationId,pageNo,pageSize);
        int totalPage = 0;
        int count = userInfoDAO.findNum(organizationId);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum,pageSize,totalPage,count,userInfoDOS);
    }

    public void deleteByIds(JSONArray result) {
        String temp = "";
        List<String> list = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            JSONObject obj = (JSONObject) result.get(i);
            String id = obj.getString("id");
            temp = "'" + id + "'";
            list.add(temp);
        }
        String str = String.join(",", list);
        String ids = "("+str+")";
        idsDeleteDAO.deleteUserByIds(ids);
    }




    //外加接口
    public String findUserIdByPhone(WxUserInfoDTO wxUserInfoDTO) {

        String userId=this.wxUserInfoDAO.findUserByPhone(wxUserInfoDTO.getMobilePhone());
        if (userId==null||userId.equals("")){
            this.wxUserInfoDAO.insetiUser(wxUserInfoDTO);
            userId=this.wxUserInfoDAO.findUserByPhone(wxUserInfoDTO.getMobilePhone());
        }
        return userId;
    }

    //userId查询
    public UserInfoDO findByUserId(String userId) {
        return userInfoDAO.findByUserId(userId);
    }


    public User2DTO findMById(String userId) {
        return userDAO.findByUserId(userId);
    }


    public PageData findByName(String organizationId,String realName,int pageNum, int pageSize){
        int pageNo = pageSize * (pageNum - 1);
        String realName2 = "'%" + realName + "%'";
        List<UserInfoDO> userInfoDOS  = userDAO.findByName(organizationId,realName2,pageNo,pageSize);
        int totalPage = 0;
        int count = userDAO.findByNameNum(organizationId,realName2);
        if (0 == count % pageSize) {
            totalPage = count / pageSize;
        } else {
            totalPage = count / pageSize + 1;
        }
        return new PageData(pageNum,pageSize,totalPage,count,userInfoDOS);
    }

    public void resetPassword(String userId, String phone ,String salt) throws Exception{
        String password = phone.substring(5,11);
        String password2 =  Md5Util.getMD5(password,salt);
        userDAO.updatePassword(password2,userId);
    }

}
