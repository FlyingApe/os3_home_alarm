package nl.bastiaansierd.relay.streams;

import nl.bastiaansierd.relay.interfaces.BufferedReadWriter;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ServerConnection implements BufferedReadWriter {
    private final String HOST = "localhost";
    private final int PORT = 3000;

    private Socket socket = null;
    private BufferedWriter outputWriter = null;
    private BufferedReader inputReader = null;

    public ServerConnection(){
        connect();
    }

    public synchronized void connect(){
        int timeOut = 3;
        while(!isConnected()) {
            try {
                System.out.println("Connecting to Server, HOST: " + HOST + ", PORT: " + PORT + "......");
                socket = new Socket(HOST, PORT);
                outputWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Succes, connected to server.");
            } catch (IOException e) {
                try {
                    System.out.println("Could not connect to server. Will try again in " + timeOut + " seconds");
                    TimeUnit.SECONDS.sleep(timeOut);
                } catch (InterruptedException ex) {
                    //ex.printStackTrace();
                }
            }
        }
    }

    public boolean isConnected(){
        if(inputReader != null && outputWriter != null){// not a great way of testing the connection
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getName() {
        return "Server";
    }

    @Override
    public BufferedReader getReader() {
        return inputReader;
    }

    @Override
    public BufferedWriter getWriter() {
        return outputWriter;
    }

    @Override
    public void clearReader() {
        inputReader = null;
    }

    @Override
    public void clearWriter() {
        outputWriter = null;
    }
}
