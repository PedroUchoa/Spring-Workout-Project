package com.pedro.workoutproject.infra.Exceptions.userExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User With Email For Loing Not Found In Database")
public class UserEmailNotFoundException extends RuntimeException{

    public UserEmailNotFoundException(String email){
        super("None User Registered with this Email: " + email + " Please Try Again Or Create An User");
    }

}
