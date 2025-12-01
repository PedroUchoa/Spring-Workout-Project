package com.pedro.workoutproject.dtos.userDtos;

import com.pedro.workoutproject.dtos.bodyWeightDtos.ReturnBodyWeightDto;
import com.pedro.workoutproject.dtos.workoutDtos.ReturnWorkoutDto;
import com.pedro.workoutproject.dtos.workoutDtos.ReturnWorkoutInUserDto;
import com.pedro.workoutproject.models.User;
import com.pedro.workoutproject.models.Workout;

import java.time.LocalDateTime;
import java.util.List;

public record ReturnUserDto(String id,
                            String email,
                            LocalDateTime createdOn,
                            LocalDateTime updateOn,
                            Boolean isActive,
                            LocalDateTime deleteOn,
                            List<ReturnBodyWeightDto> weightList,
                            List<ReturnWorkoutInUserDto> workoutList ) {

    public ReturnUserDto(User user){
        this(user.getId(),user.getEmail(),user.getCreatedOn(),user.getUpdateOn(), user.getIsActive(),user.getDeleteOn(),user.getWeightList().stream().map(ReturnBodyWeightDto::new).toList(),user.getWorkoutList().stream().map(ReturnWorkoutInUserDto::new).toList());
    }

}
