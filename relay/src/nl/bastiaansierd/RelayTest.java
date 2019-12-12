package nl.bastiaansierd;

import nl.bastiaansierd.communicationHandlers.ArduinoToServerCommuncationHandler;
import nl.bastiaansierd.communicationHandlers.ServerToArduinoCommunicationHandler;
import nl.bastiaansierd.mockArduino.Streams.MockArduinoStream;
import nl.bastiaansierd.mockServer.Streams.MockServerStream;

public class RelayTest {
    public static void main( String[] args) {
        Thread arduinoThread = new Thread(new ArduinoToServerCommuncationHandler(MockArduinoStream.getInstance(), MockServerStream.getInstance()));
        Thread relayThread = new Thread(new ServerToArduinoCommunicationHandler(MockArduinoStream.getInstance(), MockServerStream.getInstance()));

        arduinoThread.start();
        relayThread.start();
    }
}
