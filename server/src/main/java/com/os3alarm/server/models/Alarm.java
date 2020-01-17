package com.os3alarm.server.models;


import com.os3alarm.server.services.RelayService;
import com.os3alarm.shared.interfaces.LiveAlarmListener;
import com.os3alarm.shared.models.AlarmStatus;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
@Table(name="alarm")
public class Alarm{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String token;
    private String user;


    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getUser() {
        return user;
    }

    public void setToken(String token){
        this.token = token;
    }

    public void setUser(String user) {
        this.user = user;
    }
}