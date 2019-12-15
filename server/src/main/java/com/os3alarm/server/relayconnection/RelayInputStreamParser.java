package com.os3alarm.server.relayconnection;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.os3alarm.server.models.RelayDataObserver;
import com.os3alarm.server.relayconnection.pojo.AlarmPool;
import com.os3alarm.server.relayconnection.pojo.RelayAlarm;
import com.os3alarm.server.relayconnection.pojo.RelayStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RelayInputStreamParser implements Runnable {
    private BufferedReader source;
    private RelayStream stream;
    private JsonObject json = null;
    private AlarmPool pool;

    public RelayInputStreamParser(RelayStream stream){
        this.stream = stream;
        this.source = stream.getReader();
        this.pool = AlarmPool.getInstance();
    }

    /// TODO: Fix while condition;
    public void run(){
        String line;
        try {
                String jsonBuilder = "";
                while (((line = source.readLine()) != null)){
                    jsonBuilder+=line;

                    if(isJson(jsonBuilder)){
                        String token = getToken();
                        if(pool.getAlarmByToken(token) == null ) {
                            /// TODO: create factory, replace with interface
                            RelayAlarm alarm = new RelayAlarm(token);
                            alarm.addPropertyChangeListener(new RelayDataObserver());
                            pool.addAlarm(alarm);
                        }
                        RelayAlarm alarm = pool.getAlarmByToken(token);
                        alarm.setReceivedJsonString(jsonBuilder);
                        System.out.println("token: "+ token + " && JSON_recieved: " + jsonBuilder);
                        //System.out.println(stream.toString());
                        jsonBuilder="";
                    }
                }
        } catch (IOException e) {
            //Read failed, set connection to disconnected
            System.out.println("Read failed, connection set to disconnected");
            stream.clearReader();
        }

    }

    private boolean isJson(String checkable){
        try{
            json = (JsonObject) Jsoner.deserialize(checkable);
            return true;
        } catch (Exception e){
            //e.printStackTrace();
            return false;
        }
    }

    public String getToken(){
        try{
            String token = json.getString(Jsoner.mintJsonKey("token", new String()));
            return token;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
