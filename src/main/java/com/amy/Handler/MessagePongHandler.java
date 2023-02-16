package com.amy.Handler;

import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

//自定义一个只处理Pong的类
public class MessagePongHandler extends AbstractWebSocketHandler {

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
    }

}
