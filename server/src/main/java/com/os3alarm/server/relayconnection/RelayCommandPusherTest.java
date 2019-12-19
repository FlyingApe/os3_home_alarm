package com.os3alarm.server.relayconnection;

import com.os3alarm.server.relayconnection.pojo.RelayStream;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/*
Depricated class, can be used for testpurposes
functionality moved to SpringIntegrator.pushCommand(alarmToken, command)
to us as a test, uncomment initialization in RelaySocketListener
 */

public class RelayCommandPusherTest implements Runnable {
    private RelayStream relayStream;

    public RelayCommandPusherTest(RelayStream stream) {
        relayStream = stream;
    }

    public void run() {
        //TODO: Conditie om de loop te breken als de verbinding verbreekt |||| of proberen om de verbinding te herstellen
        int x = 0;
        while (relayStream.isConnected()) {
            if(x == 4){
                x = 0;
            }

            String jsonCommand = "{\"command\":\""+Commands.values()[x]+"\"}\n";

            pushToAlarm(jsonCommand);


            try {
                TimeUnit.SECONDS.sleep(10);
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
            System.out.println("Pushed: " + pushMessage);
        } catch (IOException e) {
            //connection has failed
            System.out.println("Write failed, connection set to disconnected");
            relayStream.clearWriter();
        }
    }
}
