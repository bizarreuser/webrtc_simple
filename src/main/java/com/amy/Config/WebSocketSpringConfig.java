package com.amy.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketSpringConfig implements WebSocketConfigurer {

    final
    HandshakeInterceptor interceptor;

    final
    TextWebSocketHandler textHandler;

    final
    BinaryWebSocketHandler binaryWebSocketHandler;


    final WebSocketHandler myHandler;


    public WebSocketSpringConfig(TextWebSocketHandler textHandler, HandshakeInterceptor interceptor, BinaryWebSocketHandler binaryWebSocketHandler,
            WebSocketHandler myHandler) {
        this.textHandler = textHandler;
        this.interceptor = interceptor;
        this.binaryWebSocketHandler = binaryWebSocketHandler;
        this.myHandler = myHandler;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

//        对于同一路径，后add的handler会被覆盖，所以对同一路径，只部署一个handler; 不能用同一个handler部署add多次，因为底层为map的put，会导致之前相同的handler的路径不被处理
        registry
                .addHandler(myHandler, "/ws_test/*")//一个 * 表示一级,**表示后面可拼接多级,这里这样写表示必须为两级,每级可为空串
                .addInterceptors(interceptor)
                .setAllowedOrigins("*");
    }
}
