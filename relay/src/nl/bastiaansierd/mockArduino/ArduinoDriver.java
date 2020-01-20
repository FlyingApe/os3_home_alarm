package nl.bastiaansierd.mockArduino;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import nl.bastiaansierd.relay.interfaces.BufferedReadWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ArduinoDriver implements Runnable{
    private RelayConnection relayConnection;
    private String token;
    private JsonObject receivedJsonObject = null;
    private AlarmStatus status;
    private boolean alarmAudioOn;

    public ArduinoDriver(BufferedReadWriter s, String token) {
        this.relayConnection = (RelayConnection) s;
        this.token = token;
        System.out.println("Alarm created with token: " + this.token);
        while(!relayConnection.isConnected()){
            relayConnection.connect();
        }
        this.status = AlarmStatus.Active;
        this.alarmAudioOn = true;
    }

    public void run() {
        BufferedReader relayReader = relayConnection.getReader();
        BufferedWriter relayWriter = relayConnection.getWriter();
        System.out.println(relayReader.toString() + " :: " + relayWriter.toString());
        String receivedString = new String();
        int x = 1;
        while (true) {
            // read any incomming json
            String line;
            try {
                if(relayReader.ready()){
                    if (!(line = relayReader.readLine()).equals("")) {
                        receivedString = receivedString.concat(line);
                        System.out.println(receivedString);
                        if (json(receivedString)) {
                            ProcessCommand();

                            //System.out.println(receivedJsonObject.toJson());
                            receivedString = "";
                        }

                        if (line.trim().equals("}")) {
                            receivedString = "";
                        }
                    }
                }
            } catch (IOException e) {
                //System.out.println("server");
            }

            // create a jsonObject to be send to the relay

            JsonObject sendableJsonObject = new JsonObject();
            sendableJsonObject.put("token", token);
            sendableJsonObject.put("microphone", String.valueOf(x));
            sendableJsonObject.put("distance", String.valueOf(x));
            sendableJsonObject.put("movement", String.valueOf(x));
            sendableJsonObject.put("alarmAudioOn", String.valueOf(alarmAudioOn));
            sendableJsonObject.put("status", status.toString());



            try {
                String json = Jsoner.serialize(sendableJsonObject).concat("\n");

                relayWriter.write(json);
                relayWriter.flush();

                //System.out.println("Alarm with token "+token+" wrote to relay: " + json.trim());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            x++;
        }
    }

    private boolean json(String checkable){
        try{
            JsonObject jsonTestObject = (JsonObject) Jsoner.deserialize(checkable);
            System.out.println("received from server: " + checkable);
            receivedJsonObject = jsonTestObject;
            return true;
        } catch (Exception e){
            //System.out.println("JSON test failed. " + e.getMessage() + "\n Tested string: " + checkable);
            return false;
        }
    }

    public void ProcessCommand(){
        try{
            String command = receivedJsonObject.getString(Jsoner.mintJsonKey("command", new String()));
            switch (command){
                case "setStatusActive":
                    status = AlarmStatus.Active;
                    break;
                case "setStatusInactive":
                    status = AlarmStatus.InActive;
                    break;
                case "setAudioOn":
                    alarmAudioOn = true;
                    break;
                case "setAudioOff":
                    alarmAudioOn = false;
                    break;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
