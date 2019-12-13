package nl.bastiaansierd;

import nl.bastiaansierd.communicationHandlers.StreamToStreamPassthroughAgent;
import nl.bastiaansierd.interfaces.BufferedReadWriter;
import nl.bastiaansierd.mockArduino.MockArduino;
import nl.bastiaansierd.streams.ArduinoConnection;
import nl.bastiaansierd.streams.MockArduinoConnection;
import nl.bastiaansierd.streams.ServerConnection;

import java.util.Scanner;

public class Relay {

    public static void main ( String[] args )
    {
        BufferedReadWriter arduinoComs = null;

        // setting arduinoComs to MockArduinoConnection or ArduinoConnection depending on commandline args
        if(args.length == 1 && args[0].trim().equals("mock")){
            // instantiate MockArduino
            Thread t = new Thread(new MockArduino());
            t.start();

            // instantiate MockArduinoConnection
            arduinoComs = MockArduinoConnection.getInstance();
        } else if(args.length == 2 && args[0].equals("real") && args[1] != null){
            // instantiate RealArduinoConnection
            arduinoComs = ArduinoConnection.getInstance(args[1]);
        } else {// if no commandline args have been given. ask user what to do

            System.out.println("No valid commandline arguments given.");
            System.out.println("Entering Setup.\n");

            Scanner in  = new Scanner(System.in);
            boolean validInput = false;
            while (!validInput){
                System.out.println("Has a physical alarm been connected? (Y/N) (N wil set up a MockArduino):");

                String input = in.nextLine();

                if(input.equals("Y") || input.equals("y")){
                    System.out.println("To which serial port is the arduino connected? (e.g. COM6)");
                    input = in.nextLine();
                    System.out.println("comport :" + input);
                    // instantiate RealArduinoConnection
                    arduinoComs = ArduinoConnection.getInstance(input);
                    validInput = true;
                } else if(input.equals("N") || input.equals("n")){
                    // instantiate MockArduino
                    Thread t = new Thread(new MockArduino());
                    t.start();

                    // instantiate MockArduinoConnection
                    arduinoComs = MockArduinoConnection.getInstance();
                    validInput = true;
                } else {
                    System.out.println("No valid input has been given.");
                }
            }
        }

        BufferedReadWriter serverComs = ServerConnection.getInstance();


        Thread serverToArduinoCom = new Thread(new StreamToStreamPassthroughAgent(serverComs.getReader(), arduinoComs.getWriter()));
        Thread arduinoToServerCom = new Thread(new StreamToStreamPassthroughAgent(arduinoComs.getReader(), serverComs.getWriter()));

        serverToArduinoCom.start();
        arduinoToServerCom.start();

    }
}
