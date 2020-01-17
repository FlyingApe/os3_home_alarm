package com.os3alarm.alarmconnector;

import com.os3alarm.alarmconnector.controllers.LiveAlarmStreamParser;
import com.os3alarm.alarmconnector.models.RelayStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RelaySocketListener {
    private static RelaySocketListener instance = null;

    private RelaySocketListener() {
        Thread t = new Thread(new Connector());
        t.start();
    }

    public static void initRelaySocketListener(){
        instance = new RelaySocketListener();
    }

    private static class Connector implements Runnable {
        private final int SBAP_PORT = 3000;
        private boolean isListening = true;

        public void run(){
            // let alarms connect
            try{
                ServerSocket server = new ServerSocket(SBAP_PORT);

                while(isListening) {
                    Socket s = server.accept();

                    RelayStream stream = new RelayStream(s);
                    LiveAlarmStreamParser inputStreamParser = new LiveAlarmStreamParser(stream);
                    Thread inputStreamThread = new Thread(inputStreamParser);
                    inputStreamThread.start();
                }
            } catch (IOException e){
                //e.printStackTrace();
            }
        }
    }
}
