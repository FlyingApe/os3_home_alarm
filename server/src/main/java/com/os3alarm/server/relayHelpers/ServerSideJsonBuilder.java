package com.os3alarm.server.relayHelpers;

//import com.github.cliftonlabs.json_simple.JsonObject;
//import com.github.cliftonlabs.json_simple.Jsoner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerSideJsonBuilder {
    private static BufferedReader src;

    public ServerSideJsonBuilder(InputStream in){
        src=new BufferedReader(new InputStreamReader(in));
    }

    private String json = "";
    String jsonBuilder = "";

    public void build(){
        String line = null;
        try {
            if(src.ready()){
                while (((line = src.readLine()) != null)){
                    jsonBuilder+=line;
                    System.out.println(jsonBuilder);

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
            //JsonObject jsonTestObject = (JsonObject) Jsoner.deserialize(checkable);
            //System.out.println("JSON: " + checkable);
            //jsonTestObject.clear();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
