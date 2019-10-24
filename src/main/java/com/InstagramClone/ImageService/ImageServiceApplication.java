package com.InstagramClone.ImageService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImageServiceApplication {

	public static void main(String[] args) {
		DatabaseController db = DatabaseController.getInstance();
		SpringApplication.run(ImageServiceApplication.class, args);
	}
}
