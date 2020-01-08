package com.os3alarm.server.relay;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.os3alarm.server.models.AlarmStatus;
import com.os3alarm.server.models.RelayDataObserver;
import com.os3alarm.server.relay.models.AlarmPool;
import com.os3alarm.server.relay.models.LiveAlarm;
import com.os3alarm.server.relay.models.RelayStream;

import java.io.BufferedReader;
import java.io.IOException;

public class RelayInputStreamParser implements Runnable {
    private BufferedReader relayReader;
    private RelayStream stream;
    private JsonObject json = null;
    private AlarmPool pool;
    private String token = null;

    public RelayInputStreamParser(RelayStream stream){
        this.stream = stream;
        this.relayReader = stream.getReader();
        this.pool = AlarmPool.getInstance();
    }

    /// TODO: Fix while condition;
    public void run(){
        try {
                String line;
                String jsonBuilder = "";
                while (((line = relayReader.readLine()) != null)){
                    jsonBuilder+=line;

                    if(json(jsonBuilder)){
                        if(pool.getAlarmByToken(token) == null ) {
                            createAlarm(token);
                        }

                        updateAlarm();


                        //System.out.println("token: "+ token + " && JSON_recieved: " + jsonBuilder);
                        jsonBuilder="";
                    }
                }
        } catch (IOException e) {
            //Read failed, set connection to disconnected
            System.out.println("Read failed, connection set to disconnected");

            pool.getAlarmByToken(token).setWriter(null);
            stream.clearReader();
        }

    }

    private boolean json(String checkable){
        try{
            json = (JsonObject) Jsoner.deserialize(checkable);
            if(token == null){
                token = json.getString(Jsoner.mintJsonKey("token", new String()));
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }

    private void createAlarm(String token){
        /// TODO: create factory, replace with interface
        LiveAlarm alarm = new LiveAlarm(token);
        pool.addAlarm(alarm);
    }

    private void updateAlarm(){
        JsonObject sensors = new JsonObject();
        sensors.put("microphone", json.getString(Jsoner.mintJsonKey("microphone", new String())));
        sensors.put("distance", json.getString(Jsoner.mintJsonKey("distance", new String())));
        sensors.put("movement", json.getString(Jsoner.mintJsonKey("movement", new String())));

        LiveAlarm alarm = pool.getAlarmByToken(token);

        alarm.setJsonSensors(sensors.toJson());
        alarm.setStatus(AlarmStatus.valueOf(json.getString(Jsoner.mintJsonKey("status", new String()))));
        alarm.setAudioOn(json.getBoolean(Jsoner.mintJsonKey("alarmAudioOn", new String())));

        if(alarm.getWriter() == null && token != null){
            alarm.setWriter(stream.getWriter());
        }

        System.out.println("token:  " + token);
        System.out.println("JsonSensors: " +alarm.getJsonSensors());
        System.out.println("status: " + alarm.getStatus().toString());
        System.out.println("audioOn: " + alarm.isAudioOn() + "\n");
    }
}
