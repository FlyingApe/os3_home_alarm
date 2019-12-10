package com.os3alarm.server.relayconnection.pojo;

import java.net.Socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RelayStream {
    Socket s = null;
    private InputStream in = null;
    private OutputStream out = null;

    public RelayStream(Socket s) {
        this.s = s;
        connect();
    }

    public void connect() {
       try {
            in = s.getInputStream();
            out = s.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return s.isConnected();
    }

    public InputStream getInputStream() {
        return in;
    }

    public OutputStream getOutputStream() {
        return out;
    }
}
