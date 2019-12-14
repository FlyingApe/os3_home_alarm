package nl.bastiaansierd.mockArduino;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import nl.bastiaansierd.interfaces.BufferedReadWriter;
import nl.bastiaansierd.mockArduino.Streams.RelayConnection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.TimeUnit;


enum AlarmStatus {
    Active,
    Inactive,
    Inoperable,
    InAlarm,
    Disconnected
}

public class MockArduino implements Runnable{

    public void run() {
        try{
            final int SBAP_PORT = 5000;
            ServerSocket server = new ServerSocket(SBAP_PORT);
            System.out.println("Waiting for relay to connect...");
            while(true) {
                Socket socket = server.accept();
                System.out.println("ServerToArduinoCommunicationHandler connected.");
                BufferedReadWriter stream = new RelayConnection(socket);
                TestService testService = new TestService(stream);
                Thread t = new Thread(testService);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class TestService implements Runnable{
        private RelayConnection relayConnection;
        private String token;
        private JsonObject receivedJsonObject = null;
        private JsonObject sendableJsonObject;
        private AlarmStatus status;
        private boolean alarmAudioOn;

        public TestService(BufferedReadWriter s) {
            this.relayConnection = (RelayConnection) s;
            this.token = getRandomHexString(16);
            while(!relayConnection.isConnected()){
                relayConnection.connect();
            }
            this.status = AlarmStatus.Active;
            this.alarmAudioOn = true;
        }

        public void run() {
            BufferedReader relayReader = relayConnection.getReader();
            String receivedString = new String();
            int x = 1;
            while (true) {
                // read any incomming json
                String line;
                try {
                    if(relayReader.ready()){
                        if (!(line = relayReader.readLine()).equals("")) {
                            receivedString = receivedString.concat(line);
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
                    //e.printStackTrace();
                }

                // create a jsonObject to be send to the relay
                sendableJsonObject = new JsonObject();
                sendableJsonObject.put("token", token);
                sendableJsonObject.put("microphone", String.valueOf(x));
                sendableJsonObject.put("distance", String.valueOf(x));
                sendableJsonObject.put("movement", String.valueOf(x));
                sendableJsonObject.put("audioOn", String.valueOf(alarmAudioOn));
                sendableJsonObject.put("status", status.toString());


                BufferedWriter relayWriter = relayConnection.getWriter();

                try {
                    String json = Jsoner.serialize(sendableJsonObject);

                    relayWriter.write(json.concat("\n"));
                    relayWriter.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                x++;
            }
        }

        private String getRandomHexString(int numchars){
            Random r = new Random();
            StringBuffer sb = new StringBuffer();
            while(sb.length() < numchars){
                sb.append(Integer.toHexString(r.nextInt()));
            }

            return sb.toString().substring(0, numchars);
        }

        private boolean json(String checkable){
            try{
                JsonObject jsonTestObject = (JsonObject) Jsoner.deserialize(checkable);
                System.out.println("JSON test passed: " + checkable);
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
                        status = AlarmStatus.Inactive;
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
}

