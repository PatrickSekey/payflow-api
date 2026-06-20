// src/main/java/com/payflow/PayFlowApplication.java
package com.payflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for PayFlow API.
 * This is the entry point for the Spring Boot application.
 *
 * @SpringBootApplication is a convenience annotation that adds:
 * - @Configuration: Tags the class as a source of bean definitions
 * - @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings
 * - @ComponentScan: Tells Spring to look for other components, configurations, and services
 */
@SpringBootApplication
public class PayFlowApiApplication {

	public static void main(String[] args) {
		// SpringApplication.run() bootstraps the application
		// It creates an ApplicationContext, starts the embedded server, and auto-configures the application
		SpringApplication.run(PayFlowApiApplication.class, args);
	}
}