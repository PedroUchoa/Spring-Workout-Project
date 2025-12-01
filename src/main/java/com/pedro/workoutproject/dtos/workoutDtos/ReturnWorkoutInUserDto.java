package com.pedro.workoutproject.dtos.workoutDtos;

import com.pedro.workoutproject.dtos.workoutExerciseDtos.ReturnWorkoutExerciseDto;
import com.pedro.workoutproject.models.Workout;

import java.time.LocalDateTime;
import java.util.List;

public record ReturnWorkoutInUserDto(String id,
                                     String notes,
                                     LocalDateTime createdOn,
                                     LocalDateTime updateOn,
                                     LocalDateTime startedOn,
                                     LocalDateTime finishedOn,
                                     List<ReturnWorkoutExerciseDto> workoutExercise) {

    public ReturnWorkoutInUserDto(Workout workout){
        this(workout.getId(),
                workout.getNotes(),
                workout.getCreatedOn(),
                workout.getUpdateOn(),
                workout.getStartedOn(),
                workout.getFinishedOn(),
                workout.getWorkoutExerciseList().stream().map(ReturnWorkoutExerciseDto::new).toList());

    }

}
