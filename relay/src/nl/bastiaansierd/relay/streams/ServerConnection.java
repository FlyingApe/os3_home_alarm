package nl.bastiaansierd.relay.streams;

import nl.bastiaansierd.relay.interfaces.BufferedReadWriter;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ServerConnection implements BufferedReadWriter {
    private final String HOST = "localhost";
    private final int PORT = 3000;

    private static ServerConnection instance = null;

    private Socket socket = null;
    private BufferedWriter destination = null;
    private BufferedReader source = null;

    public static ServerConnection getInstance() {
        /* singelton initialisatie*/
        if(instance == null){
            instance = new ServerConnection();
        }
        return instance;
    }


    private ServerConnection(){
        connect();
    }

    public synchronized void connect(){
        int timeOut = 3;
        while(!isConnected()) {
            try {
                System.out.println("Connecting to Server, HOST: " + HOST + ", PORT: " + PORT + "......");
                socket = new Socket(HOST, PORT);
                destination = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                source = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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

    public synchronized boolean isConnected(){
        if(source != null && destination != null){// not a great way of testing the connection
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
    public synchronized BufferedReader getReader() {
        return source;
    }

    @Override
    public synchronized BufferedWriter getWriter() {
        return destination;
    }

    @Override
    public synchronized void clearReader() {
        source = null;
    }

    @Override
    public synchronized void clearWriter() {
        destination = null;
    }
}
