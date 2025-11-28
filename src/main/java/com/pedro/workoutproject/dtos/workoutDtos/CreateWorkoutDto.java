package com.pedro.workoutproject.dtos.workoutDtos;

import java.time.LocalDateTime;

public record CreateWorkoutDto(String notes,
                               LocalDateTime startedOn,
                               LocalDateTime finishedOn,
                               String userId) {
}
