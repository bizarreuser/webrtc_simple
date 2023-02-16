package com.amy.Handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class AllowTextOrPongHandler extends TextWebSocketHandler {////处理消息时，该父类对消息类型进行判断，遇到Binary消息，拒绝处理并断开连接

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(message);
        System.out.println(session.getAttributes());
        System.out.println("handleTextMessage");
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        System.out.println("pong");
    }

}

