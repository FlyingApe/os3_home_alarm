package com.os3alarm.server.relay.models;

public interface AlarmSensorObserver {
    void update(String jsonSensorData);
}
