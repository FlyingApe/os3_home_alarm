package com.os3alarm.server.relayconnection;

import com.os3alarm.server.relayconnection.pojo.AlarmPool;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;

enum Commands {
    setStatusInactive,
    setStatusActive,
    setAudioOff,
    setAudioOn,
}

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SpringIntegrater {
    private AlarmPool pool;
    private RelaySocketListener connector;

    public SpringIntegrater() {
        connector = RelaySocketListener.getInstance();
        pool = AlarmPool.getInstance();
    }

    @Async
    public void pushCommand(String alarmToken, Commands command){
        String jsonCommand = "{\"command\":\""+command+"\"}\n";
        pushToAlarm(alarmToken, jsonCommand);
    }

    private synchronized void pushToAlarm(String token, String jsonCommand){
        BufferedWriter writer = pool.getAlarmByToken(token).getWriter();
        try {
            writer.write(jsonCommand);
            writer.flush();
        } catch (Exception e) {
            //write failed
            System.out.println("Write to alarm with token "+token+" failed.");
            System.out.println(e.getMessage());

            //commented because relayInputStreamParser is responsible for checking the connection. It recieves regular messages.
            //System.out.println("Write failed, connection set to disconnected");
            //pool.getAlarmByToken(token).clearWriter();
        }
    }
}
