package com.os3alarm.server.models;

//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RelayDataObserver implements PropertyChangeListener {
    private String sensorData = "test";


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.sensorData = (String) (evt.getNewValue());
        System.out.println(this.sensorData);
    }

    public String getSensorData(){
        return sensorData;
    }


    /*
    //@MessageMapping("/alarm")
    //@SendTo("/alarm/sensordata")
    private String pushToSocket(String message) {
        return message;
    }
     */


}
