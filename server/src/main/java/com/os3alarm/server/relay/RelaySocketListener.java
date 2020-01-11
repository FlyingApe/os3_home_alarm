package com.os3alarm.server.relay;

import com.os3alarm.server.relay.models.AlarmPool;
import com.os3alarm.server.relay.models.RelayStream;
import com.os3alarm.server.services.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RelaySocketListener {
    //private AlarmPool pool;
    private static RelaySocketListener instance = null;
    private MessagingService _messagingService;


    public static RelaySocketListener getInstance(){
        if(instance == null){
            instance = new RelaySocketListener();
        }
        return instance;
    }
    private RelaySocketListener(MessagingService messagingService) {
        _messagingService = messagingService;
        Thread t = new Thread(new Connector(_messagingService));
        t.start();
    }

    private static class Connector implements Runnable {
        final int SBAP_PORT = 3000;
        ServerSocket server = null;

        private MessagingService _messagingService;
        @Autowired
        Connector(MessagingService messagingService){
            _messagingService = messagingService;
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

                RelayStream stream = new RelayStream(s);

                /// TODO: create factory, replace with interface

                RelayInputStreamParser inputStreamParser = new RelayInputStreamParser(stream, _messagingService);
                Thread inputStreamThread = new Thread(inputStreamParser);
                inputStreamThread.start();
            }
        }
    }
}
