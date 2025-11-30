package com.pedro.workoutproject.repositories;

import com.pedro.workoutproject.models.WorkoutExercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise,String> {
    Optional<WorkoutExercise> findByIdAndIsActiveTrue(String id);

    Page<WorkoutExercise> findAllByIsActiveTrue(Pageable pageable);
}
