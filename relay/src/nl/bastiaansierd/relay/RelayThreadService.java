package nl.bastiaansierd.relay;

import nl.bastiaansierd.relay.helpers.JsonBuilder;
import nl.bastiaansierd.relay.streams.ArduinoStream;
import nl.bastiaansierd.relay.streams.MockArduinoStream;
import nl.bastiaansierd.relay.streams.MockServerStream;
import nl.bastiaansierd.interfaces.DataStream;
import nl.bastiaansierd.relay.streams.ServerStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class RelayThreadService implements Runnable
{
    private DataStream arduinoStream;
    private DataStream serverStream;
    private BufferedReader reader = null;
    private boolean jsonBuilderHackForMockArduino = false;

    public RelayThreadService() {
        /* TODO: naar method */
        /*Mock componenten*/
        //serverStream = MockServerStream.getInstance();
        //arduinoStream = MockArduinoStream.getInstance();

        /*Fysieke componenten*/
        serverStream = ServerStream.getInstance();
        arduinoStream = ArduinoStream.getInstance();

        //activate superUglyMockArduinoHack
        if (arduinoStream.getClass().getName().endsWith("MockArduinoStream")){
            System.out.println("mockArduino = true");
            jsonBuilderHackForMockArduino = true;
        }
    }

    //TODO: 2 streams, Arduino -> Server, Server -> Arduino, scheiden naar 2 verschillende threads
    public void run ()
    {
        //JsonBuilder builds jsonStrings from te Arduino inputstream
        JsonBuilder jsonBuilder = JsonBuilder.getInstance(arduinoStream.getInputStream());

        while (true) {
            //System.out.println("looping");

            if(!serverStream.isConnected()){
                serverStream.connect();
                try{
                    reader = new BufferedReader(new InputStreamReader(serverStream.getInputStream(), StandardCharsets.US_ASCII));
                } catch (Exception e){
                    System.out.println("No Connection.");
                }
            }

            //let jsonBuilder read the arduino's inputstream and build a jsonString
            jsonBuilder.build(jsonBuilderHackForMockArduino);
            String json = jsonBuilder.getJson();
            //if there is a valid jsonString -> send it to the server
            if(!json.equals("")){
                if(serverStream.isConnected()){
                    writeToServer(json);
                }
            }

            //read the serversInputStream
            String serverReply = "";
            try {
                if(serverStream.isConnected()){
                    serverReply = readServer();
                    //System.out.println(serverReply);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //send serverReply to the arduino
            writeToArduino(serverReply);

            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //TODO: reader meegeven aan methode, uit globale scope halen
    private String readServer() throws IOException {
        String serverString = "";

        if(reader.ready()){
            int ascii = reader.read();
            char character = (char)ascii;
            serverString = String.valueOf(character);
        }

        return serverString;
    }


    private void writeToServer(String writeable){
        PrintWriter writer = new PrintWriter(serverStream.getOutputStream());
        writer.println(writeable);
        writer.flush();
    }

    private byte b = 0;
    private void writeToArduino(String serverInput){
        if(serverInput.equals("1")){
            b = 1;
        } else if(serverInput.equals("0")){
            b = 0;
        }

        try {
            arduinoStream.getOutputStream().write(b);
            arduinoStream.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}