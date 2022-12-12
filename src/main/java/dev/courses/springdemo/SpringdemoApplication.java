package dev.courses.springdemo;

import dev.courses.springdemo.assignment.injection.service.OutputAggregator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringdemoApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(SpringdemoApplication.class, args);
		context.getBean(OutputAggregator.class).printInput("Hello world");
	}

}
