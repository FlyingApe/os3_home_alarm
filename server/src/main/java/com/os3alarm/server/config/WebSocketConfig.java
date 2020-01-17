package com.os3alarm.server.config;

import com.os3alarm.server.components.LiveAlarmRepresentation;
import com.os3alarm.server.services.RelayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private RelayService relayService;

    @Autowired
    public WebSocketConfig(RelayService relayService){
        this.relayService = relayService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(new LiveAlarmRepresentation(relayService), "/user");
    }
}