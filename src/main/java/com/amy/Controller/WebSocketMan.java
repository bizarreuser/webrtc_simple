package com.amy.Controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@ServerEndpoint("/ws_test/{id}")
@Component
public class WebSocketMan {

    private Session session;

    private static final List<String> okIds = Arrays.asList("sender", "receiver");

    private static final Map<WebSocketMan, String> SessionMap = new ConcurrentHashMap<>();


    @OnOpen
    public synchronized void onOpen(Session session, @PathParam("id") String accessId) {
        this.session = session;

        if (okIds.contains(accessId)) {
            SessionMap.put(this, accessId);
            log.info("{}进入房间,会话为{},this为{}", accessId, session, this);

        } else {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnMessage
    public synchronized void onMessage(Session session, String msg) {
        log.info("{}发来的消息: {},会话为{}", SessionMap.get(this), msg, session);
        if ("receiver".equals(SessionMap.get(this))) {
            sendToExpect(msg, "sender");
        } else if ("sender".equals(SessionMap.get(this))) {
            sendToExpect(msg, "receiver");
        }

    }

    //    异常的话会调用异常和关闭两个回调
    @OnClose
    public synchronized void Onclose(Session session) {
        log.info("会话为{} 关闭,id为 {}", session, SessionMap.get(this));
        SessionMap.remove(this);
    }

    @OnError
    public synchronized void OnError(Session session, Throwable e) {
        log.info("会话为{} 异常: {}", session, e);
        SessionMap.remove(this);
    }

    public static void sendToExpect(String msg, String... ids) {

        if (Objects.nonNull(msg)) {
            SessionMap.forEach(((webSocketMan, id) -> {
                if (ArrayUtils.contains(ids, id)) {
                    try {
                        log.info("发送{}给{}", msg, ids);
                        webSocketMan.session.getBasicRemote().sendText(msg);

                    } catch (Exception e) {
                        log.info("转发的消息失败:{}", msg);
                        e.printStackTrace();
                    }
                }
            }));
        } else {
            log.info("发到{}消息为空,不发", (Object[]) ids);
        }

    }

    public static void sendToAllExcept(String msg, String... ids) {
        SessionMap.forEach(((webSocketMan, s) -> {
            if (!ArrayUtils.contains(ids, s)) {
                try {
                    webSocketMan.session.getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    System.out.println("神奇");
                    e.printStackTrace();
                }
            }
        }));
    }


}
