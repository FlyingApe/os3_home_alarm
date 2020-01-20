package nl.bastiaansierd.mockArduino;

import nl.bastiaansierd.relay.interfaces.BufferedReadWriter;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class RelayConnection implements BufferedReadWriter {
    private Socket relaySocket;
    private BufferedWriter destination = null;
    private BufferedReader source = null;
    //
    // private Lock streamLock;

    public RelayConnection(Socket s) {
        relaySocket = s;
        //streamLock = new ReentrantLock();
    }

    public void connect() {
        int timeOut = 3;
        while(!isConnected()){
            try {
                destination = new BufferedWriter(new OutputStreamWriter(relaySocket.getOutputStream()));
                source = new BufferedReader(new InputStreamReader(relaySocket.getInputStream()));
            } catch (IOException e) {
                try {
                    System.out.println("ArduinoGenerator could not connect to relay.\n "+e.getMessage()+"\nWill try again in " + timeOut + " seconds.");
                    TimeUnit.SECONDS.sleep(timeOut);
                } catch (InterruptedException ex) {
                    //ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean isConnected() {
        if (source != null && destination != null){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getName() {
        return "Relay";
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
