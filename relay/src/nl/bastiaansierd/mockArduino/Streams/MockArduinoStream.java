package nl.bastiaansierd.mockArduino.Streams;

import nl.bastiaansierd.interfaces.DataStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class MockArduinoStream implements DataStream {
    private static MockArduinoStream instance = null;
    private final String HOST = "localhost";
    private final int PORT = 5000;

    Socket s = null;
    private InputStream serverIn = null;
    private OutputStream serverOut = null;

    public static MockArduinoStream getInstance() {
        /* singelton initialisatie*/
        if (instance == null) {
            instance = new MockArduinoStream();
        }
        return instance;
    }

    public MockArduinoStream(){
        connect();
    }

    @Override
    public void connect() {
        int timeOut = 5;
        while (s == null){
            try {
                System.out.println("Connecting to MockArduino, HOST: " + HOST + ", PORT: " + PORT + "......");
                s = new Socket(HOST, PORT);
                serverOut = s.getOutputStream();
                serverIn = s.getInputStream();
                System.out.println("Succes, connected to MockArduino.");
            } catch (IOException e) {
                try {
                    System.out.println("Could not connect. Will try again in " + timeOut + " seconds");
                    TimeUnit.SECONDS.sleep(timeOut);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                //e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isConnected() {
        if(s.isConnected() && serverIn != null){
            return true;
        } else {
            return false;
        }
    }

    public InputStream getInputStream() {
        return serverIn;
    }

    public OutputStream getOutputStream() {
        return serverOut;
    }
}