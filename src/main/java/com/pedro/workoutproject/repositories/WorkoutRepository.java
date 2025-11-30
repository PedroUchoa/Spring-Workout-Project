package com.pedro.workoutproject.repositories;

import com.pedro.workoutproject.models.Workout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout,String> {
    Optional<Workout> findByIdAndIsActiveTrue(String id);

    Page<Workout> findAllByIsActiveTrue(Pageable pageable);
}
