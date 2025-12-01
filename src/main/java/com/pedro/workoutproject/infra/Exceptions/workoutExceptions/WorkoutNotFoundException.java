package com.pedro.workoutproject.infra.Exceptions.workoutExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Workout Not Found In Database")
public class WorkoutNotFoundException extends RuntimeException{

    public WorkoutNotFoundException(String id){
        super("Workout With Id: " + id + " Not Found In Database, Please Try Again");
    }


}
