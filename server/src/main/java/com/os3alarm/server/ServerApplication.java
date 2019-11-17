package com.os3alarm.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);

		RelayController relayConnection = new RelayController();
		try {
			relayConnection.runRelayConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
