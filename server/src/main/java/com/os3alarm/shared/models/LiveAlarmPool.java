package com.os3alarm.shared.models;

import com.os3alarm.alarmconnector.models.LiveAlarm;
import com.os3alarm.shared.interfaces.LiveAlarmCreationListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//shared resource voor alle alarmconnector's om data in te pushen
public class LiveAlarmPool {
    private List<LiveAlarm> pool;
    private static LiveAlarmPool instance = null;
    private Lock poolLock;
    private List<LiveAlarmCreationListener> listeners = new ArrayList<>();

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

            //notify Spring listeners that a new alarm has connected
            for (LiveAlarmCreationListener listener: listeners){
                listener.newAlarmCreated(alarm.getToken());
            }
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

    public void addListener(LiveAlarmCreationListener listener){
        listeners.add(listener);
    }
}
