package com.amy.Handler;

import static com.amy.Util.EnterUtil.takeIdOf;

import com.amy.Worker.Relay;
import java.net.URI;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

//我们自定义一个什么类型都处理的处理类,在这里，我们集合所有的Handler
@Component
public class MyHandler implements WebSocketHandler {

    final
    Relay relay;

    public MyHandler(Relay relay) {
        this.relay = relay;
    }

    /**
     * 建立连接
     *
     * @param session Session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        relay.saveSession(session);//必然是有id的
        relay.init();//告诉relay可以发送了
        System.out.println(session.getAttributes());
        System.out.println("afterConnectionEstablished");
    }


    /**
     * 接收消息
     *
     * @param session Session
     * @param message 消息
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println(session.getAttributes().get("id"));
        System.out.println(message.getPayload());
        System.out.println("handleMessage");
    }

    /**
     * 发生错误
     *
     * @param session Session
     * @param exception 异常
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String id = takeIdOf(session);

        if ("sender".equals(id)){

        }


        System.out.println(id);
        System.out.println("handleTransportError");
    }

    /**
     * 关闭连接
     *
     * @param session Session
     * @param closeStatus 关闭状态
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println(session.getAttributes().get("id"));
        ;
        System.out.println("afterConnectionClosed");
    }

    /**
     * 是否支持发送部分消息
     *
     * @return false
     */
    @Override
    public boolean supportsPartialMessages() {
        System.out.println("supportsPartialMessages");
        return false;
    }
}
