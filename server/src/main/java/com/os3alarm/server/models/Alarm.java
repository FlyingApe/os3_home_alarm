package com.os3alarm.server.models;


import javax.persistence.*;

@Entity
public class Alarm {

    @Id
    /// TODO: Implement static id by input validation.
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String token;
    private String user;

    @Transient
    private int distance;
    @Transient
    private int movement;
    @Transient
    private int microphone;
    @Transient
    private AlarmStatus status;
    @Transient
    private boolean audioOn;



    public Alarm() {} //Allows REST POST

    public Alarm(String token, int distance, int movement, int microphone, AlarmStatus status, boolean audioON){
        this.token = token;
        this.distance = distance;
        this.movement = movement;
        this.microphone = microphone;
        this.status = status;
        this.audioOn = audioON;
    }

    public Alarm(String token) {
        this.token = token;
        this.user = "admin";
    }

    public AlarmStatus getStatus() {
        return status;
    }

    public int getDistance() {
        return distance;
    }

    public int getMovement() {
        return movement;
    }

    public int getMicrophone() {
        return microphone;
    }

    public boolean isAudioOn() {
        return audioOn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}