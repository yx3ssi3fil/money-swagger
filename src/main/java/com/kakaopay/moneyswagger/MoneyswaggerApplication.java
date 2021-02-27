package com.kakaopay.moneyswagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MoneyswaggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyswaggerApplication.class, args);
	}
}
