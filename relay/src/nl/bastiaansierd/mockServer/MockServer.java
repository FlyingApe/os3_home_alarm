package nl.bastiaansierd.mockServer;

import nl.bastiaansierd.interfaces.DataStream;
import nl.bastiaansierd.mockServer.Streams.RelayStream;
import nl.bastiaansierd.mockServer.helpers.ServerSideJsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MockServer {
    public static void main(String[] args) throws IOException {
        final int SBAP_PORT = 3000;
        ServerSocket server = new ServerSocket(SBAP_PORT);
        System.out.println("Waiting for clients to connect...");
        while(true) {
            Socket s = server.accept();
            System.out.println("Client connected.");
            DataStream stream = new RelayStream(s);
            TestService testService = new TestService(stream);
            Thread t = new Thread(testService);
            t.start();
        }
    }

    public static class TestService implements Runnable{
        private DataStream relayStream;

        public TestService(DataStream s) {
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

