package nl.bastiaansierd.relay.streams;

import nl.bastiaansierd.relay.interfaces.BufferedReadWriter;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class MockArduinoConnection implements BufferedReadWriter {
    private final String HOST = "localhost";
    private final int PORT = 5000;

    //private static MockArduinoConnection instance = null;

    private Socket socket = null;
    private BufferedWriter outputWriter = null;
    private BufferedReader inputReader = null;

    public MockArduinoConnection(){
        connect();
    }

    public synchronized void connect() {
        int timeOut = 3;
        while(!isConnected()) {
            try {
                System.out.println("Connecting to ArduinoGenerator, HOST: " + HOST + ", PORT: " + PORT + "......");
                socket = new Socket(HOST, PORT);
                inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outputWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                System.out.println("Succes, connected to ArduinoGenerator.");
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
    public synchronized boolean isConnected() {
        if(inputReader != null && outputWriter != null){// Not the best way to test the connection
            return true;
        } else {
           return false;
        }
    }

    @Override
    public String getName() {
        return "ArduinoGenerator";
    }

    @Override
    public synchronized BufferedReader getReader(){
        return inputReader;
    }

    @Override
    public synchronized BufferedWriter getWriter(){
        return outputWriter;
    }

    @Override
    public synchronized void clearReader() {
        inputReader = null;
    }

    @Override
    public synchronized void clearWriter() {
        outputWriter = null;
    }
}