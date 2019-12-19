package com.os3alarm.server.relayconnection.pojo;

import com.os3alarm.server.models.AlarmStatus;
import com.os3alarm.server.models.IRelayDataObservable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedWriter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

///TODO: investigate if/when locks and or synchronised methods should be used

public class RelayAlarm implements IRelayDataObservable {
    private String token;
    private String JsonSensors;
    private AlarmStatus status;
    private boolean audioOn;
    private BufferedWriter writer = null;
    private Lock sensorLock;
    private Lock statusLock;
    private Lock audioLock;

    private PropertyChangeSupport support;

    //constructor
    public RelayAlarm(String token) {
        this.token = token;
        sensorLock = new ReentrantLock();
        statusLock = new ReentrantLock();
        audioLock = new ReentrantLock();

        support = new PropertyChangeSupport(this);
    }

    //getters
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

    //TODO: probably depricated, discuss if method can be removed. Currently used for printing to console in testing
    public String getJsonSensors() {
        sensorLock.lock();
        try{
            return JsonSensors;
        } finally {
            sensorLock.unlock();
        }
    }

    public synchronized BufferedWriter getWriter() {
            return writer;
    }

    public boolean isAudioOn() {
        audioLock.lock();
        try{
            return audioOn;
        } finally {
            audioLock.unlock();
        }
    }

    //setters
    public void setStatus(AlarmStatus status) {
        statusLock.lock();
        try{
            this.status = status;
        } finally {
            statusLock.unlock();
        }
    }

    public void setJsonSensors(String value) {
        sensorLock.lock();
        try{
            this.JsonSensors = value;
            support.firePropertyChange("received", this.JsonSensors, value);
        } finally {
            sensorLock.unlock();
        }
    }

    public synchronized void setWriter(BufferedWriter writer) {
            this.writer = writer;
    }

    public synchronized void clearWriter() {
            this.writer = null;
    }

    public void setAudioOn(boolean audioOn) {
        audioLock.lock();
        try{
            this.audioOn = audioOn;
        } finally {
            audioLock.unlock();
        }
    }

    //Integration into Spring
    @Override
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
}
