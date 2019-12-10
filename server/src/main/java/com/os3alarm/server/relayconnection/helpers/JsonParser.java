package com.os3alarm.server.relayconnection.helpers;

import com.github.cliftonlabs.json_simple.*;
import com.os3alarm.server.relayconnection.pojo.RelayAlarm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonParser {
    private static BufferedReader src;
    private String json = "";
    String jsonBuilder = "";

    public JsonParser(InputStream in){
        src=new BufferedReader(new InputStreamReader(in));
    }


    public void build(){
        String line = null;
        try {
            if(src.ready()){
                while (((line = src.readLine()) != null)){
                    jsonBuilder+=line;

                    if(isJson(jsonBuilder)){
                        json = jsonBuilder;
                    }
                    jsonBuilder="";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getJson(){
        return json;
    }

    private boolean isJson(String checkable){
        try{
            JsonObject jsonTestObject = (JsonObject) Jsoner.deserialize(checkable);
            jsonTestObject.clear();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getToken(){
        try{
            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(json);
            return jsonObject.getStringOrDefault(Jsoner.mintJsonKey("token", new Object()));
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
