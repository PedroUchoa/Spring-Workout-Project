package com.pedro.workoutproject.dtos.bodyWeightDtos;

import jakarta.validation.constraints.NotNull;

public record UpdateBodyWeightDto(
        @NotNull(message = "Value Is Required")
        Double value
) {}
