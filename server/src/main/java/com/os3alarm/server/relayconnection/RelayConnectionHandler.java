package com.os3alarm.server.relayconnection;

import com.os3alarm.server.relayconnection.pojo.RelayStream;

import java.io.PrintWriter;

public class RelayConnectionHandler implements Runnable {
    private RelayStream relayStream;
    //private AlarmPool pool;

    public RelayConnectionHandler(RelayStream stream) {
        relayStream = stream;
        //pool = AlarmPool.getInstance();

        /// TODO: create factory, replace with interface
        RelayInputStreamParser inputStreamParser = new RelayInputStreamParser(relayStream.getInputStream());
        Thread inputStreamThread = new Thread(inputStreamParser);
        inputStreamThread.start();
    }

    public void run() {
        //TODO: Conditie om de loop te breken als de verbinding verbreekt |||| of proberen om de verbinding te herstellen
        while (relayStream.isConnected()) {


            /*
            String audioOn = "0";

            pushToAlarm(audioOn);

            System.out.println("audioOn:" + audioOn);
            */
        }
    }


    private void pushToAlarm(String reply){
        PrintWriter writer = new PrintWriter(relayStream.getOutputStream());
        writer.print(reply);
        writer.flush();
    }
}
