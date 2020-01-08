package com.os3alarm.server.relay.models;

public interface IAlarmSensorObserver {
    void update(String jsonSensorData);
}
