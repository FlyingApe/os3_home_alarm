package datalogger.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class FileLog implements DataLog {
    private String logFilePath;
    private String archive;
    private PrintWriter log;

    public FileLog(String logDirectory, String logfile) {
        logFilePath = logDirectory + "\\" + logfile;

        if(!Files.exists(Paths.get(logDirectory))){
            System.out.println("Logfile directory does not exist, it wil be created");
            new File(logDirectory).mkdir();
            System.out.println("Directory '" + logDirectory + "' has been created");
        }

        try{
            File logFile = new File(logFilePath);
            Scanner scanner = new Scanner(logFile);
            StringBuilder sBuilder = new StringBuilder();
            while (scanner.hasNextLine()){
                sBuilder.append(scanner.nextLine() + "\n");
            }
            setArchive(sBuilder.toString());

        }
        catch (FileNotFoundException fnfe){
            System.out.println("Logfile " + logfile + " not found, one wil be created");
            Date date = new Date();
            String dateString = new SimpleDateFormat("HH:mm dd-MM-yyyy").format(date);
            archive = dateString + " :: Logfile created";
        }

        try {
            log = new PrintWriter(logFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setArchive(String archive) {
        this.archive = archive;
    }

    public void saveLog(String message){
        log.println(message);
        if(archive != null){log.print(archive); }
        log.flush();
    }
}
