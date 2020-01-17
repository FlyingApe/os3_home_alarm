package com.os3alarm.server.services;

import com.os3alarm.alarmconnector.models.LiveAlarm;
import com.os3alarm.server.components.Authorization;
import com.os3alarm.shared.interfaces.LiveAlarmCreationListener;
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

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RelayService{
    private LiveAlarmPool pool;
    private Authorization authorization;

    @Autowired
    public RelayService(Authorization authorization) {
        this.authorization = authorization;
        RelaySocketListener.initRelaySocketListener();
        this.pool = LiveAlarmPool.getInstance();
    }

    @Async
    public LiveAlarm getLiveAlarmByToken(String token){
        if(authorization.isAuthorized(token)){
            return this.pool.getAlarmByToken(token);
        } else {
            return null;
        }
    }

    @Async
    public void pushCommand(String token, Commands command){
        if(authorization.isAuthorized(token)) {
            String jsonCommand = "{\"command\":\"" + command + "\"}\n";

            BufferedWriter writer = this.pool.getAlarmByToken(token).getWriter();
            try {
                writer.write(jsonCommand);
                writer.flush();
            } catch (Exception e) {
                //write failed
                System.out.println("Write to alarm with token " + token + " failed.");
                System.out.println(e.getMessage());
            }
        }
    }

    @Async
    public void subscribeToAlarmData(String token, LiveAlarmListener listener){
        if(authorization.isAuthorized(token)) {
            pool.getAlarmByToken(token).addListener(listener);
        }
    }

    @Async
    public void subscribeToAlarmCreation(LiveAlarmCreationListener listener){
        pool.addListener(listener);
    }
}
