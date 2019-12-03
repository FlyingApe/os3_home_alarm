package com.os3alarm.server.relayHelpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RelayStream {
    Socket relaySocket = null;
    private InputStream in = null;
    private OutputStream out = null;

    public RelayStream(Socket s) {
        relaySocket = s;
        connect();
    }

    public void connect() {
        try {
            in = relaySocket.getInputStream();
            out = relaySocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        if (in != null && relaySocket.isConnected()){
            return true;
        } else {
            return false;
        }
    }

    public InputStream getInputStream() {
        return in;
    }

    public OutputStream getOutputStream() {
        return out;
    }
}
