package com.os3alarm.relay.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//shared resource voor alle relay's om data in te pushen
public class LiveAlarmPool {
    private List<LiveAlarm> pool;
    private static LiveAlarmPool instance = null;
    private Lock poolLock;

    private LiveAlarmPool() {
        //TODO: research if lock is necsesarry here
        this.poolLock = new ReentrantLock();
        this.pool = new ArrayList<>();
    }

    public static LiveAlarmPool getInstance(){
        /* singleton initialisatie */
        if(instance == null){
            instance = new LiveAlarmPool();
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
            //TODO: probably quite slow for a large LiveAlarmPool, something to think about
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
