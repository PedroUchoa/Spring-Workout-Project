package com.pedro.workoutproject.infra.Exceptions.exercise;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Exercise Not Found in Database")
public class ExerciseNotFoundException extends RuntimeException{

    public ExerciseNotFoundException(String id){
        super("Exercise With Id: " + id + " Not Found In Database, Please Try Again!");
    }

}
