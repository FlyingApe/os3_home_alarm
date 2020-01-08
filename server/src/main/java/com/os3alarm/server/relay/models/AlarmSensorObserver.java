package com.os3alarm.server.relay.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AlarmSensorObserver implements IAlarmSensorObserver {
    private String jsonSensorData;
    private PropertyChangeSupport support;

    public AlarmSensorObserver() {
        this.jsonSensorData = jsonSensorData;
        support = new PropertyChangeSupport(this);
    }

    @Override
    public void update(String value) {
        support.firePropertyChange("sensoren", this.jsonSensorData, value);
        this.jsonSensorData = value;
        //support.firePropertyChange("sensoren_".concat(token), this.JsonSensors, value);
    }

    //Spring Integration
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

}
