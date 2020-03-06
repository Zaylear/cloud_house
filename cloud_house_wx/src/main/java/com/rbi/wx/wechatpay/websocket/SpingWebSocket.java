package com.rbi.wx.wechatpay.websocket;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class SpingWebSocket extends TextWebSocketHandler{
    private WebSocketSession webSocketSession1;
    private WebSocketSession webSocketSession2;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (webSocketSession1==null){
            webSocketSession1=session;
        }else if (webSocketSession2==null){
            webSocketSession2=session;
        }
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

            if(session.equals(webSocketSession1)){
                sendMessage(webSocketSession2,message);
            }else {
                sendMessage(webSocketSession1,message);
            }
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        super.handlePongMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
    public void sendMessage(WebSocketSession session,TextMessage textMessage){
        try {
            session.sendMessage(textMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages();
    }
}
