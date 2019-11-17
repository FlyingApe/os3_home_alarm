package nl.bastiaansierd.mockArduino;

import nl.bastiaansierd.interfaces.DataStream;
import nl.bastiaansierd.mockArduino.Streams.RelayStream;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class MockArduino {
    public static void main(String[] args) throws IOException {
        final int SBAP_PORT = 5000;
        ServerSocket server = new ServerSocket(SBAP_PORT);
        System.out.println("Waiting for relay to connect...");
        while(true) {
            Socket s = server.accept();
            System.out.println("RelayThreadService connected.");
            DataStream stream = new RelayStream(s);
            TestService testService = new TestService(stream);
            Thread t = new Thread(testService);
            t.start();
        }
    }

    public static class TestService implements Runnable{
        private DataStream relayStream;
        private int alarmAudioOn = 0;

        public TestService(DataStream s) {
            relayStream = s;
            while(!relayStream.isConnected()){
                relayStream.connect();
            }

        }

        public void run() {


            int x = 0;
            while (true) {
                x++;
                System.out.println("loopCount: " + (x+1));
                BufferedReader mockSerial = new BufferedReader(new InputStreamReader(relayStream.getInputStream()));
                try {
                    if(mockSerial.ready()){
                        alarmAudioOn = mockSerial.read();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Writer mockSerialWriter = new OutputStreamWriter(relayStream.getOutputStream());

                try {
                    mockSerialWriter.write("{\n");
                    mockSerialWriter.write("\t\"microphone\":\"");
                    mockSerialWriter.write(String.valueOf(x));
                    mockSerialWriter.write("\",");
                    mockSerialWriter.write("\"distance\":\"");
                    mockSerialWriter.write(String.valueOf(x));
                    mockSerialWriter.write("\",");
                    mockSerialWriter.write("\"movement\":\"");
                    mockSerialWriter.write(String.valueOf(x));
                    mockSerialWriter.write("\",");
                    mockSerialWriter.write("\"AlarmAudioOn\":\"");
                    mockSerialWriter.write(String.valueOf(alarmAudioOn));
                    mockSerialWriter.write("\"\n");
                    mockSerialWriter.write("}\n");
                    mockSerialWriter.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

