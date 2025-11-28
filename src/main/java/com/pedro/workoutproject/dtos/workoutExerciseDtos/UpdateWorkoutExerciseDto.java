package com.pedro.workoutproject.dtos.workoutExerciseDtos;

public record UpdateWorkoutExerciseDto(Integer weight,
                                       Integer sets,
                                       Integer reps,
                                       String notes) {
}
