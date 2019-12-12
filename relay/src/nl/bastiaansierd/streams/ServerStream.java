package nl.bastiaansierd.streams;

import nl.bastiaansierd.interfaces.DataStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerStream implements DataStream {
    private static ServerStream instance = null;
    private final String HOST = "localhost";
    private final int PORT = 3000;

    Socket s = null;
    private InputStream serverIn = null;
    private OutputStream serverOut = null;

    public static ServerStream getInstance() {
        /* singelton initialisatie*/
        if(instance == null){
            instance = new ServerStream();
        }
        return instance;
    }


    public ServerStream(){
    }

    public void connect(){
        try {
            System.out.println("Connecting to Server, HOST: " + HOST + ", PORT: " + PORT + "......");
            s = new Socket(HOST, PORT);
            serverOut = s.getOutputStream();
            serverIn = s.getInputStream();
            System.out.println("Succes, connected to server.");
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
