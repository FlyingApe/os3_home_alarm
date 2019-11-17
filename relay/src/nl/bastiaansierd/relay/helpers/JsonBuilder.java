package nl.bastiaansierd.relay.helpers;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import org.w3c.dom.css.Counter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonBuilder {
    private static JsonBuilder instance = null;
    private static BufferedReader src;
    //private static InputStream inputStream = null;

    public static JsonBuilder getInstance(){
        if(instance == null){
            instance = new JsonBuilder();
        }
        return instance;
    }

    public static JsonBuilder getInstance(InputStream in){
        if(instance == null){
            instance = new JsonBuilder();
            //inputStream = in;
            src=new BufferedReader(new InputStreamReader(in));
        }
        return instance;
    }

    private String json = "";
    private String jsonBuilder = "";
    private boolean uglyHack = false;
    private String uglyHackLineNumber;

    public void build(boolean hack){
        this.uglyHack = hack;
        String line = null;
        try {
            while ((line = src.readLine()) != null){
                jsonBuilder+=line;
                if (line.trim().equals("}")) {
                    if(isJson(jsonBuilder)){
                        json = jsonBuilder;

                        //lelijke hack om de MockArduino aan de gang te krijgen, dit skipped elke keer één volwaardige jsonString
                        uglyHackLineNumber = String.valueOf(new Throwable().getStackTrace()[0].getLineNumber()+1);
                        if(uglyHack){
                            src.reset();
                        }
                    }
                    jsonBuilder="";
                }
            }

        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public String getJson(){
        return json;
    }

    private boolean isJson(String checkable){
        try{
            JsonObject jsonTestObject = (JsonObject) Jsoner.deserialize(checkable);
            System.out.println("JSON test passed: " + checkable);
            jsonTestObject.clear();
            return true;
        } catch (Exception e){
            String message = "";
            if(uglyHack){
                message = "Most likely failed because uglyHack was turned on by MockArduino. Hack can be found in " + this.getClass().getName() + " on line: " + uglyHackLineNumber;
            }
            System.out.println("JSON test failed. " + message);
            //e.printStackTrace();
            return false;
        }
    }
}
