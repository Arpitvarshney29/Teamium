package com.teamium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class TeamiumApplication extends SpringBootServletInitializer {

	/**
	 * Called by SpringBoot framework configure the environment
	 * 
	 * @param application
	 * @return SpringApplicationBuilder
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TeamiumApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(TeamiumApplication.class, args);
	}
}
