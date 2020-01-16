package nl.bastiaansierd;

import nl.bastiaansierd.relay.communicationHandlers.StreamToStreamPassthroughAgent;
import nl.bastiaansierd.relay.interfaces.BufferedReadWriter;
import nl.bastiaansierd.relay.streams.ArduinoConnection;
import nl.bastiaansierd.relay.streams.ServerConnection;

import java.util.Scanner;

public class Relay {

    public static void main ( String[] args )
    {
        BufferedReadWriter arduinoComs = null;
        boolean validInput = false;
        Scanner in  = new Scanner(System.in);

        while (!validInput){
            try {
                System.out.println("To which serial port is the arduino connected? (e.g. COM6)");
                String input = in.nextLine();

                // instantiate RealArduinoConnection
                arduinoComs = ArduinoConnection.getInstance(input);

                System.out.println("Connected to arduino on comport: " + input);
                validInput = true;
            } catch (Exception e) {
                System.out.println("No valid input has been given.");
            }
        }

        BufferedReadWriter serverComs = new ServerConnection();

        Thread serverToArduinoCom = new Thread(new StreamToStreamPassthroughAgent(serverComs, arduinoComs));
        Thread arduinoToServerCom = new Thread(new StreamToStreamPassthroughAgent(arduinoComs, serverComs));

        serverToArduinoCom.start();
        arduinoToServerCom.start();

    }
}
