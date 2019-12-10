package com.os3alarm.server.relayconnection;

import com.os3alarm.server.relayconnection.pojo.AlarmPool;
import com.os3alarm.server.relayconnection.pojo.RelayAlarm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SpringIntegrater {
    private AlarmPool pool;
    private RelayConnector connector;

    public SpringIntegrater() {
        connector = RelayConnector.getInstance();
        pool = AlarmPool.getInstance();
    }

    @Async
    public String getJsonString(String token) {
        return pool.getAlarmByToken(token).getRecievedJsonString();
    }

    /*public RelayAlarm getAlarm(String token){
        return pool.getAlarmByToken(token);
    }*/

    @Async
    public void setSendableString(String s, String token){
        pool.getAlarmByToken(token).setSendableString(s);
    }
}
