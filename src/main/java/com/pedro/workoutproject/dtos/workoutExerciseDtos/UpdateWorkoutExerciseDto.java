package com.pedro.workoutproject.dtos.workoutExerciseDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateWorkoutExerciseDto(
        @NotNull(message = "Weight Is Required")
        Integer weight,
        @NotNull(message = "Sets Are Required")
        Integer sets,
        @NotNull(message = "Reps Are Required")
        Integer reps,
        @NotBlank(message = "Notes Are Required")
        String notes) {
}
