package nl.bastiaansierd.mockArduino;

import nl.bastiaansierd.interfaces.BufferedReadWriter;
import nl.bastiaansierd.mockArduino.Streams.RelayConnection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MockArduino implements Runnable{

    public void run() {
        try{
            final int SBAP_PORT = 5000;
            ServerSocket server = new ServerSocket(SBAP_PORT);
            System.out.println("Waiting for relay to connect...");
            while(true) {
                Socket socket = server.accept();
                System.out.println("ServerToArduinoCommunicationHandler connected.");
                BufferedReadWriter stream = new RelayConnection(socket);
                TestService testService = new TestService(stream);
                Thread t = new Thread(testService);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class TestService implements Runnable{
        private RelayConnection relayConnection;
        private String token;
        private int alarmAudioOn = 0;

        public TestService(BufferedReadWriter s) {
            this.relayConnection = (RelayConnection) s;
            token = getRandomHexString(16);
            while(!relayConnection.isConnected()){
                relayConnection.connect();
            }
        }

        private String getRandomHexString(int numchars){
            Random r = new Random();
            StringBuffer sb = new StringBuffer();
            while(sb.length() < numchars){
                sb.append(Integer.toHexString(r.nextInt()));
            }

            return sb.toString().substring(0, numchars);
        }

        public void run() {
            int x = 0;
            while (true) {
                x++;
                //System.out.println("loopCount: " + (x+1));
                BufferedReader mockSerial = relayConnection.getReader();
                try {
                    if(mockSerial.ready()){
                        alarmAudioOn = mockSerial.read();
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                }

                BufferedWriter mockSerialWriter = relayConnection.getWriter();

                try {
                    mockSerialWriter.write("{\n");
                    mockSerialWriter.write("\"token\":\"");
                    mockSerialWriter.write(token);
                    mockSerialWriter.write("\",");
                    mockSerialWriter.write("\"microphone\":\"");
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

