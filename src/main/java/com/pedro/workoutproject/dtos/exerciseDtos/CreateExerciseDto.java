package com.pedro.workoutproject.dtos.exerciseDtos;

import jakarta.validation.constraints.NotBlank;

public record CreateExerciseDto(
        @NotBlank(message = "Name Is Required")
        String name,
        @NotBlank(message = "Type Is Required")
        String type) {
}
