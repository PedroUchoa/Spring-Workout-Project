package com.pedro.workoutproject.repositories;

import com.pedro.workoutproject.models.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise,String> {
    Exercise getExerciseByName(String exercise);

    Page<Exercise> findAllByIsActiveTrue(Pageable pageable);

    Optional<Exercise> findByIdAndIsActiveTrue(String id);

    Optional<Exercise> getExerciseByNameAndIsActiveTrue(String s);
}
