package com.os3alarm.server;

import com.os3alarm.server.services.RelayService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);

		RelayService relayConnection = new RelayService();
		try {
			relayConnection.runRelayConnection();
		/*} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

}
