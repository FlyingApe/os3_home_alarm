package com.os3alarm.server.components;

import com.os3alarm.server.models.Alarm;
import com.os3alarm.server.services.AlarmService;
import com.os3alarm.server.services.RelayService;
import com.os3alarm.shared.interfaces.LiveAlarmCreationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AlarmCreator implements LiveAlarmCreationListener {
    private AlarmService alarmService;

    @Autowired
    public AlarmCreator(RelayService relayService, AlarmService alarmService) {
        relayService.subscribeToAlarmCreation(this);
        this.alarmService = alarmService;
    }

    @Async
    @Override
    public void newAlarmCreated(String token) {
        if(!alarmService.getAlarmByToken(token).isPresent()){
            Alarm alarm = new Alarm();
            alarm.setToken(token);
            alarmService.save(alarm);
        }
    }
}
