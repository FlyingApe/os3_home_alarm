package com.os3alarm.server.relay.models;

import com.os3alarm.server.models.AlarmStatus;
import datalogger.SimpleLogger;

import java.io.BufferedWriter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

///TODO: investigate if/when locks and or synchronised methods should be used

public class LiveAlarm{
//public class LiveAlarm{
    private String token;
    private String JsonSensors;
    private AlarmStatus status;
    private boolean audioOn;

    private BufferedWriter writer = null;

    private Lock sensorLock;
    private Lock statusLock;
    private Lock audioLock;


    //constructor
    public LiveAlarm(String token) {
        this.token = token;

        sensorLock = new ReentrantLock();
        statusLock = new ReentrantLock();
        audioLock = new ReentrantLock();
    }

    //getters and setters
    String getToken() {
        return token;
    }

    //TODO: add observer pattern implementation to status
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
                SimpleLogger.write(token, "Status changed to: ".concat(status.toString()));
            }
            this.status = status;
        } finally {
            statusLock.unlock();
        }
    }

    //TODO: probably depricated, discuss if method can be removed. Currently used for printing to console in testing
    public String getJsonSensors() {
        sensorLock.lock();
        try{
            return JsonSensors;
        } finally {
            sensorLock.unlock();
        }
    }

    public void setJsonSensors(String value) {
        sensorLock.lock();
        try{
            this.JsonSensors = value;
        } finally {
            sensorLock.unlock();
        }
    }

    public synchronized BufferedWriter getWriter() {
            return writer;
    }

    public synchronized void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public boolean isAudioOn() {
        audioLock.lock();
        try{
            return audioOn;
        } finally {
            audioLock.unlock();
        }
    }

    public void setAudioOn(boolean audioOn) {
        audioLock.lock();
        try{
            this.audioOn = audioOn;
        } finally {
            audioLock.unlock();
        }
    }
}