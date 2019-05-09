package com.songle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = "com.songle.mapper")
public class SpringbootShiroApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringbootShiroApplication.class, args);
	}

}
