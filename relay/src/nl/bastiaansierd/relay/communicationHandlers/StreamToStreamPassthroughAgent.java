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
    BufferedReader reader = null;

    public StreamToStreamPassthroughAgent(BufferedReadWriter source, BufferedReadWriter destination) {
        this.source = source;
        this.destination = destination;
    }

    public void run()
    {

        // keep reading the source
        while(true){
            String line;
            String jsonBuilder = new String();

            while(!source.isConnected() || reader==null){
                try {
                    reader = source.getReader();
                    System.out.println("Reader is null trying to reëstablish reader in 3 seconds");
                    TimeUnit.SECONDS.sleep(3);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }

            try {
                if(reader.ready()){
                    //System.out.println("reading from " + reader.toString());
                    if (!(line = reader.readLine()).equals("")) {
                        jsonBuilder+=line;
                        System.out.println(line);
                        if(isJson(jsonBuilder)){
                            writeToDestination(jsonBuilder);
                            jsonBuilder="";
                        }

                        if (line.trim().equals("}")) {
                            jsonBuilder="";
                        }
                    }
                }
            } catch (Exception e) {
                //TODO: serialConnection works different than sockets. Find a way to check if the Arduino is still working/connected
                if(!source.getName().equals("Arduino")){
                    //No working connection with source
                    //System.out.println("Connection with source " + source.getName() + " broken.");
                    //Reset connection
                    //source.clearReader();
                    //source.clearWriter();

                    //source.connect();
                    //reader = source.getReader();
                }
            }

            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
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
                //System.out.println("written to server: "+writeable.trim());
            }
        } catch (IOException e) {
            if(destination.getName().equals("Server")){
                //No working connection with source
                System.out.println("Connection with "+destination.getName()+" broken.");
                //Reset connection
                destination.clearWriter();
                try {
                    destination.getReader().close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                destination.clearReader();

                //Reconnect
                destination.connect();
                writer = destination.getWriter();
            }
        }
    }
}
