package com.os3alarm.server.services;

import com.os3alarm.server.models.Alarm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
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
    public SimpMessagingTemplate _messagingTemplate;

    @Autowired
    public MessagingService(AlarmService alarmService, SessionRegistry sessionRegistry) {
        _alarmService = alarmService;
        _sessionRegistry = sessionRegistry;
    }

    public void sendAlarmDataToSpecificUser(@Payload Alarm alarm) {
        alarm = UpdateAlarmWithUsername(alarm);
        String sessionId = getSessionId("admin");

        System.out.println("Session id is: " + sessionId);

        _messagingTemplate.convertAndSendToUser(alarm.getUser(),"/sensordata", alarm,
                createHeaders(sessionId));

    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    @Async
    public Alarm UpdateAlarmWithUsername(Alarm alarm) {
        getActiveSessions();
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
            if (user.getUsername().equals(username)) {
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
        System.out.println(activeSessions);
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
