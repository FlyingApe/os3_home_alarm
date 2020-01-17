package com.os3alarm.server;

import com.os3alarm.datalogger.SimpleLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
		SimpleLogger.createLog("AlarmEventChanges.txt");
	}

}
