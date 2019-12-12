package nl.bastiaansierd.ArduinoTester;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import nl.bastiaansierd.interfaces.DataStream;

import java.io.*;

public class ArduinoStream {
    private final String comPort = "COM6";
    private static ArduinoStream instance = null;
    private static BufferedWriter destination = null;
    private static BufferedReader source=null;
    private SerialPort arduinoSerialPort = null;

     public static ArduinoStream getInstance(){
        /* singelton initialisatie*/
         try {
        if(instance == null){
            instance = new ArduinoStream();
            source=new BufferedReader(new InputStreamReader(instance.arduinoSerialPort.getInputStream()));
            destination=new BufferedWriter(new OutputStreamWriter(instance.arduinoSerialPort.getOutputStream()));
        }}
         catch (IOException ex) {

         }
        return instance;
    }

    private ArduinoStream() {
        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect()
    {
        try{
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(comPort);
            if ( portIdentifier.isCurrentlyOwned() )
            {
                System.out.println("Error: Port is currently in use");
            }
            else
            {
                CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

                if ( commPort instanceof SerialPort)
                {
                    arduinoSerialPort = (SerialPort) commPort;
                    arduinoSerialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                }
                else
                {
                    System.out.println("Error: Only serial ports are handled by this example.");
                }
            }
        } catch (Exception e){
            //e.printStackTrace();
        }
    }

    public boolean isConnected() {
        if(arduinoSerialPort != null){
            return true;
        }
        else {
            return false;
        }
    }

    public BufferedReader getInputStream() {
        return source;
    }

    public BufferedWriter getOutputStream() {
        return destination;
    }

    public static void read() {
        try {
            String line;
            while ((line = source.readLine()) != null){
                System.out.println(line);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}
