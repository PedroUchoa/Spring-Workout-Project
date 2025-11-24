package com.pedro.workoutproject.enumerated;

public enum Role {

    ADMIN("admin"),
    USER("user");

    private final String role;

    Role(String role){
        this.role = role;
    }

    String getRole(){
        return role;
    }

}
