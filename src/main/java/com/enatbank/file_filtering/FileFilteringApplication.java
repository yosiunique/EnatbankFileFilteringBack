package com.enatbank.file_filtering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FileFilteringApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileFilteringApplication.class, args);
	}

}
