package com.sudhirt.practice.batch.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		scanBasePackages = { "com.sudhirt.practice.batch.transaction", "com.sudhirt.practice.batch.accountservice" })
public class TransactionBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionBatchApplication.class, args);
	}

}
