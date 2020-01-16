package com.os3alarm.server.relay;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.os3alarm.server.models.Alarm;
import com.os3alarm.server.models.AlarmStatus;
import com.os3alarm.server.relay.models.AlarmPool;
import com.os3alarm.server.relay.models.LiveAlarm;
import com.os3alarm.server.relay.models.RelayStream;
import com.os3alarm.server.services.MessagingService;

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

    private MessagingService _messagingService;


    public RelayInputStreamParser(RelayStream stream, MessagingService messagingService){
        this.stream = stream;
        this.relayReader = stream.getReader();
        this.pool = AlarmPool.getInstance();
        _messagingService = messagingService;
    }

    public void run(){
        try {
            String line;
            String jsonBuilder = "";
            /// TODO: Fix while condition;
            while (((line = relayReader.readLine()) != null)){
                jsonBuilder+=line;

                if(json(jsonBuilder)){
                    System.out.println("Server recieved: "+ json.toJson());

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
            pool.getAlarmByToken(token).setStatus(AlarmStatus.Disconnected);
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void updateAlarm(){
        Alarm alarm = new Alarm(token, json.getInteger(Jsoner.mintJsonKey("distance", new String())), json.getInteger(Jsoner.mintJsonKey("movement", new String())), json.getInteger(Jsoner.mintJsonKey("microphone", new String())), AlarmStatus.valueOf(json.getString(Jsoner.mintJsonKey("status", new String()))), json.getBoolean(Jsoner.mintJsonKey("alarmAudioOn", new String())));
        try{
            _messagingService.sendAlarmDataToSpecificUser(alarm);
        } catch (Exception e){

        }

        /// TODO: see if alarmPool and liveAlarm can be deleted
        LiveAlarm liveAlarm = pool.getAlarmByToken(token);

        liveAlarm.setStatus(AlarmStatus.valueOf(json.getString(Jsoner.mintJsonKey("status", new String()))));
        if(liveAlarm.getWriter() == null && token != null){
            liveAlarm.setWriter(stream.getWriter());
        }

        JsonObject sensors = new JsonObject();
        sensors.put("microphone", json.getString(Jsoner.mintJsonKey("microphone", new String())));
        sensors.put("distance", json.getString(Jsoner.mintJsonKey("distance", new String())));
        sensors.put("movement", json.getString(Jsoner.mintJsonKey("movement", new String())));

        System.out.println("token:  " + token);
        System.out.println("JsonSensors: " + sensors.toJson());
        System.out.println("status: " + liveAlarm.getStatus().toString());
        System.out.println("audioOn: " + json.getString(Jsoner.mintJsonKey("alarmAudioOn", new String())) + "\n");
    }
}
