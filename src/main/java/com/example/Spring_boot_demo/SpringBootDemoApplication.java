package com.example.Spring_boot_demo;

import com.example.Spring_boot_demo.assignment.injection.service.OutputAggregator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootDemoApplication {

	public static void main(String[] args) {

		var context = SpringApplication.run(SpringBootDemoApplication.class, args);
		context.getBean(OutputAggregator.class).printInput("Hello world");
	}

}
