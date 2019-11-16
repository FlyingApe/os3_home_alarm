package nl.bastiaansierd.relay;

public class Relay {
    public static void main ( String[] args )
    {
        try
        {
            Thread relayThread = new Thread(new RelayThreadService());
            relayThread.start();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}
