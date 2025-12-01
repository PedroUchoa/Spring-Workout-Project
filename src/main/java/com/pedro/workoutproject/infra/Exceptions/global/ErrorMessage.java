package com.pedro.workoutproject.infra.Exceptions.global;

import org.springframework.http.HttpStatus;

public record ErrorMessage(HttpStatus httpStatus, String message) {
}
