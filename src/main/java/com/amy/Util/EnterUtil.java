package com.amy.Util;

import java.net.URI;
import java.util.Objects;
import org.springframework.web.socket.WebSocketSession;

public class EnterUtil {
    public static String handleUri(URI uri, int pathIndex) {
        if (pathIndex<0){
            System.out.println("？？？");
            return null;
        }
        System.out.println(uri.getPath());//这里会以 / 开头,所以split后第一位为空串
        String[] split = uri.getPath().split("/");
        if (split.length >= pathIndex + 3) {
            return split[pathIndex + 2];
        }
        return null;
    }

    public static String takeIdOf(WebSocketSession session) {
        if (Objects.isNull(session)) {
            throw new IllegalArgumentException("session is null!");
        }

        Object id = session.getAttributes().get("id");
        if (Objects.nonNull(id)) {
            return id.toString();
        } else {
            throw new RuntimeException("不可能");
        }
    }
}
