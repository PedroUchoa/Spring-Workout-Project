package com.pedro.workoutproject.dtos.workoutDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateWorkoutDto(
        @NotBlank(message = "Notes Are Required")
        String notes,
        @NotNull(message = "Started On Is Required")
        LocalDateTime startedOn,
        @NotNull(message = "Finished On Is Required")
        LocalDateTime finishedOn) {
}
