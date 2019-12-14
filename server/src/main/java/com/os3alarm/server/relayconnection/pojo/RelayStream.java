package com.os3alarm.server.relayconnection.pojo;

import java.io.*;
import java.net.Socket;

public class RelayStream {
    private Socket socket = null;
    private BufferedReader source = null;
    private BufferedWriter destination = null;
    //private InputStream in = null;
    //private OutputStream out = null;

    public RelayStream(Socket s) {
        this.socket = s;
        connect();
    }

    private void connect() {
        while(!isConnected()){
            try {
                source = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                destination = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                //in = socket.getInputStream();
                //out = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public boolean isConnected() {
        /// TODO: betere conditie schrijven om verbinding te checken
        if(source == null || destination == null){
            return false;
        } else {
            return true;
        }
    }

    public BufferedReader getReader() {
        return source;
    }

    public BufferedWriter getWriter() {
        return destination;
    }

    public void clearWriter(){
        destination = null;
    }

    public void clearReader(){
        source = null;
    }
}