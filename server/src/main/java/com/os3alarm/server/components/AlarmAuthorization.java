package com.os3alarm.server.components;

import com.os3alarm.server.models.Alarm;
import com.os3alarm.server.services.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Optional;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Authorization {
    private AlarmService alarmService;

    @Autowired
    public Authorization(AlarmService alarmService){
        this.alarmService = alarmService;

    }

    public boolean isAuthorized(String token){
        boolean isAuthorized = false;
        Alarm alarm = null;
        Optional<Alarm> optionalAlarm = alarmService.getAlarmByToken(token);

        if(optionalAlarm.isPresent()){
            System.out.println("Alarm "+token+" is present in DB");
            alarm = optionalAlarm.get();
        }

        String username = this.getEncodedAuthentication();

        if(username == null){
            System.out.println("username could not be found");
            return false;
        }

        if(alarm != null){
            isAuthorized = alarm.getUser().equals(username);
            return isAuthorized;
        }
        return false;

    }

    public String getUserName(){
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            username = authentication.getName();
        }

        return username;
    }

    public String getEncodedAuthentication(){
        String encodedString = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            encodedString = Base64.getEncoder().encodeToString((authentication.getName().concat(":").concat(authentication.getCredentials().toString())).getBytes());
        }
        System.out.println(encodedString);
        return encodedString;
    }
}
