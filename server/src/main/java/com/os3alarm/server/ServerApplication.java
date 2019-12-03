package com.os3alarm.server;

import com.os3alarm.server.services.RelayService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);

		RelayService relayConnection = new RelayService();
		relayConnection.runRelayConnection();
	}
}
