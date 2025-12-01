package com.pedro.workoutproject.infra.Exceptions.exercise;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Exercise Name Not Found In Database")
public class ExerciseNameNotFoundException extends RuntimeException{

    public ExerciseNameNotFoundException(String name){
        super("Exercise With Name: " + name+ " Not Found In Database, Please Try Again!");
    }
}
