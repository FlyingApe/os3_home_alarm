package nl.bastiaansierd.streams;

import nl.bastiaansierd.interfaces.BufferedReadWriter;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class MockArduinoConnection implements BufferedReadWriter {
    private final String HOST = "localhost";
    private final int PORT = 5000;

    private static MockArduinoConnection instance = null;

    private Socket socket = null;
    private BufferedWriter destination = null;
    private BufferedReader source = null;


    public static MockArduinoConnection getInstance() {
        /* singelton initialisatie*/
        if (instance == null) {
            instance = new MockArduinoConnection();
        }
        return instance;
    }

    private MockArduinoConnection(){
        connect();
    }

    private void connect() {
        int timeOut = 3;
        while(!isConnected()) {
            try {
                System.out.println("Connecting to MockArduino, HOST: " + HOST + ", PORT: " + PORT + "......");
                socket = new Socket(HOST, PORT);
                source = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                destination = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                System.out.println("Succes, connected to MockArduino.");
            } catch (IOException e) {
                try {
                    System.out.println("Could not connect. Will try again in " + timeOut + " seconds");
                    TimeUnit.SECONDS.sleep(timeOut);
                } catch (InterruptedException ex) {
                    //ex.printStackTrace();
                }
                //e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isConnected() {
        if(socket != null){// Not the best way to test the connection
            return true;
        } else {
           return false;
        }
    }

    @Override
    public BufferedReader getReader(){
        return source;
    }

    @Override
    public BufferedWriter getWriter(){
        return destination;
    }
}