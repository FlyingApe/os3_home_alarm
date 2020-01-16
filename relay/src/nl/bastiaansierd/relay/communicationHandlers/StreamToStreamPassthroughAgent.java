package nl.bastiaansierd.relay.communicationHandlers;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import nl.bastiaansierd.relay.interfaces.BufferedReadWriter;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class StreamToStreamPassthroughAgent implements Runnable
{
    private BufferedReadWriter source;
    private BufferedReadWriter destination;

    public StreamToStreamPassthroughAgent(BufferedReadWriter source, BufferedReadWriter destination) {
        this.source = source;
        this.destination = destination;
    }

    public void run()
    {
        BufferedReader reader = source.getReader();

        // keep reading the source
        while(true){
            String line;
            String jsonBuilder = new String();
            try {
                if(reader.ready()){
                    while ((line = reader.readLine()) != null){
                        jsonBuilder+=line;
                        if(isJson(jsonBuilder)){
                            writeToDestination(jsonBuilder);
                            jsonBuilder="";
                        }

                        if (line.trim().equals("}")) {
                            jsonBuilder="";
                        }
                    }
                }
            } catch (IOException e) {
                //TODO: serialConnection works different than sockets. Find a way to check if the Arduino is still working/connected
                if(!source.getName().equals("Arduino")){
                    //No working connection with source
                    System.out.println("Connection with source " + source.getName() + " broken.");
                    //Reset connection
                    source.clearReader();
                    //Reconnect
                    source.connect();
                    reader = source.getReader();
                }
            }

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean isJson(String checkable){
        try{
            JsonObject jsonTestObject = (JsonObject) Jsoner.deserialize(checkable);
            //2System.out.println("JSON test passed: " + checkable);
            jsonTestObject.clear();
            return true;
        } catch (Exception e){
            //System.out.println("JSON test failed. " + e.getMessage() + "\n Tested string: " + checkable);
            return false;
        }
    }

    private void writeToDestination(String writeable){
        BufferedWriter writer = destination.getWriter();
        try {
            writer.write(writeable);
            writer.write("\n\r");
            writer.flush();

            if (source.getName().equals("ArduinoGenerator") || source.getName().equals("Arduino")){
                System.out.println("written to server: "+writeable.trim());
            }
        } catch (IOException e) {
            //System.out.println(e.getMessage());

            //Connection broken
            //System.out.println("Connection with destination " + destination.getName() + " broken.");
            //Reset connection
            //destination.clearWriter();
            //Reconnect
            //destination.connect();
        }
    }
}
