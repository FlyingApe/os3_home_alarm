package com.os3alarm.server.relay;

import com.os3alarm.server.relay.models.AlarmPool;
import com.os3alarm.server.relay.models.RelayStream;
import com.os3alarm.server.services.MessagingService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RelaySocketListener {
    //private AlarmPool pool;
    private static RelaySocketListener instance = null;
    private MessagingService _messagingService;


    public static RelaySocketListener getInstance(){
        if(instance == null){
            System.out.println("RelaySocketListener should be initiated through initRelaySocketListener(MessagingService messagingService)");
        }
        return instance;
    }

    public static void initRelaySocketListener(MessagingService messagingService){
        instance = new RelaySocketListener(messagingService);
    }

    private RelaySocketListener(MessagingService messagingService) {
        _messagingService = messagingService;
        Thread t = new Thread(new Connector(_messagingService));
        t.start();
    }

    private static class Connector implements Runnable {
        private final int SBAP_PORT = 3000;
        private boolean isListening = true;
        private MessagingService _messagingService;

        Connector(MessagingService messagingService){
            _messagingService = messagingService;
        }

        public void run(){
            // let alarms connect
            try{
                ServerSocket server = new ServerSocket(SBAP_PORT);

                while(isListening) {
                    Socket s = server.accept();

                    RelayStream stream = new RelayStream(s);
                    RelayInputStreamParser inputStreamParser = new RelayInputStreamParser(stream, _messagingService);
                    Thread inputStreamThread = new Thread(inputStreamParser);
                    inputStreamThread.start();
                }
            } catch (IOException e){
                //e.printStackTrace();
            }
        }
    }
}
