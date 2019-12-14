package nl.bastiaansierd.communicationHandlers;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class StreamToStreamPassthroughAgent implements Runnable
{
    private BufferedReader source;
    private BufferedWriter destination;

    public StreamToStreamPassthroughAgent(BufferedReader source, BufferedWriter destination) {
        this.source = source;
        this.destination = destination;
    }

    public void run()
    {
        // check if source is ready to be read. If not, wait and check again
        /*try {
            while (!source.ready()){
                TimeUnit.MILLISECONDS.sleep(50);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // keep reading the source
        while(true){
            String line;
            String jsonBuilder = new String();
            try {
                if(source.ready()){
                    while ((line = source.readLine()) != null){
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
                //e.printStackTrace();
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
            System.out.println("JSON test passed: " + checkable);
            jsonTestObject.clear();
            return true;
        } catch (Exception e){
            //System.out.println("JSON test failed. " + e.getMessage() + "\n Tested string: " + checkable);
            return false;
        }
    }

    private void writeToDestination(String writeable){
        try {
            destination.write(writeable);
            destination.write("\n\r");
            destination.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
