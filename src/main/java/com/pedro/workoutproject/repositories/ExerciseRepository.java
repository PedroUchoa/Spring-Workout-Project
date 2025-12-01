package com.pedro.workoutproject.repositories;

import com.pedro.workoutproject.models.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise,String> {
    Optional<Exercise> findByName(String name);
}
