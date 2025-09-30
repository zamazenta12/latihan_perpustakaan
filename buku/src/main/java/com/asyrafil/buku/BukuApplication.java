package com.asyrafil.buku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class BukuApplication {

	public static void main(String[] args) {
		SpringApplication.run(BukuApplication.class, args);
	}

}