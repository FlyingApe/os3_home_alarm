package nl.bastiaansierd;

import nl.bastiaansierd.communicationHandlers.ArduinoToServerCommuncationHandler;
import nl.bastiaansierd.communicationHandlers.ServerToArduinoCommunicationHandler;
import nl.bastiaansierd.streams.ArduinoStream;
import nl.bastiaansierd.streams.ServerStream;

public class Relay {
    public static void main ( String[] args )
    {
        Thread arduinoThread = new Thread(new ArduinoToServerCommuncationHandler(ArduinoStream.getInstance(), ServerStream.getInstance()));
        Thread relayThread = new Thread(new ServerToArduinoCommunicationHandler(ArduinoStream.getInstance(), ServerStream.getInstance()));

        arduinoThread.start();
        relayThread.start();
    }
}
