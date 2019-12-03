package com.os3alarm.server.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.os3alarm.server.relayHelpers.RelayStream;
import com.os3alarm.server.relayHelpers.ServerSideJsonBuilder;

public class RelayService {
    public void runRelayConnection() throws IOException {
        final int SBAP_PORT = 3000;
        ServerSocket server = new ServerSocket(SBAP_PORT);
        System.out.println("Waiting for clients to connect...");
        while(true) {
            Socket s = server.accept();
            System.out.println("Client connected.");
            RelayStream stream = new RelayStream(s);
            TestService testService = new TestService(stream);
            Thread t = new Thread(testService);
            t.start();
        }
    }

    public static class TestService implements Runnable{
        private RelayStream relayStream;

        public TestService(RelayStream s) {
            relayStream = s;
        }

        public void run() {
            ServerSideJsonBuilder jsonBuilder = new ServerSideJsonBuilder(relayStream.getInputStream());
            String audioOn = "1";

            while (true) {
                //System.out.println("looping");

                //let the jasonbuilder build strings with relayInput
                jsonBuilder.build();
                String json = jsonBuilder.getJson();

                //if there is a valid jsonString -> print it
                if(!json.equals("")){
                    System.out.println("JSON: " + json);
                }

                sendToRelay(audioOn);

                System.out.println("audioOn:" + audioOn);
            }
        }

        private void sendToRelay(String reply){
            PrintWriter writer = new PrintWriter(relayStream.getOutputStream());
            writer.print(reply);
            writer.flush();
        }
    }
}
