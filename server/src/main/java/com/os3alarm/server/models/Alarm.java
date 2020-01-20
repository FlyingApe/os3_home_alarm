package com.os3alarm.server.models;


import javax.persistence.*;

@Entity
@Table(name="alarm")
public class Alarm{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String token;
    private String authtoken;


    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getAuthenticationToken() {
        return authtoken;
    }

    public void setToken(String token){
        this.token = token;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
}