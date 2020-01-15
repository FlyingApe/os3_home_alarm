package com.os3alarm.server.relay.models;

import com.os3alarm.server.models.AlarmStatus;
import com.os3alarm.datalogger.SimpleLogger;

import java.io.BufferedWriter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

///TODO: investigate if/when locks and or synchronised methods should be used

public class LiveAlarm{
    private String token;
    private AlarmStatus status;

    private BufferedWriter writer = null;

    private Lock statusLock;


    //constructor
    public LiveAlarm(String token) {
        this.token = token;

        statusLock = new ReentrantLock();
    }

    //getters and setters
    String getToken() {
        return token;
    }

    public AlarmStatus getStatus() {
        statusLock.lock();
        try{
            return status;
        } finally {
            statusLock.unlock();
        }
    }

    public void setStatus(AlarmStatus status) {
        statusLock.lock();
        try{
            if(!status.equals(this.status)){
                SimpleLogger.write(token, status.toString());
            }
            this.status = status;
        } finally {
            statusLock.unlock();
        }
    }

    public synchronized BufferedWriter getWriter() {
            return writer;
    }

    public synchronized void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }
}
