package nl.bastiaansierd;

import nl.bastiaansierd.mockArduino.ArduinoGenerator;
import nl.bastiaansierd.relay.communicationHandlers.StreamToStreamPassthroughAgent;
import nl.bastiaansierd.relay.interfaces.BufferedReadWriter;
import nl.bastiaansierd.relay.streams.MockArduinoConnection;
import nl.bastiaansierd.relay.streams.ServerConnection;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MockArduinoRelayGenerator {

    public static void main ( String[] args )
    {
        Integer numberOfAlarms = null;
        Scanner in  = new Scanner(System.in);
        boolean validInput = false;

        System.out.println("Entering Setup.\n");

        while (!validInput) {
            String input;

            System.out.println("How many alarms do you wish to simulate?:");
            input = in.nextLine();
            try{
                numberOfAlarms = Integer.parseInt(input);
                validInput = true;
               
            } catch (Exception e){
                System.out.println("No valid input has been given.");
                validInput = false;
            }
        }

        //startup ArduinoGenerator driver
        Thread t = new Thread(new ArduinoGenerator());
        t.start();

        for (int i=0; i < numberOfAlarms; i++) {
            //setup new connection with ArduinoGenerator
            BufferedReadWriter arduinoComs = new MockArduinoConnection();
            BufferedReadWriter serverComs = new ServerConnection();

            Thread serverToArduinoCom = new Thread(new StreamToStreamPassthroughAgent(serverComs, arduinoComs));
            Thread arduinoToServerCom = new Thread(new StreamToStreamPassthroughAgent(arduinoComs, serverComs));

            serverToArduinoCom.start();
            arduinoToServerCom.start();

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
