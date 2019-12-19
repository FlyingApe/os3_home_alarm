package com.os3alarm.server.models;

import java.beans.PropertyChangeListener;

public interface IRelayDataObservable {
    public void addPropertyChangeListener(PropertyChangeListener pcl);
    public void removePropertyChangeListener(PropertyChangeListener pcl);
    public void setJsonSensors(String value);
}
