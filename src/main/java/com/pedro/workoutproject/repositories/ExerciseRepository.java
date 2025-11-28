package com.pedro.workoutproject.repositories;

import com.pedro.workoutproject.models.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise,String> {
    Exercise getExerciseByName(String exercise);
}
