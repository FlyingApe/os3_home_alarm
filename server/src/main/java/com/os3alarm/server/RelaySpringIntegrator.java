package com.os3alarm.server;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public interface RelaySpringIntegrator {
    @Async
    String getJsonString(String token);

    @Async
    void setSendableString(String s, String token);
}