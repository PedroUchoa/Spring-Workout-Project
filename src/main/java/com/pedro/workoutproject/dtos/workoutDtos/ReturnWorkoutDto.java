package com.pedro.workoutproject.dtos.workoutDtos;

import com.pedro.workoutproject.dtos.userDtos.ReturnUserDto;
import com.pedro.workoutproject.dtos.workoutExerciseDtos.ReturnWorkoutExerciseDto;
import com.pedro.workoutproject.models.Workout;

import java.time.LocalDateTime;
import java.util.List;

public record ReturnWorkoutDto(String id,
                               String notes,
                               LocalDateTime createdOn,
                               LocalDateTime updateOn,
                               LocalDateTime startedOn,
                               LocalDateTime finishedOn,
                               ReturnUserDto user,
                               List<ReturnWorkoutExerciseDto> workoutExerciseDtos) {
    public ReturnWorkoutDto(Workout workout) {
        this(workout.getId(),
                workout.getNotes(),
                workout.getCreatedOn(),
                workout.getUpdateOn(),
                workout.getStartedOn(),
                workout.getFinishedOn(),
                new ReturnUserDto(workout.getUserId()),
                workout.getWorkoutExerciseList().stream().map(ReturnWorkoutExerciseDto::new).toList());
    }


}
