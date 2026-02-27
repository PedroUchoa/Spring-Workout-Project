package com.pedro.workoutproject.dtos.bodyWeightDtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateBodyWeightDto(
        @NotNull(message = "Value Is Required")
        Double value,
        @NotBlank(message = "User Id Is Required")
        String userId,
        @NotNull(message = "Logged On Is Required")
        LocalDateTime loggedOn) {


}
