package com.os3alarm.server;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.net.Socket;

enum status {
    Active,
    Inactive,
    Inoperable,
    Inalarm,
    Disconnected
        }



@Entity
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private status alarmStatus;
    //private List<Event> eventLog;

    public Alarm() {} //Allows REST POST

    public status getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(status alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /*
    public List<Event> getEventLog() {
        return eventLog;
    }
    */


}