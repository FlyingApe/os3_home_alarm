package com.os3alarm.server.services;

import com.os3alarm.alarmconnector.models.LiveAlarm;
import com.os3alarm.server.components.AlarmAuthorization;
import com.os3alarm.server.models.Alarm;
import com.os3alarm.shared.interfaces.LiveAlarmCreationListener;
import com.os3alarm.shared.models.AlarmStatus;
import com.os3alarm.shared.models.LiveAlarmPool;
import com.os3alarm.shared.interfaces.LiveAlarmListener;
import com.os3alarm.shared.models.Commands;
import com.os3alarm.alarmconnector.RelaySocketListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RelayService{
    private LiveAlarmPool pool;
    private AlarmAuthorization alarmAuthorization;

    @Autowired
    public RelayService(AlarmAuthorization alarmAuthorization, AlarmService alarmService) {
        this.alarmAuthorization = alarmAuthorization;
        RelaySocketListener.initRelaySocketListener();
        this.pool = LiveAlarmPool.getInstance();

        //populate pool with all database stored alarms
        List<Alarm> allAlarms = alarmService.findAll();
        for (Alarm alarm: allAlarms){
            LiveAlarm newLiveAlarm = new LiveAlarm(alarm.getToken());
            newLiveAlarm.setStatus(AlarmStatus.Disconnected);
            pool.addAlarm(newLiveAlarm);
        }
    }

    @Async
    public LiveAlarm getLiveAlarmByToken(String token, String authToken){
        if(alarmAuthorization.isAuthorized(token, authToken)){
            return this.pool.getAlarmByToken(token);
        } else {
            return null;
        }
    }

    @Async
    public void pushCommand(String token, Commands command){
        if(alarmAuthorization.isAuthorized(token)) {
            String jsonCommand = "{\"command\" : \"" + command.toString() + "\"}\n";

            BufferedWriter writer = this.pool.getAlarmByToken(token).getWriter();
            try {
                writer.write(jsonCommand);
                writer.flush();
                System.out.println("Command " + jsonCommand + " send to " + token + " with writer " + writer.toString());
            } catch (Exception e) {
                //write failed
                System.out.println("Write to alarm with token " + token + " failed.");

                System.out.println(e.getMessage());
            }
        }
    }

    public void subscribeToAlarmData(String token, String authToken, LiveAlarmListener listener){
        if(alarmAuthorization.isAuthorized(token, authToken)) {
            pool.getAlarmByToken(token).addListener(listener);
        }
    }

    public void unscribeToAlarmData(String token, LiveAlarmListener listener){
        pool.getAlarmByToken(token).removeListener(listener);
    }

    public void subscribeToAlarmCreation(LiveAlarmCreationListener listener){
        pool.addListener(listener);
    }
}
