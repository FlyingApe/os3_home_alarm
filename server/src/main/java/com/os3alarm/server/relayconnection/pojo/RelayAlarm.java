package com.os3alarm.server.relayconnection.pojo;

import com.os3alarm.server.models.IRelayDataObservable;
import com.os3alarm.server.models.RelayDataObservable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RelayAlarm implements IRelayDataObservable {
    private String token;
    private String receivedJsonString;
    private String sendableString;
    private Lock sendableLock;
    private Lock receivableLock;

    private PropertyChangeSupport support;

    public RelayAlarm(String token) {
        this.token = token;
        sendableLock = new ReentrantLock();
        receivableLock = new ReentrantLock();

        support = new PropertyChangeSupport(this);
    }

    @Override
    public void setReceivedJsonString(String value) {
        receivableLock.lock();
        try{
            this.receivedJsonString = value;
            support.firePropertyChange("received", this.receivedJsonString, value);
        } finally {
            receivableLock.unlock();
        }
    }

    public void setSendableString(String sendableString) {
        sendableLock.lock();
        try{
            this.sendableString = sendableString;
        } finally {
            sendableLock.unlock();
        }
    }

    public String getReceivedJsonString() {
        receivableLock.lock();
        try{
            return receivedJsonString;
        } finally {
            receivableLock.unlock();
        }
    }

    public String getSendableString(){
        sendableLock.lock();
        try{
            return this.sendableString;
        } finally {
            sendableLock.unlock();
        }
    }

    public String getToken() {
        return token;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
}
