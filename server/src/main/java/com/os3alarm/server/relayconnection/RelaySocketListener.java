package com.os3alarm.server.relayconnection;

import com.os3alarm.server.relayconnection.pojo.AlarmPool;
import com.os3alarm.server.relayconnection.pojo.RelayStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class RelaySocketListener {
    private AlarmPool pool;
    private static RelaySocketListener instance = null;

    static RelaySocketListener getInstance(){
        if(instance == null){
            instance = new RelaySocketListener();
        }
        return instance;
    }

    private RelaySocketListener() {
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

            //System.out.println("Waiting for clients to connect...");
            Socket s = null;
            while(true) {
                try {
                    if (((s = server.accept()) == null)) break;
                } catch (IOException e) {
                    System.out.println("server.accept() -->> throws IOExceptions");
                    //e.printStackTrace();
                }
                System.out.println("Client connected.");

                /* TODO: replace single thread with dual threads -> one reading from relay, one writing to relay
                could either be build here or inside RelayConnectionHandler.
                In the last case, threads should not be build here, but inside RelayConnectionHandler
                */

                RelayStream stream = new RelayStream(s);
                RelayConnectionHandler connection = new RelayConnectionHandler(stream);
                Thread t = new Thread(connection);
                t.start();
            }
        }
    }



}
