package com.pedro.workoutproject.repositories;

import com.pedro.workoutproject.models.BodyWeight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BodyWeightRepository extends JpaRepository<BodyWeight,String> {
}
