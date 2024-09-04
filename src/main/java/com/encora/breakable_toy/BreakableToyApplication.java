package com.encora.breakable_toy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BreakableToyApplication {
	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	public static void main(String[] args) {
		SpringApplication.run(BreakableToyApplication.class, args);
	}

}
