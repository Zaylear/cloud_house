package com.rbi.wx.wechatpay.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
@EnableWebMvc
public class WebSocket implements WebSocketConfigurer{
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
//这里的url要与页面的url一致
      // String[] allowsOrigins = {"http://www.xxx.com"};
      webSocketHandlerRegistry.addHandler(myHandler(),"/websocket.ws").addInterceptors(new WebSocketInterceptors());
    }
    @Bean
    public WebSocketHandler myHandler(){
        return new SpingWebSocket();
    }
}
