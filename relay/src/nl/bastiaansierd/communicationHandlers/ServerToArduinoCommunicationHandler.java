package nl.bastiaansierd.communicationHandlers;

import nl.bastiaansierd.streams.ArduinoStream;
import nl.bastiaansierd.interfaces.DataStream;
import nl.bastiaansierd.streams.ServerStream;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class ServerToArduinoCommunicationHandler implements Runnable
{
    private DataStream arduinoStream;
    private DataStream serverStream;
    private BufferedReader reader = null;

    public ServerToArduinoCommunicationHandler(ArduinoStream arduinoStream, ServerStream serverStream) {
        this.arduinoStream = arduinoStream;
        this.serverStream = serverStream;
    }

    public void run ()
    {
        while (true) {
            //read the serversInputStream
            String serverReply = "";
            try {
                if(serverStream.isConnected()){
                    serverReply = readServer(reader);
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

    private String readServer(BufferedReader bufferReader) throws IOException {
        String serverString = "";

        if(bufferReader.ready()){
            int ascii = bufferReader.read();
            char character = (char)ascii;
            serverString = String.valueOf(character);
        }

        return serverString;
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