package com.pedro.workoutproject.infra.Exceptions.userExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User Not Found in Database")
public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String id){
        super("User with Id: " + id + " Not Found Or Deleted, Please Try Again!");
    }

}
