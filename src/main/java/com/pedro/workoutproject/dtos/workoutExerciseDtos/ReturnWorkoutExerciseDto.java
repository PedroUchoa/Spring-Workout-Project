package com.pedro.workoutproject.dtos.workoutExerciseDtos;

import com.pedro.workoutproject.dtos.exerciseDtos.ReturnExerciseDto;
import com.pedro.workoutproject.dtos.workoutDtos.ReturnWorkoutDto;
import com.pedro.workoutproject.models.WorkoutExercise;

import java.time.LocalDateTime;

public record ReturnWorkoutExerciseDto(
        String id,
        Integer weight,
        Integer sets,
        Integer reps,
        String notes,
        LocalDateTime createdOn,
        LocalDateTime updateOn,
        ReturnWorkoutDto workoutDto,
        ReturnExerciseDto exerciseDto
) {
    public ReturnWorkoutExerciseDto(WorkoutExercise workoutExercise) {
        this(workoutExercise.getId(), workoutExercise.getWeight(), workoutExercise.getSets(), workoutExercise.getReps(), workoutExercise.getNotes(), workoutExercise.getCreatedOn(),workoutExercise.getUpdateOn(), new ReturnWorkoutDto(workoutExercise.getWorkoutId()),new ReturnExerciseDto(workoutExercise.getExerciseId()));
    }
}
