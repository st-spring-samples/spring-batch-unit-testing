package com.sudhirt.practice.batch.accountservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.sudhirt.practice.batch.accountservice")
public class JpaConfig {

}