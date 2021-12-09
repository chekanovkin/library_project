package com.project.library_project;

import com.project.library_project.config.RabbitConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.util.logging.LogManager;

@Import(RabbitConfig.class)
@ComponentScan
@SpringBootApplication
public class LibraryProjectApplication {

	public static void main(String[] args) {
		try {
			LogManager.getLogManager().readConfiguration(
				LibraryProjectApplication.class.getResourceAsStream("/logging.properties"));
		} catch (IOException e) {
			System.err.println("Could not setup logger configuration: " + e.toString());
		}
		SpringApplication.run(LibraryProjectApplication.class, args);
	}

}
