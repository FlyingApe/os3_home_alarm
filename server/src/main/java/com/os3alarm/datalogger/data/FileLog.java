package com.os3alarm.datalogger.data;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class FileLog implements DataLog {
    private String archive;
    private PrintWriter writer;
    private Scanner scanner;
    private String directory;
    private String fileName;

    public FileLog(String logDirectory, String logfile) {
        //this.directory = logDirectory;
        //this.fileName = logfile;

        String logFilePath = logDirectory + "\\" + logfile;

        if(!Files.exists(Paths.get(logDirectory))){
            System.out.println("Logfile directory does not exist, it wil be created");
            new File(logDirectory).mkdir();
            System.out.println("Directory '" + logDirectory + "' has been created");
        }


        File logFile = null;

        try {
            logFile = new File(logFilePath);
            writer = new PrintWriter(logFilePath);
            scanner = new Scanner(logFile);
        } catch (FileNotFoundException fnfe){
            System.out.println("Logfile " + logfile + " not found, one wil be created");
            Date date = new Date();
            String dateString = new SimpleDateFormat("HH:mm dd-MM-yyyy").format(date);
            saveLog(dateString + " :: Logfile created");
        }
    }

    private void setArchive() {
        StringBuilder sBuilder = new StringBuilder();
        while (scanner.hasNextLine()){
            sBuilder.append(scanner.nextLine().concat("\n"));
        }

        this.archive = sBuilder.toString();
    }

    public synchronized void saveLog(String message){
        //setArchive();

        writer.println(message);
        writer.flush();

       /* if(archive != null){
            writer.print(archive);
            archive =
        }

        */
    }
}
