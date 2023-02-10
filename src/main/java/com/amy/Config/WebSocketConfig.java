package com.amy.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
//import org.yeauty.standard.ServerEndpointExporter;


//必要的配置,websocket自身就解决了黏包半包问题(自定义消息帧，通过开始和结束符实现)
@Configuration
public class WebSocketConfig{
//    用于扫描WebSocket相关的注解
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
