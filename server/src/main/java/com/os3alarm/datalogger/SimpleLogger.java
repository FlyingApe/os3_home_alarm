package com.os3alarm.datalogger;

import com.os3alarm.datalogger.data.FileLog;
import com.os3alarm.datalogger.data.DataLog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLogger {
    private static SimpleLogger instance = null;
    private String logDirectory;
    private DataLog log;


    private SimpleLogger(String logFile){
        logDirectory = System.getProperty("user.dir") + "\\log";
        if(!getFileExtension(logFile).equals("txt")){
            System.out.println(logFile + " could not be written, log files should be '.txt' files.");
        } else {
            log = new FileLog(logDirectory, logFile);
        }
    }

    public static SimpleLogger getLog(){
        /* singelton initialisatie*/
        if(instance != null){
            return instance;
        } else {
            System.out.println("Logger has not yet been instanciated, create a new log with SimpleLogger.createLog(String Logfile).");
            return null;
        }
    }

    public static void createLog(String logFile){
        /* singelton initialisatie*/
        instance = new SimpleLogger(logFile);
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }


    private void writer(String whoDunnit, String logText){
        Date date = new Date();
        String dateString = new SimpleDateFormat("HH:mm dd-MM-yyyy").format(date);
        String message = dateString + " :: " + whoDunnit + " :: " + logText;

        try{
            log.saveLog(message);
            //System.out.println("Message written to " + logDirectory + "\\" + log + ": " + message);
        }
        catch (Exception e){
            //System.out.println("Could not write to " + logDirectory + "\\" + log + ".");
        }
    }

    public static void write(String whoDunnit, String logText){
        SimpleLogger thisLogger = SimpleLogger.getLog();
        if(thisLogger!=null){
            thisLogger.writer(whoDunnit, logText);
        }
    }

}

