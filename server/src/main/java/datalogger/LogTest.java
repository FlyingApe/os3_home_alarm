package datalogger;

public class LogTest {
    public static void main(String[] args){
        SimpleLogger.createLog("log.txt");

        SimpleLogger.getLog().write("ik", "test");
    }
}
