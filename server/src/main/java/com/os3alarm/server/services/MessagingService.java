package com.os3alarm.server.services;

import com.os3alarm.server.models.Alarm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessagingService {

    private AlarmService _alarmService;
    @Qualifier("sessionRegistry")
    private SessionRegistry _sessionRegistry;

    @Autowired
    public MessagingService(AlarmService alarmService, SessionRegistry sessionRegistry) {
        _alarmService = alarmService;
        _sessionRegistry = sessionRegistry;
    }

    @Async
    public void sendToSpecificUser(Alarm alarm) {
        alarm = UpdateAlarmWithUsername(alarm);
        String sessionId = getSessionId(alarm.getUser());
        System.out.println(sessionId);

    }

    @Async
    public Alarm UpdateAlarmWithUsername(Alarm alarm) {
        Optional<Alarm> OptionalDatabaseAlarm = _alarmService.getAlarmByToken(alarm.getToken());
        if (OptionalDatabaseAlarm.isPresent()) {
            alarm.setUser(OptionalDatabaseAlarm.get().getUser());
        }
        return alarm;
    }

    public String getSessionId(String username) {
        String sessionId = "";
        List<SessionInformation> activeSessions = getActiveSessions();
        for (SessionInformation item : activeSessions) {
            User user = getUser(item);
            if (user.getUsername() == username) {
                sessionId = item.getSessionId();
            }
        }
        return sessionId;
    }


    public List<SessionInformation> getActiveSessions()
    {
        List<SessionInformation> activeSessions = new ArrayList<>();
        for ( Object principal : _sessionRegistry.getAllPrincipals() )
        {
            activeSessions.addAll( _sessionRegistry.getAllSessions( principal, false ) );
        }
        return activeSessions;
    }

    public User getUser( SessionInformation session )
    {
        Object principalObj = session.getPrincipal();
        if ( principalObj instanceof User )
        {
            User user = (User) principalObj;
            return user;
        }
        return null;
    }




}
