package com.os3alarm.alarmconnector.models;

import com.os3alarm.shared.interfaces.LiveAlarmListener;
import com.os3alarm.shared.models.AlarmStatus;
import com.os3alarm.datalogger.SimpleLogger;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

///TODO: investigate if/when locks and or synchronised methods should be used

public class LiveAlarm{
    private List<LiveAlarmListener> listeners = new ArrayList<>();

    private String token;
    private AlarmStatus status;
    private String allJsonData;

    private BufferedWriter writer = null;

    //constructor
    public LiveAlarm(String token) {
        this.token = token;
    }

    //getters
    public synchronized String getToken(){return this.token;}

    public synchronized String getAllJsonData(){return allJsonData;}

    public synchronized BufferedWriter getWriter() {return this.writer;}

    //setters
    public synchronized void update(AlarmStatus status, String jsonData){
        this.setStatus(status);
        this.allJsonData = jsonData;

        notifyListeners();
    }

    public synchronized void setStatus(AlarmStatus status){
        if(!status.equals(this.status)){
            SimpleLogger.write(token, status.toString());
        }

        this.status = status;
    }

    public synchronized void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    //deal with listeners
    public synchronized void addListener(LiveAlarmListener listener){
        listeners.add(listener);
        String json = "{\"alarmAudioOn\":\"unknown\",\"distance\":\"unknown\",\"microphone\":\"unknown\",\"movement\":\"unknown\",\"token\":\""+token+"\",\"status\":\""+status.toString()+"\"}";
        update(status, json);
        System.out.println("listener added" + listener.toString());
    }

    public synchronized void removeListener(LiveAlarmListener listener){
        if(listeners.contains(listener)){
            listeners.remove(listener);
            System.out.println("listener removed " + listener.toString());
        }
    }

    private synchronized void notifyListeners(){
        List<LiveAlarmListener> removableListeners = new ArrayList<>();

        for (LiveAlarmListener listener: listeners) {
            try{
                if (listener.isAlive()) {
                    listener.newDataRecieved();
                } else {
                    removableListeners.add(listener);
                    System.out.println("removed listener " + listener.toString());
                }
            } catch (Exception e){
                removableListeners.add(listener);
                System.out.println("removed " + listener.toString() + " through Exception ");
                //e.printStackTrace();
            }
        }

        listeners.removeAll(removableListeners);
    }
}
