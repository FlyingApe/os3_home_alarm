package nl.bastiaansierd.ArduinoTester.helpers;

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

    public static InputStreamReader getInstance(InputStream in){
        if(instance == null){
            instance = new InputStreamReader();
            //inputStream = in;
            src=new BufferedReader(new java.io.InputStreamReader(in));
        }
        return instance;
    }


    public void build(){
        String line;
        try {
            while ((line = src.readLine()) != null){
                System.out.println(line);
            }

        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

}
