package com.os3alarm.server.relay.models;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//shared resource voor alle relay's om data in te pushen
public class AlarmPool {
    private List<LiveAlarm> pool;
    private static AlarmPool instance = null;
    private Lock poolLock;

    private AlarmPool() {
        //TODO: research if lock is necsesarry here
        this.poolLock = new ReentrantLock();
        this.pool = new ArrayList<>();
    }

    public static AlarmPool getInstance(){
        /* singleton initialisatie */
        if(instance == null){
            instance = new AlarmPool();
        }
        return instance;
    }

    public void addAlarm(LiveAlarm alarm) {
        poolLock.lock();
        try{
            pool.add(alarm);
        } finally {
            poolLock.unlock();
        }
    }
    
    public LiveAlarm getAlarmByToken(String token){
        poolLock.lock();
        try{
            //TODO: probably quite slow for a large AlarmPool, something to think about
            for (LiveAlarm alarm: pool) {
                if(alarm.getToken().equals(token)){
                    return alarm;
                }
            }
        } finally {
            poolLock.unlock();
        }

        return null;
    }
}
