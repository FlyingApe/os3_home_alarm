package com.os3alarm.alarmconnector.controllers;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.os3alarm.shared.models.AlarmStatus;

/*
This class is responsible for dealing with the json that is send by each alarm
 */

public class JsonController {
    private JsonObject json = null;
    private String token = null;

    void parseJson(String jsonString) throws JsonException {
        this.json = (JsonObject) Jsoner.deserialize(jsonString);
        if(this.token == null){
            this.token = json.getString(Jsoner.mintJsonKey("token", new String()));
        }
    }

    String getToken(){
        return this.token;
    }

    String getJsonString(){
        return this.json.toJson();
    }

    AlarmStatus getStatus(){
        return AlarmStatus.valueOf(json.getString(Jsoner.mintJsonKey("status", new String())));
    }
}
