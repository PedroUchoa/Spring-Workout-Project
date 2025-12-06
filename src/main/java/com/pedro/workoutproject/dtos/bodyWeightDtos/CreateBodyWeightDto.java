package com.pedro.workoutproject.dtos.bodyWeightDtos;


import java.time.LocalDateTime;

public record CreateBodyWeightDto(Double value, String userId, LocalDateTime loggedOn) {


}
