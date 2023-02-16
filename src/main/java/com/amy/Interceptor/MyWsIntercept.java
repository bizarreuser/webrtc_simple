package com.amy.Interceptor;

import static com.amy.Const.UserConst.ALLOW_IDS;
import static com.amy.Util.EnterUtil.handleUri;

import com.amy.Worker.Relay;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
public class MyWsIntercept implements HandshakeInterceptor {

    //    握手前
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) {
        String id = handleUri(Objects.requireNonNull(request.getURI()), 0);
        if (!ALLOW_IDS.contains(id)) {
            System.out.println("拒绝");
            return false;
        }
//    这里的  attributes 后面会放在Session中
        attributes.put("id", id);
        System.out.println("beforeHandshake");
        return true;
    }

    //    握手后
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            exception.printStackTrace();
        }
        System.out.println("afterHandshake");
    }
}

