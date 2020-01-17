package com.os3alarm.server.components;

import com.os3alarm.server.models.Alarm;
import com.os3alarm.server.services.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Authorization {
    AlarmService alarmService;

    @Autowired
    public Authorization(AlarmService alarmService){
        this.alarmService = alarmService;
    }

    public boolean isAuthorized(String token){
        Alarm alarm = alarmService.getAlarmByToken(token).get();
        String username = this.getUserName();

        if(username == null){
            return false;
        }

        return (alarm.getUser().equals(username));
    }

    public String getUserName(){
        String username = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) username = authentication.getName();

        return username;
    }
}
