//package com.rbi.wx.wechatpay.configuration;
//
//import com.rbi.wx.wechatpay.requestentity.JsonUtil;
//import com.rbi.wx.wechatpay.redis.RedisUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Component
//public class WeChatFilter implements HandlerInterceptor {
//    @Autowired
//    private RedisUtil redisUtil;
//    @Override//在一个请求进入Controller层方法执行前执行这个方法
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("WeChatFilter调用---------------------------");
//        //在这里可以对参数做一些预处理和做一些验证
//        String header=request.getHeader("wx-requested-h5-request");
//        System.out.println(header);
//        if (redisUtil.hasKey(header)){
//            redisUtil.expire(header,600);
//        return true;
//        }//方法给予执行，就是允许controller的方法进行执行
//        response.getOutputStream().write(new JsonUtil("10003","非法访问").toString().getBytes("UTF-8"));
//        return false;
//    }
//    //返回model前
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        //Controller执行完毕后，返回之前，可以对request和reponse进行处理
//        //如果是前后端没有分离，在进入View层中前执行
//    }
//
//    //返回model后
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        //在一个请求处理完毕，即将销毁的时候，执行，可以做一些资源释放之类的工作
//    }
//}
