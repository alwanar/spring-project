package com.example.jdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


import java.util.Properties;

@SpringBootApplication
//@EnableJpaRepositories(basePackages="com.example.jdbc.repository")
//@EntityScan(basePackages = "com.example.jdbc.entity")
public class SpringJdbcApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringJdbcApplication.class, args);
	}

}
