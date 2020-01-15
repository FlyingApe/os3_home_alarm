package nl.bastiaansierd.mockArduino;

import nl.bastiaansierd.relay.interfaces.BufferedReadWriter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
This class is responsible for generating arduino drivers for each incomming connection
 */

public class ArduinoGenerator implements Runnable{
    private final int SBAP_PORT = 5000;
    private boolean isListening = true;
    private TokenGenerator tokenGenerator;

    public ArduinoGenerator(){
        this.tokenGenerator = TokenGenerator.getInstance();
    }

    public void run() {
        try{
            ServerSocket server = new ServerSocket(SBAP_PORT);

            while(isListening) {
                Socket socket = server.accept();

                //do something with the connected client
                BufferedReadWriter stream = new RelayConnection(socket);
                String token = tokenGenerator.getToken();
                ArduinoDriver arduinoDriver = new ArduinoDriver(stream, token);
                Thread t = new Thread(arduinoDriver);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

