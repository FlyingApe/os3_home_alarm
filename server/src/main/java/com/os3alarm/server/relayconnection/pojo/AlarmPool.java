package com.os3alarm.server.relayconnection.pojo;

import com.os3alarm.server.relayconnection.pojo.RelayAlarm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//shared resource voor alle relay's om data in te pushen
public class AlarmPool {
    private List<RelayAlarm> pool;
    private static AlarmPool instance = null;
    private Lock poolLock;

    private AlarmPool() {
        this.poolLock = new ReentrantLock();
        this.pool = new ArrayList<>();
    }

    public static AlarmPool getInstance(){
        /* singelton initialisatie*/
        if(instance == null){
            instance = new AlarmPool();
        }
        return instance;
    }

    public void addAlarm(RelayAlarm alarm) {
        poolLock.lock();
        try{
            pool.add(alarm);
        } finally {
            poolLock.unlock();
        }

    }
    
    public RelayAlarm getAlarmByToken(String token){
        poolLock.lock();
        try{
            for (RelayAlarm alarm: pool) {
                if(alarm.getToken() == token){
                    return alarm;
                }
            }
        } finally {
            poolLock.unlock();
        }

        return null;
    }
}
