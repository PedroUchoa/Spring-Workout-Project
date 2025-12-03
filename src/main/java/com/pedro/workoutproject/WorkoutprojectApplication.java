package com.pedro.workoutproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class WorkoutprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkoutprojectApplication.class, args);
	}

}
