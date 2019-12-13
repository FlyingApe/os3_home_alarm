package nl.bastiaansierd.mockArduino.Streams;

import nl.bastiaansierd.interfaces.BufferedReadWriter;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RelayConnection implements BufferedReadWriter {
    Socket relaySocket = null;
    private BufferedWriter destination = null;
    private BufferedReader source = null;
    private InputStream in = null;
    private OutputStream out = null;
    private Lock streamLock;

    public RelayConnection(Socket s) {
        relaySocket = s;
        streamLock = new ReentrantLock();
    }

    public void connect() {
        try {
            destination = new BufferedWriter(new OutputStreamWriter(relaySocket.getOutputStream()));
            source = new BufferedReader(new InputStreamReader(relaySocket.getInputStream()));

            //in = relaySocket.getInputStream();
            //out = relaySocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean isConnected() {
        if (destination != null){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public BufferedReader getReader() {
        return source;
    }

    @Override
    public BufferedWriter getWriter() {
        return destination;
    }
/*
    public InputStream getInputStream() {
        streamLock.lock();
        try {
            return in;
        } finally {
            streamLock.unlock();
        }
    }

    public OutputStream getOutputStream() {
        streamLock.lock();
        try {
            return out;
        } finally {
            streamLock.unlock();
        }
    }
    */
}
