package nl.bastiaansierd.relay.streams;

import nl.bastiaansierd.interfaces.DataStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MockServerStream implements DataStream {
    private static MockServerStream instance = null;
    private final String HOST = "localhost";
    private final int PORT = 3000;

    Socket s = null;
    private InputStream serverIn = null;
    private OutputStream serverOut = null;

    public static MockServerStream getInstance() {
        /* singelton initialisatie*/
        if(instance == null){
            instance = new MockServerStream();
        }
        return instance;
    }


    public MockServerStream(){
    }

    public void connect(){
        try {
            System.out.println("Connecting to MockServer, HOST: " + HOST + ", PORT: " + PORT + "......");
            s = new Socket(HOST, PORT);
            serverOut = s.getOutputStream();
            serverIn = s.getInputStream();
            System.out.println("Succes, connected to MockServer.");
        } catch (IOException e) {
            System.out.println("Could not connect.");
            //e.printStackTrace();
        }
    }

    public boolean isConnected(){
        if(s != null && s.isConnected()){
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
