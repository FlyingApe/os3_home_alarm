package com.os3alarm.server.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.os3alarm.server.relayHelpers.RelayStream;
import com.os3alarm.server.relayHelpers.ServerSideJsonBuilder;

//TODO: refactor naar meerdere klassen?
public class RelayService {
    public void runRelayConnection() {
        Thread t = new Thread(new RelayConnector());
        t.start();
    }

    public static class RelayConnector implements Runnable{
        final int SBAP_PORT = 3000;
        ServerSocket server = null;

        public RelayConnector(){
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
                TestService testService = new TestService(stream);
                Thread t = new Thread(testService);
                t.start();
            }
        }
    }


    public static class TestService implements Runnable{
        private RelayStream relayStream;

        public TestService(RelayStream s) {
            relayStream = s;
        }

        public void run() {
            ServerSideJsonBuilder jsonBuilder = new ServerSideJsonBuilder(relayStream.getInputStream());
            String audioOn = "0";

            //TODO: Conditie om de loop te breken als de verbinding verbreekt |||| of proberen om de verbinding te herstellen
            while (relayStream.isConnected()) {
                //let the jsonbuilder build strings with relayInput
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
