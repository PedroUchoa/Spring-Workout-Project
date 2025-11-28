package com.pedro.workoutproject.dtos.workoutDtos;

import java.time.LocalDateTime;

public record UpdateWorkoutDto(String notes,
                               LocalDateTime startedOn,
                               LocalDateTime finishedOn) {
}
