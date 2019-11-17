package nl.bastiaansierd.mockArduino.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RelayStream implements nl.bastiaansierd.interfaces.DataStream {
    Socket relaySocket;
    private InputStream in = null;
    private OutputStream out = null;
    private Lock streamLock;

    public RelayStream(Socket s) {
        relaySocket = s;
        streamLock = new ReentrantLock();
    }

    @Override
    public void connect() {
        try {
            in = relaySocket.getInputStream();
            out = relaySocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean isConnected() {
        if (relaySocket.isConnected() && in != null){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public InputStream getInputStream() {
        streamLock.lock();
        try {
            return in;
        } finally {
            streamLock.unlock();
        }
    }

    @Override
    public OutputStream getOutputStream() {
        streamLock.lock();
        try {
            return out;
        } finally {
            streamLock.unlock();
        }
    }
}
