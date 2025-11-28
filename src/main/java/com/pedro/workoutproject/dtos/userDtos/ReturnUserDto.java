package com.pedro.workoutproject.dtos.userDtos;

import com.pedro.workoutproject.dtos.bodyWeightDtos.ReturnBodyWeightDto;
import com.pedro.workoutproject.models.User;
import com.pedro.workoutproject.models.Workout;

import java.time.LocalDateTime;
import java.util.List;

public record ReturnUserDto(String id,
                            String email,
                            LocalDateTime createdOn,
                            LocalDateTime updateOn,
                            List<ReturnBodyWeightDto> weightList,
                            List<Workout> workoutList ) {

    public ReturnUserDto(User user){
        this(user.getId(),user.getEmail(),user.getCreatedOn(),user.getUpdateOn(),user.getWeightList().stream().map(ReturnBodyWeightDto::new).toList(),user.getWorkoutList());
    }

}
