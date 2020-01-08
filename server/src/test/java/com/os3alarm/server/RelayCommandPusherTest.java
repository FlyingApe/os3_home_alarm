package com.os3alarm.server;

import com.os3alarm.server.relay.RelaySocketListener;
import com.os3alarm.server.relay.models.AlarmPool;
import com.os3alarm.server.models.Commands;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/*
Depricated class, can be used for testpurposes
functionality moved to SpringIntegrator.pushCommand(alarmToken, command)
to us as a test, uncomment initialization in RelaySocketListener
 */

public class RelayCommandPusherTest{
    private static RelaySocketListener socketListener;
    private static AlarmPool pool;

    public static void main(String[] args) {
        socketListener = RelaySocketListener.getInstance();
        pool = AlarmPool.getInstance();


        while(pool.getAlarmByToken(args[0]) == null){
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Thread t = new Thread(new commandPusher(args[0]));
        t.start();
    }

    private static class commandPusher implements Runnable{
        private BufferedWriter writer;

        public commandPusher(String token) {
            writer = AlarmPool.getInstance().getAlarmByToken(token).getWriter();
        }

        public void run() {
            //TODO: Conditie om de loop te breken als de verbinding verbreekt |||| of proberen om de verbinding te herstellen
            int x = 0;
            while (true) {
                if(x == 4){
                    x = 0;
                }

                String jsonCommand = "{\"command\":\""+ Commands.values()[x]+"\"}\n";

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
            try {
                writer.write(pushMessage);
                writer.flush();
                System.out.println("Pushed: " + pushMessage);
            } catch (IOException e) {
                //connection has failed
                System.out.println("Write failed, connection set to disconnected");
            }
        }
    }
}
