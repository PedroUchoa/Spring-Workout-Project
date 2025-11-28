package com.pedro.workoutproject.dtos.workoutExerciseDtos;

import java.time.LocalDateTime;

public record CreateWorkoutExerciseDto(Integer weight,
                                       Integer sets,
                                       Integer reps,
                                       String notes,
                                       String workoutId,
                                       String exerciseName) {
}
