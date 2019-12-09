package com.os3alarm.server.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private AlarmStatus status;
    private String name;
    private String token;
    //private List<Event> eventLog;

    public Alarm() {} //Allows REST POST

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

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    /*
    public List<Event> getEventLog() {
        return eventLog;
    }
    */


}