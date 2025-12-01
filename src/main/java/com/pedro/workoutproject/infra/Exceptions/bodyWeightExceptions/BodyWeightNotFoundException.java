package com.pedro.workoutproject.infra.Exceptions.bodyWeightExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Body Weight Not Found in Database")
public class BodyWeightNotFoundException extends RuntimeException{

    public BodyWeightNotFoundException(String id){
        super("Body Weight With Id: " + id + " Not Found In Database, Please Try Again!");
    }

}
