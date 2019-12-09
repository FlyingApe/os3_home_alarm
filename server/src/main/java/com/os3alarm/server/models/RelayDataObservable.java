package com.os3alarm.server.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class RelayDataObservable {
    private String received;
    private PropertyChangeSupport support;

    public RelayDataObservable() {
        support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void setReceived(String value) {
        support.firePropertyChange("received", this.received, value);
    }
}