package com.pedro.workoutproject.dtos.exerciseDtos;

import com.pedro.workoutproject.models.Exercise;

public record ReturnExerciseDto(String id,String name, String type) {
    public ReturnExerciseDto(Exercise exercise) {
        this(exercise.getId(), exercise.getName(), exercise.getType());
    }
}
