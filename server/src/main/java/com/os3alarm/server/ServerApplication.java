package com.os3alarm.server;

import com.os3alarm.datalogger.SimpleLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SimpleLogger.createLog("AlarmEventChanges.txt");
		SpringApplication.run(ServerApplication.class, args);
	}


}
