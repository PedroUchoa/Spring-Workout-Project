package com.pedro.workoutproject.repositories;

import com.pedro.workoutproject.models.BodyWeight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BodyWeightRepository extends JpaRepository<BodyWeight,String> {
    Page<BodyWeight> findAllByUserIdId(String id, Pageable pageable);


    Optional<BodyWeight> findBodyWeightByIdAndIsActiveTrue(String id);
}
