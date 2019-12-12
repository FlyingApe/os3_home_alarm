package nl.bastiaansierd.ArduinoTester;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import nl.bastiaansierd.interfaces.DataStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArduinoStream implements DataStream {
    private final String comPort = "COM6";
    private static ArduinoStream instance = null;
    private SerialPort arduinoSerialPort = null;

     public static ArduinoStream getInstance(){
        /* singelton initialisatie*/
        if(instance == null){
            instance = new ArduinoStream();
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

    @Override
    public boolean isConnected() {
        if(arduinoSerialPort != null){
            return true;
        }
        else {
            return false;
        }
    }

    public InputStream getInputStream() {
        InputStream in = null;
        try {
            in = arduinoSerialPort.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return in;
    }

    public OutputStream getOutputStream() {
        OutputStream out = null;
        try {
            out = arduinoSerialPort.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out;
    }
}
