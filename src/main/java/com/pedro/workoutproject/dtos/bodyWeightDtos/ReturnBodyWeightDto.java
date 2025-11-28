package com.pedro.workoutproject.dtos.bodyWeightDtos;

import com.pedro.workoutproject.models.BodyWeight;

import java.time.LocalDateTime;

public record ReturnBodyWeightDto(String id, Double value, LocalDateTime loggedOn) {
    public ReturnBodyWeightDto(BodyWeight bodyWeight) {
        this(bodyWeight.getId(), bodyWeight.getValue(), bodyWeight.getLoggedOn());
    }
}
