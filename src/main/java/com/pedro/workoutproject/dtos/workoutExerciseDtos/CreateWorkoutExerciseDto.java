package com.pedro.workoutproject.dtos.workoutExerciseDtos;

public record CreateWorkoutExerciseDto(Integer weight,
                                       Integer sets,
                                       Integer reps,
                                       String notes,
                                       String workoutId,
                                       String exerciseName) {
}
