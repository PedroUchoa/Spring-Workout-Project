package com.pedro.workoutproject.infra.Exceptions.userExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Trying to create an account with a email already in use")
public class UserWithEmailDuplicatedException extends RuntimeException{

    public UserWithEmailDuplicatedException(String email){
        super("The email: " + email + " Is Already In Use, Please Try Again Or Re-Enable Your Old Account!");
    }

}
