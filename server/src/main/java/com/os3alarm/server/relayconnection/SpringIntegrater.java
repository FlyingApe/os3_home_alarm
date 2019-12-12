package com.os3alarm.server.relayconnection;

import com.os3alarm.server.relayconnection.pojo.AlarmPool;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SpringIntegrater {
    private AlarmPool pool;
    private RelaySocketListener connector;

    public SpringIntegrater() {
        connector = RelaySocketListener.getInstance();
        pool = AlarmPool.getInstance();
    }

    public void setSendableString(String s, String token){
        pool.getAlarmByToken(token).setSendableString(s);
    }
}
