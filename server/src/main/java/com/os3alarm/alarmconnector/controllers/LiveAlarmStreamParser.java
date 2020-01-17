package com.os3alarm.relay.controllers;

import com.github.cliftonlabs.json_simple.JsonException;
import com.os3alarm.relay.controllers.JsonController;
import com.os3alarm.relay.controllers.LiveAlarmController;
import com.os3alarm.relay.models.LiveAlarm;
import com.os3alarm.server.models.AlarmStatus;
import com.os3alarm.relay.models.RelayStream;

import java.io.BufferedReader;
import java.io.IOException;

public class LiveAlarmStreamParser implements Runnable {
    private BufferedReader relayReader;

    private JsonController jsonController;
    private LiveAlarmController alarmController;


    public LiveAlarmStreamParser(RelayStream stream){
        this.relayReader = stream.getReader();

        this.jsonController = new JsonController();
        this.alarmController = new LiveAlarmController(this.jsonController, stream);
    }

    public void run(){
        try {
            String line;
            StringBuffer jsonBuilder = new StringBuffer(256);
            /// TODO: Fix while condition;
            while (((line = relayReader.readLine()) != null)){
                jsonBuilder.append(line);

                try {
                    jsonController.parseJson(jsonBuilder.toString());
                    System.out.println("Server recieved: "+ jsonController.getJsonString());

                    alarmController.update();
                    jsonBuilder = new StringBuffer(256);
                } catch (JsonException e){
                    //SimpleLogger.write("LiveAlarmStreamParser.run:jsonController", e.getMessage());
                }
            }
        } catch (IOException e) {
            alarmController.setToDisconnected();
        }
    }
}
