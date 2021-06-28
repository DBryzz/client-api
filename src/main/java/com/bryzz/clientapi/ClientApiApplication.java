package com.bryzz.clientapi;

import com.bryzz.clientapi.domain.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class ClientApiApplication implements CommandLineRunner {
	private static Logger logger = LoggerFactory.getLogger(ClientApiApplication.class);

	@Resource
	FileStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(ClientApiApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
//		storageService.deleteAll();
		logger.info(System.getProperty("user.dir"));

		storageService.init();
	}

}
