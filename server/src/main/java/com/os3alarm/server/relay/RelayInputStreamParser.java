package com.os3alarm.server.relay;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.os3alarm.server.models.Alarm;
import com.os3alarm.server.models.AlarmStatus;
import com.os3alarm.server.relay.models.AlarmPool;
import com.os3alarm.server.relay.models.LiveAlarm;
import com.os3alarm.server.relay.models.RelayStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RelayInputStreamParser implements Runnable {
    private RelayStream stream;
    private BufferedReader relayReader;
    private AlarmPool pool;

    private JsonObject json = null;
    private String token = null;

    public RelayInputStreamParser(RelayStream stream){
        this.stream = stream;
        this.relayReader = stream.getReader();
        this.pool = AlarmPool.getInstance();
    }

    public void run(){
        try {
            String line;
            String jsonBuilder = "";
            /// TODO: Fix while condition;
            while (((line = relayReader.readLine()) != null)){
                jsonBuilder+=line;

                if(json(jsonBuilder)){
                    if(pool.getAlarmByToken(token) == null ) {
                        createAlarm(token);
                    }

                    updateAlarm();
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

        URL url = null;
        HttpURLConnection con = null;
        try {
            url = new URL("http://localhost:8080/api/alarm/registerArduino");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");

            //String userCredentials = "admin:123";
            String basicAuth = "Basic YWRtaW46MTIz";
            con.setRequestProperty ("Authorization", basicAuth);

            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = "{\"token\": \"" + token + "\"}";

            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
                //os.flush();
            } catch (Exception e){
                //System.out.println(e.getMessage());
            }

            try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            } catch (Exception e){
                //System.out.println(e.getMessage());
            }

            System.out.println("tryed to create alarm");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void updateAlarm(){
        Alarm alarm = new Alarm(token, json.getInteger(Jsoner.mintJsonKey("distance", new String())), json.getInteger(Jsoner.mintJsonKey("movement", new String())), json.getInteger(Jsoner.mintJsonKey("microphone", new String())), AlarmStatus.valueOf(json.getString(Jsoner.mintJsonKey("status", new String()))), json.getBoolean(Jsoner.mintJsonKey("alarmAudioOn", new String())));


        /// TODO: depricated, only the writer is used
        LiveAlarm liveAlarm = pool.getAlarmByToken(token);
        JsonObject sensors = new JsonObject();

        sensors.put("microphone", json.getString(Jsoner.mintJsonKey("microphone", new String())));
        sensors.put("distance", json.getString(Jsoner.mintJsonKey("distance", new String())));
        sensors.put("movement", json.getString(Jsoner.mintJsonKey("movement", new String())));


        liveAlarm.setJsonSensors(sensors.toJson());
        liveAlarm.setStatus(AlarmStatus.valueOf(json.getString(Jsoner.mintJsonKey("status", new String()))));
        liveAlarm.setAudioOn(json.getBoolean(Jsoner.mintJsonKey("alarmAudioOn", new String())));

        if(liveAlarm.getWriter() == null && token != null){
            liveAlarm.setWriter(stream.getWriter());
        }


        System.out.println("token:  " + token);
        System.out.println("JsonSensors: " +liveAlarm.getJsonSensors());
        System.out.println("status: " + liveAlarm.getStatus().toString());
        System.out.println("audioOn: " + liveAlarm.isAudioOn() + "\n");


    }
}
