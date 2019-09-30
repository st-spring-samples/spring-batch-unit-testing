package com.sudhirt.practice.batch.transaction.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.sudhirt.practice.batch.transaction")
public class BatchJpaConfig {

}