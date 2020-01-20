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
public class AlarmAuthorization {
    private AlarmService alarmService;
    private String authToken;

    @Autowired
    public AlarmAuthorization(AlarmService alarmService){
        this.alarmService = alarmService;

    }

    public boolean isAuthorized(String token, String authToken){
        this.authToken = authToken;

        return isAuthorized(token);
    }

    public synchronized boolean isAuthorized(String token){
        boolean isAuthorized = false;
        Alarm alarm = null;
        Optional<Alarm> optionalAlarm = alarmService.getAlarmByToken(token);

        if(optionalAlarm.isPresent()){
            alarm = optionalAlarm.get();
        }

        String encodedAuthenticationString = this.getEncodedAuthentication();

        if(encodedAuthenticationString == null){
            System.out.println("Authentication token could not be found");
            return false;
        }

        if(alarm != null){
            isAuthorized = alarm.getAuthenticationToken().equals(encodedAuthenticationString);
            return isAuthorized;
        }
        return false;

    }

    public String getEncodedAuthentication(){
        String encodedString = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            encodedString = Base64.getEncoder().encodeToString((authentication.getName().concat(":").concat(authentication.getCredentials().toString())).getBytes());
        } else if(authToken != null){
            encodedString = authToken;
        }
        //System.out.println(encodedString);
        return encodedString;
    }
}
