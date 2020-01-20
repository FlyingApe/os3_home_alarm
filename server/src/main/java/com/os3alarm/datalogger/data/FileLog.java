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

    public FileLog(String logDirectory, String logfile) {

        String logFilePath = logDirectory + "\\" + logfile;

        if(!Files.exists(Paths.get(logDirectory))){
            System.out.println("Logfile directory does not exist, it wil be created");
            new File(logDirectory).mkdir();
            System.out.println("Directory '" + logDirectory + "' has been created");
        }

        File logFile = new File(logFilePath);

        try {
            scanner = new Scanner(logFile);
            setArchive();
        } catch (FileNotFoundException fnfe){
            System.out.println("Logfile " + logfile + " not found, one wil be created");

            Date date = new Date();
            String dateString = new SimpleDateFormat("HH:mm dd-MM-yyyy").format(date);
            archive = dateString.concat(" :: Logfile created").concat("\n");
        }

        try {
            writer = new PrintWriter(logFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        if(archive != null){
            writer.print(archive);
            archive = null;
        }

        writer.println(message);
        writer.flush();
    }
}
