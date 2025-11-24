package com.pedro.workoutproject.repositories;

import com.pedro.workoutproject.models.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise,String> {
}
