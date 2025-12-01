package com.pedro.workoutproject.dtos.workoutDtos;

import com.pedro.workoutproject.dtos.userDtos.ReturnUserOnWorkoutDto;
import com.pedro.workoutproject.dtos.workoutExerciseDtos.ReturnWorkoutExerciseDto;
import com.pedro.workoutproject.models.User;
import com.pedro.workoutproject.models.Workout;

import java.time.LocalDateTime;
import java.util.List;

public record ReturnWorkoutDto(String id,
                               String notes,
                               LocalDateTime createdOn,
                               LocalDateTime updateOn,
                               LocalDateTime startedOn,
                               LocalDateTime finishedOn,
                               ReturnUserOnWorkoutDto user,
                               List<ReturnWorkoutExerciseDto> workoutExercise) {
    public ReturnWorkoutDto(Workout workout) {
        this(workout.getId(),
                workout.getNotes(),
                workout.getCreatedOn(),
                workout.getUpdateOn(),
                workout.getStartedOn(),
                workout.getFinishedOn(),
                new ReturnUserOnWorkoutDto(workout.getUserId()),
                workout.getWorkoutExerciseList().stream().map(ReturnWorkoutExerciseDto::new).toList());
    }


}
