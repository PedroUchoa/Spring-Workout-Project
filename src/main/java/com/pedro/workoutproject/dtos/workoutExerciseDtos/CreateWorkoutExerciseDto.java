package com.pedro.workoutproject.dtos.workoutExerciseDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateWorkoutExerciseDto(
        @NotNull(message = "Weight Is Required")
        Integer weight,
        @NotNull(message = "Sets Are Required")
        Integer sets,
        @NotNull(message = "Reps Are Required")
        Integer reps,
        @NotBlank(message = "Notes Are Required")
        String notes,
        @NotBlank(message = "WorkoutId Is Required")
        String workoutId,
        @NotBlank(message = "Exercise Name Is Required")
        String exerciseName) {
}
