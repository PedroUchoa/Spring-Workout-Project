package com.pedro.workoutproject.dtos.userDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDto(
        @NotBlank(message = "Email Is Required")
        @Email(message = "Email Format Invalid")
        String email,
        @NotBlank(message = "Password Is Required")
        String password) {
}
