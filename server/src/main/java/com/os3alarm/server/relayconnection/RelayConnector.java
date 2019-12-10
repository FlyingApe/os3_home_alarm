package com.os3alarm.server.relayconnection;

import com.os3alarm.server.relayconnection.pojo.AlarmPool;
import com.os3alarm.server.relayconnection.pojo.RelayStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//TODO: singleton van maken
class RelayConnector {
    private AlarmPool pool;
    private static RelayConnector instance = null;

    static RelayConnector getInstance(){
        if(instance == null){
            instance = new RelayConnector();
        }
        return instance;
    }

    private RelayConnector() {
        Thread t = new Thread(new Connector());
        t.start();
    }

    public static class Connector implements Runnable {
        final int SBAP_PORT = 3000;
        ServerSocket server = null;

        Connector(){
            try {
                server = new ServerSocket(SBAP_PORT);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }

        public void run(){
            //TODO: conditie loop aanpassen. -. Socketlistener
            //TODO: checken of meerdere alarmen kunnen connecten

            System.out.println("Waiting for clients to connect...");
            Socket s = null;
            while(true) {
                try {
                    if (((s = server.accept()) == null)) break;
                } catch (IOException e) {
                    System.out.println("server.accept() -->> throws IOExceptions");
                    //e.printStackTrace();
                }
                System.out.println("Client connected.");
                RelayStream stream = new RelayStream(s);
                ConnectionService connection = new ConnectionService(stream);
                Thread t = new Thread(connection);
                t.start();
            }
        }
    }



}
