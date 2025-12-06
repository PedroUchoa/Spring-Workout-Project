package com.pedro.workoutproject.infra.Exceptions.global;

import com.pedro.workoutproject.infra.Exceptions.bodyWeightExceptions.BodyWeightNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.exercise.ExerciseNameNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.exercise.ExerciseNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.userExceptions.UserEmailNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.userExceptions.UserNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.userExceptions.UserWithEmailDuplicatedException;
import com.pedro.workoutproject.infra.Exceptions.workoutExceptions.WorkoutNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.workoutExerciseException.WorkoutExerciseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException ex){
        ErrorMessage threatResponse = new ErrorMessage(HttpStatus.NOT_FOUND,ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(UserWithEmailDuplicatedException.class)
    public ResponseEntity<ErrorMessage> handleUserWithEmailDuplicatedException(Exception ex){
        ErrorMessage threatResponse = new ErrorMessage(HttpStatus.CONFLICT,ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(threatResponse);
    }

    @ExceptionHandler(WorkoutNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleWorkoutNotFoundException(Exception ex){
        ErrorMessage threatResponse = new ErrorMessage(HttpStatus.NOT_FOUND,ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(BodyWeightNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleBodyWeightNotFoundException(Exception ex){
        ErrorMessage threatResponse = new ErrorMessage(HttpStatus.NOT_FOUND,ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(ExerciseNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleExerciseNotFoundException(Exception ex){
        ErrorMessage threatResponse = new ErrorMessage(HttpStatus.NOT_FOUND,ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(ExerciseNameNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleExerciseNameNotFoundException(Exception ex){
        ErrorMessage threatResponse = new ErrorMessage(HttpStatus.NOT_FOUND,ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(WorkoutExerciseNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleWorkoutExerciseNotFoundException(Exception ex){
        ErrorMessage threatResponse = new ErrorMessage(HttpStatus.NOT_FOUND,ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(UserEmailNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserEmailNotFoundException(Exception ex){
        ErrorMessage threatResponse = new ErrorMessage(HttpStatus.NOT_FOUND,ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> handleBadCredentialsException(Exception ex){
        ErrorMessage threatResponse = new ErrorMessage(HttpStatus.UNAUTHORIZED, "Login Or Password Incorrect, Please Try Again!");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(threatResponse);
    }


}
