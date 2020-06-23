package com.son.mzzb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MzzbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MzzbApplication.class, args);
	}

}
