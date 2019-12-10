package com.os3alarm.server.relayconnection;

import com.os3alarm.server.models.RelayDataObserver;
import com.os3alarm.server.relayconnection.pojo.AlarmPool;
import com.os3alarm.server.relayconnection.pojo.RelayAlarm;
import com.os3alarm.server.relayconnection.pojo.RelayStream;
import com.os3alarm.server.relayconnection.helpers.JsonParser;

import java.io.PrintWriter;

public class ConnectionService implements Runnable {
    private RelayStream relayStream;
    private AlarmPool pool;
    private JsonParser parser;

    public ConnectionService(RelayStream stream) {
        relayStream = stream;
        pool = AlarmPool.getInstance();

        parser = new JsonParser(relayStream.getInputStream());
    }

    public void run() {
        //TODO: Conditie om de loop te breken als de verbinding verbreekt |||| of proberen om de verbinding te herstellen
        while (relayStream.isConnected()) {
            System.out.println("looping");
            String token = updateAlarm();

            if(token != null){
                System.out.println("JSON: " + pool.getAlarmByToken(token).getReceivedJsonString());
            }

            /*
            String audioOn = "0";

            //let the jasonbuilder build strings with relayInput
            jsonBuilder.build();
            String json = jsonBuilder.getJson();

            //if there is a valid jsonString -> print it
            if(!json.equals("")){
                System.out.println("JSON: " + json);
            }

            pushToAlarm(audioOn);

            System.out.println("audioOn:" + audioOn);
            */
        }
    }

    private String updateAlarm(){
        parser.build();
        String json = parser.getJson();
        if(json != ""){
            System.out.println("update loop");
            String token = parser.getToken();
            if(pool.getAlarmByToken(parser.getToken()) == null ) {
                RelayAlarm alarm = new RelayAlarm(token);
                alarm.addPropertyChangeListener(new RelayDataObserver());
                pool.addAlarm(alarm);
            }
            RelayAlarm alarm = pool.getAlarmByToken(token);
            alarm.setReceivedJsonString(json);

            System.out.println(token);

            return token;
        }
        return null;
    }

    private void pushToAlarm(String reply){
        PrintWriter writer = new PrintWriter(relayStream.getOutputStream());
        writer.print(reply);
        writer.flush();
    }
}
