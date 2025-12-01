package com.pedro.workoutproject.infra.Exceptions.workoutExerciseException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Workout Exercise Not Found Database")
public class WorkoutExerciseNotFoundException extends RuntimeException{

    public WorkoutExerciseNotFoundException(String id){
        super("Workout Exercise With Id: " + id+ " Not Found in Database, Please Try Again!");
    }

}
