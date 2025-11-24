package com.pedro.workoutproject.repositories;

import com.pedro.workoutproject.models.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<Workout,String> {
}
