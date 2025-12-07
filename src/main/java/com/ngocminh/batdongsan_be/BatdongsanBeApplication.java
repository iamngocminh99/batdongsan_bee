package com.ngocminh.batdongsan_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BatdongsanBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatdongsanBeApplication.class, args);
	}

}
