package com.pedro.workoutproject.dtos.userDtos;

import com.pedro.workoutproject.models.User;

import java.time.LocalDateTime;

public record ReturnUserOnWorkoutDto(String id,
                                     String email,
                                     LocalDateTime createdOn,
                                     LocalDateTime updateOn
                                     ) {

    public ReturnUserOnWorkoutDto(User user){
        this(user.getId(),user.getEmail(),user.getCreatedOn(),user.getUpdateOn());
    }

}
