package com.os3alarm.server.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.net.Socket;

enum AlarmStatus {
    Active,
    Inactive,
    Inoperable,
    InAlarm,
    Disconnected
}

@Entity
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String name;
    private AlarmStatus status;
    // private List<Event> events;
    // private List<Sensor> sensors;
    private Socket socket;
    //private List<Event> eventLog;

    public Alarm(long id, String name, AlarmStatus status, /* List<Event> events, List<Sensor> sensors */ Socket socket) {
        this.id = id;
        this.name = name;
        this.status = status;
        /*
        this.events = events;
        this.sensors = sensors;
        */
        this.socket = socket;
    }

    public Alarm() {}

    public AlarmStatus getStatus() {
        return status;
    }

    public void setStatus(AlarmStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    /*
    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
     */

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /*
    public List<Event> getEventLog() {
        return eventLog;
    }
    */
}

class Sensor {
    public boolean sensorStatus;
    public SensorData sensorData;
}

interface SensorData {
    int GetData();
}

class MicrophoneData implements SensorData {
    public MicrophoneData(int decibel) {
        this.decibel = decibel;
    }

    private int decibel;

    public int GetData() {
        return decibel;
    }
}

class InfraredData implements SensorData {
    public InfraredData(int range) {
        this.range = range;
    }

    private int range;

    public int GetData() {
        return range;
    }
}

/// TODO: Implement event subtypes;
abstract class Event {
    public Event(String dateTime, Sensor sensor) {
        this.dateTime = dateTime;
        this.sensor = sensor;
    }

    private String dateTime;
    private Sensor sensor;

    public String getDateTime() {
        return dateTime;
    }

    public Sensor getSensor() {
        return sensor;
    }
}