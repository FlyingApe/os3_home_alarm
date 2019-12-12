package nl.bastiaansierd.ArduinoTester.helpers;

import nl.bastiaansierd.ArduinoTester.ArduinoStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamReader {
    private static InputStreamReader instance = null;
    private static BufferedReader src;
    //private static InputStream inputStream = null;

    public static InputStreamReader getInstance(){
        if(instance == null){
            instance = new InputStreamReader();
        }
        return instance;
    }




}
