package com.os3alarm.server.models;

import org.springframework.messaging.handler.annotation.SendTo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RelayDataObserver implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.pushToSocket((String) (evt.getNewValue()));
    }

    @SendTo("/alarm/sensordata")
    private String pushToSocket(String message) {
        return message;
    }
}
