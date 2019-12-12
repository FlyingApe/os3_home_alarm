package nl.bastiaansierd.ArduinoTester;

import nl.bastiaansierd.ArduinoTester.helpers.InputStreamReader;
import nl.bastiaansierd.interfaces.DataStream;

import java.io.BufferedReader;
import java.util.concurrent.TimeUnit;

public class RelayThreadService implements Runnable
{
    private DataStream arduinoStream;
    private BufferedReader reader = null;

    public RelayThreadService() {
        arduinoStream = ArduinoStream.getInstance();
        //arduinoStream = MockArduinoStream.getInstance();
    }

    public void run ()
    {
        //InputStreamReader builds jsonStrings from te Arduino inputstream
        InputStreamReader inputStreamReader = InputStreamReader.getInstance(arduinoStream.getInputStream());

        Integer i = 0;
        while (true) {
            //System.out.println("looping");


            //let inputStreamReader read the arduino's inputstream and build a jsonString
            inputStreamReader.build();


            //send serverReply to the arduino
            writeToArduino("{\"TestJson\":\"Dit is test\" "+ i.toString() + "}");

            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            i++;
        }
    }

    private void writeToArduino(String serverInput){
        try{
            for(Character c : serverInput.toCharArray()){
                arduinoStream.getOutputStream().write(c);
                //System.out.println(c);
            }
            arduinoStream.getOutputStream().flush();
        } catch (Exception e){

        }
    }
}