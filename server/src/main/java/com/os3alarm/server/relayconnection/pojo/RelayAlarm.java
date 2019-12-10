package com.os3alarm.server.relayconnection.pojo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RelayAlarm {
    private String token;
    private String recievedJsonString;
    private String sendableString;
    private Lock sendableLock;
    private Lock recievableLock;

    public RelayAlarm(String token) {
        this.token = token;
        sendableLock = new ReentrantLock();
        recievableLock = new ReentrantLock();
    }

    public void setRecievedJsonString(String recievedJsonString) {
        recievableLock.lock();
        try{
            this.recievedJsonString = recievedJsonString;
        } finally {
            recievableLock.unlock();
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

    public String getRecievedJsonString() {
        recievableLock.lock();
        try{
            return recievedJsonString;
        } finally {
            recievableLock.unlock();
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
}
