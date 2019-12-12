package nl.bastiaansierd.communicationHandlers;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import nl.bastiaansierd.interfaces.DataStream;
import nl.bastiaansierd.streams.ArduinoStream;
import nl.bastiaansierd.streams.ServerStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ArduinoToServerCommuncationHandler implements Runnable
{
    private DataStream serverStream;
    private DataStream arduinoStream;

    public ArduinoToServerCommuncationHandler(DataStream arduinoStream, DataStream serverStream) {
        this.arduinoStream = arduinoStream;
        this.serverStream = serverStream;
    }

    public void run()
    {
        BufferedReader reader = null;

        while (true) {
            if(!arduinoStream.isConnected()){
                arduinoStream.connect();
                try{
                    reader = new BufferedReader(new InputStreamReader(arduinoStream.getInputStream(), StandardCharsets.US_ASCII));
                } catch (Exception e){
                    System.out.println("No Connection.");
                }
            }
            String line = null;
            String jsonBuilder = null;
            try {
                while ((line = reader.readLine()) != null){
                    jsonBuilder+=line;
                    if (line.trim().equals("}")) {
                        if(isJson(jsonBuilder)){
                            writeToServer(jsonBuilder);
                            jsonBuilder="";
                        }
                    }
                }
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isJson(String checkable){
        try{
            JsonObject jsonTestObject = (JsonObject) Jsoner.deserialize(checkable);
            System.out.println("JSON test passed: " + checkable);
            jsonTestObject.clear();
            return true;
        } catch (Exception e){
            String message = "";
            System.out.println("JSON test failed. " + message);
            e.printStackTrace();
            return false;
        }
    }

    private void writeToServer(String writeable){
        PrintWriter writer = new PrintWriter(serverStream.getOutputStream());
        writer.println(writeable);
        writer.flush();
    }
}
