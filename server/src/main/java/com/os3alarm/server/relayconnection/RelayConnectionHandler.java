package com.os3alarm.server.relayconnection;

import com.os3alarm.server.relayconnection.pojo.RelayStream;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

enum Commands {
    setStatusInactive,
    setStatusActive,
    setAudioOff,
    setAudioOn,
}

public class RelayConnectionHandler implements Runnable {
    private RelayStream relayStream;
    //private AlarmPool pool;

    public RelayConnectionHandler(RelayStream stream) {
        relayStream = stream;
        //pool = AlarmPool.getInstance();

        /// TODO: create factory, replace with interface
        RelayInputStreamParser inputStreamParser = new RelayInputStreamParser(relayStream);
        Thread inputStreamThread = new Thread(inputStreamParser);
        inputStreamThread.start();

        // TODO: add thread for pushing data to the alarm that has an observer to eventhandle
    }

    public void run() {
        //TODO: Conditie om de loop te breken als de verbinding verbreekt |||| of proberen om de verbinding te herstellen
        int x = 0;
        while (relayStream.isConnected()) {
            if(x == 4){
                x = 0;
            }

            String jsonCommand = "{\"command\":\""+Commands.values()[x]+"\"}\n";

            System.out.println("Pushed: " + jsonCommand);
            pushToAlarm(jsonCommand);


            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            x++;
        }
    }


    private void pushToAlarm(String pushMessage){
        BufferedWriter writer = relayStream.getWriter();
        try {
            writer.write(pushMessage);
            writer.flush();
        } catch (IOException e) {
            //connection has failed
            System.out.println("Write failed, connection set to disconnected");
            relayStream.clearWriter();
        }
    }
}
