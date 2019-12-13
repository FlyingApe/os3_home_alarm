package nl.bastiaansierd.streams;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import nl.bastiaansierd.interfaces.BufferedReadWriter;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class ArduinoConnection implements BufferedReadWriter {
    private String comPort = null;
    private static ArduinoConnection instance = null;
    private SerialPort arduinoSerialPort = null;
    private BufferedReader source = null;
    private BufferedWriter destination = null;


    public static ArduinoConnection getInstance(String port){
        /* singelton initialisatie*/
        if(instance == null){
            instance = new ArduinoConnection();
            instance.comPort = port;
            instance.connect();
        }
        return instance;
    }

    public static ArduinoConnection getInstance() {
        /* singelton initialisatie*/
        if(instance.comPort == null){
            try {
                throw new InstantiationException("No serial comPort has been given, first instantiation needs a 'String port' argument. Use ArduinoConnection.getInstance(String token) instead");
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private ArduinoConnection(){
    }

    public void connect()
    {
        int timeOut = 3;
        while(!isConnected()) {
            try {
                CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(comPort);
                if (portIdentifier.isCurrentlyOwned()) {
                    System.out.println("Error: Port is currently in use");
                } else {
                    CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

                    arduinoSerialPort = (SerialPort) commPort;
                    arduinoSerialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                    source = new BufferedReader(new InputStreamReader(arduinoSerialPort.getInputStream()));
                    destination = new BufferedWriter(new OutputStreamWriter(arduinoSerialPort.getOutputStream()));

                }
            } catch (Exception e) {
                try {
                    System.out.println("Could not connect to Arduino on comport: "+comPort+". Will try again in " + timeOut + " seconds");
                    TimeUnit.SECONDS.sleep(timeOut);
                } catch (InterruptedException ex) {
                    //ex.printStackTrace();
                }
                //e.printStackTrace();
            }
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

    @Override
    public BufferedReader getReader() {
        return source;
    }

    @Override
    public BufferedWriter getWriter() {
        return destination;
    }
}
