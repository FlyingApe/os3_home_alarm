package com.os3alarm.alarmconnector.controllers;

import com.os3alarm.alarmconnector.models.LiveAlarmPool;
import com.os3alarm.alarmconnector.models.LiveAlarm;
import com.os3alarm.alarmconnector.models.RelayStream;
import com.os3alarm.server.models.AlarmStatus;

public class LiveAlarmController {
    private LiveAlarmPool pool;
    private JsonController jsonController;
    private RelayStream stream;

    public LiveAlarmController(JsonController jsonController, RelayStream stream){
        this.pool = LiveAlarmPool.getInstance();
        this.stream = stream;
        this.jsonController = jsonController;
    }

    public void update(){
        if(this.pool.getAlarmByToken(this.jsonController.getToken()) == null ) {
            createAlarm(this.jsonController.getToken());
        }

        updateAlarm();
    }

    public void setToDisconnected(){
        //Read failed, set connection to disconnected
        System.out.println("Read failed, connection set to disconnected");

        pool.getAlarmByToken(jsonController.getToken()).setWriter(null);
        pool.getAlarmByToken(jsonController.getToken()).setStatus(AlarmStatus.Disconnected);
        stream.clearReader();

    }


    private void createAlarm(String token){
        LiveAlarm alarm = new LiveAlarm(token);
        this.pool.addAlarm(alarm);

        /*
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
                os.flush();
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
        */
    }

    private void updateAlarm(){
        LiveAlarm liveAlarm = pool.getAlarmByToken(jsonController.getToken());

        liveAlarm.setStatus(jsonController.getStatus());
        if(liveAlarm.getWriter() == null && jsonController.getToken() != null){
            liveAlarm.setWriter(stream.getWriter());
        }
    }
}
