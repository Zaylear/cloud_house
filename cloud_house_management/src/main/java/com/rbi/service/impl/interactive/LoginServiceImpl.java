package com.rbi.service.impl.interactive;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.rbi.dao.ILogLoginDAO;
import com.rbi.dao.IPermissionDAO;
import com.rbi.dao.LogLoginDAO;
import com.rbi.dao.UserInfoDAO;
import com.rbi.entity.LogLoginDO;
import com.rbi.entity.UserInfoDO;
import com.rbi.entity.dto.PermitDTO;
import com.rbi.service.interactive.LoginService;
import com.rbi.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    private final static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired(required = false)
    ILogLoginDAO iLogLoginDAO;

    @Autowired(required = false)
    IPermissionDAO iPermissionDAO;

    @Autowired
    LogLoginDAO logLoginDAO;

    @Override
    @Transactional
    public Map<String,Object> login(String username,String password,String module,String IP) throws NullPointerException {
        try {
            Map<String,Object> map = new HashMap<>();
            String token = "";
            UserInfoDO userInfo = userInfoDAO.findByUsername(username);
            if (null==userInfo){
                throw new NullPointerException();
            }
            password = Md5Util.getMD5(password,userInfo.getSalt());
            if (1==userInfo.getEnabled()){
                if (1==userInfo.getLoginStatus()){
                    LogLoginDO logLoginDO = iLogLoginDAO.findNewestLoginData(userInfo.getUserId());
                    if (null==logLoginDO||!IP.equals(logLoginDO.getIpAddress())){
                        logger.info("【登录实现类】账号异地登录，username={}",username);
                        map.put("status","1016");
                        return map;
                    }
                }
                if (password.equals(userInfo.getPassword())){
                    token = JwtToken.createToken(userInfo.getUserId());
                    userInfoDAO.updateLoginStatus(1,userInfo.getUserId());
                }else {
                    logger.info("【登录实现类】密码错误username={}",username);
                    map.put("status","1014");
                    return map;
                }
            }else {
                logger.info("【登录实现类】用户被禁用username={}",username);
                map.put("status","1013");
                return map;
            }

            List<PermitDTO> permitDTOS = iPermissionDAO.findPermit(module,userInfo.getUserId());
            if (0==permitDTOS.size()){
                userInfoDAO.updateLoginStatus(0,userInfo.getUserId());
                map.put("status","1015");
                return map;
            }

            /**
             * 登录日志
             */
            LogLoginDO logLoginDO = new LogLoginDO();
            logLoginDO.setUserId(userInfo.getUserId());
            logLoginDO.setUsername(username);
            logLoginDO.setMobilePhone(userInfo.getMobilePhone());
            logLoginDO.setOrganizationId(userInfo.getOrganizationId());
            logLoginDO.setRealName(userInfo.getRealName());
            String date = DateUtil.date(DateUtil.FORMAT_PATTERN);
            logLoginDO.setIdt(date);
            logLoginDO.setUdt(DateUtil.timeStamp());
            logLoginDO.setIpAddress(IP);
            logLoginDO.setEvent("登录");
            logLoginDAO.save(logLoginDO);

            map.put("status","1010");
            map.put("token",token);
            map.put("permitDTOS",permitDTOS);
            map.put("userInfo",userInfo);
            return map;
        } catch (Exception e) {
            logger.error("【登录实现类】用户名不存在！username={}，ERROR:{}",username,e);
            throw new NullPointerException();
        }
    }

    @Override
    public void logout(String userId,String ipAdress) {
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        LogLoginDO logLoginDO = new LogLoginDO();
        logLoginDO.setUserId(userInfoDO.getUserId());
        logLoginDO.setUsername(userInfoDO.getUsername());
        logLoginDO.setMobilePhone(userInfoDO.getMobilePhone());
        logLoginDO.setOrganizationId(userInfoDO.getOrganizationId());
        logLoginDO.setRealName(userInfoDO.getRealName());
        String date = DateUtil.date(DateUtil.FORMAT_PATTERN);
        logLoginDO.setIdt(date);
        logLoginDO.setUdt(DateUtil.timeStamp());
        logLoginDO.setIpAddress(ipAdress);
        logLoginDO.setEvent("退出");
        logLoginDAO.save(logLoginDO);
        userInfoDAO.updateLoginStatus(0,userInfoDO.getUserId());
    }

    @Override
    public Map<String, Object> loginAdmin(String username, String password, String module) throws Exception {
        try {
            Map<String,Object> map = new HashMap<>();
            String token = "";
            UserInfoDO userInfo = userInfoDAO.findByUsername(username);
            password = Md5Util.getMD5(password,userInfo.getSalt());
            if (1==userInfo.getEnabled()){
                if (password.equals(userInfo.getPassword())){
                    token = JwtToken.createToken(userInfo.getUserId());
                    userInfoDAO.updateLoginStatus(1,userInfo.getUserId());
                }else {
                    logger.info("【登录实现类】密码错误username={}",username);
                    map.put("status","1014");
                    return map;
                }
            }else {
                logger.info("【登录实现类】用户被禁用username={}",username);
                map.put("status","1013");
                return map;
            }

            List<PermitDTO> permitDTOS = iPermissionDAO.findAdminPermit(module,userInfo.getUserId());
            if (0==permitDTOS.size()){
                map.put("status","1015");
                return map;
            }
            map.put("status","1010");
            map.put("token",token);
//            map.put("permitDTOS",permitDTOS);
            return map;
        } catch (Exception e) {
            logger.error("【登录实现类】用户名不存在！username={}",username);
            throw new NullPointerException();
        }
    }

    @Override
    public void automaticLogOff() {
        List<LogLoginDO> logLoginDOS = iLogLoginDAO.findAllOrderByIDGroupByUserId();
        long timeStamp = DateUtil.timeStamp();
        List<UserInfoDO> userInfoDOS = userInfoDAO.findAllByLoginStatus(1);
        for (LogLoginDO logLoginDO : logLoginDOS) {
            for (UserInfoDO userInfoDO : userInfoDOS) {
                if (userInfoDO.getUserId().equals(logLoginDO.getUserId())){
                    if ((timeStamp-logLoginDO.getUdt())>10*60*1000l){
                        userInfoDAO.updateLoginStatus(0,logLoginDO.getUserId());
                    }
                }
            }
        }
    }

    @Override
    public PageData<LogLoginDO> findLoginLogByOrganizationId(JSONObject jsonObject, String userId) {
        int pageNo = jsonObject.getInteger("pageNo");
        int pageSize = jsonObject.getInteger("pageSize");
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        String username = jsonObject.getString("username");
        String realName = jsonObject.getString("realName");
        UserInfoDO userInfoDO = userInfoDAO.findByUserId(userId);
        Pageable pageable = new PageRequest(pageNo - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Page<LogLoginDO> page = logLoginDAO.findAll(new Specification<LogLoginDO>() {
            @Override
            public Predicate toPredicate(Root<LogLoginDO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = Lists.newArrayList();
                predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("organizationId"), userInfoDO.getOrganizationId())));
                if (StringUtils.isNotBlank(username)){
                    predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("username"),username)));
                }
                if (StringUtils.isNotBlank(realName)){
                    predicateList.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("realName"),realName)));
                }

                //起始日期
                if (startTime != null && !startTime.trim().equals("")) {
                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("idt").as(String.class), startTime));
                }
                //结束日期
                if (endTime != null && !endTime.trim().equals("")) {
                    predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("idt").as(String.class), endTime));
                }
                return criteriaQuery.where(criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]))).getRestriction();
            }
        },pageable);
        return new  PageData(page);
    }

}
