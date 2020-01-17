package com.os3alarm.server.services;

import com.os3alarm.alarmconnector.models.LiveAlarmPool;
import com.os3alarm.server.models.Commands;
import com.os3alarm.alarmconnector.RelaySocketListener;
import com.os3alarm.alarmconnector.models.LiveAlarm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RelayService {
    private LiveAlarmPool pool;

    public RelayService() {
        RelaySocketListener.initRelaySocketListener();
        this.pool = LiveAlarmPool.getInstance();
    }

    @Async
    public void pushCommand(String token, Commands command){
        String jsonCommand = "{\"command\":\""+command+"\"}\n";

        BufferedWriter writer = pool.getAlarmByToken(token).getWriter();
        try {
            writer.write(jsonCommand);
            writer.flush();
        } catch (Exception e) {
            //write failed
            System.out.println("Write to alarm with token "+token+" failed.");
            System.out.println(e.getMessage());
        }
    }

    @Async
    public LiveAlarm getAlarmByToken(){
        return null;
    }
}
