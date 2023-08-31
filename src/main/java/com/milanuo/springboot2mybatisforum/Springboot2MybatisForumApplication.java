package com.milanuo.springboot2mybatisforum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.milanuo.springboot2mybatisforum.module")
@EnableCaching
public class Springboot2MybatisForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(Springboot2MybatisForumApplication.class, args);
	}
}
