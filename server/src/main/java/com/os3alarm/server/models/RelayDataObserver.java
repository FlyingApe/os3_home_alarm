package com.os3alarm.server.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RelayDataObserver implements PropertyChangeListener, RelayDataReceiver {
    private String received;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.setReceived((String) (evt.getNewValue()));
    }

    @Override
    public String getReceived() {
        return received;
    }

    @Override
    public void setReceived(String received) {
        this.received = received;
    }
}
