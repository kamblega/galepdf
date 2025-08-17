package com.example.project.management.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectManagementWriteService {

	public static void main(String[] args) {
		SpringApplication.run( ProjectManagementWriteService.class, args);
		System.out.println("Project Management Service is running...");
	}

}
