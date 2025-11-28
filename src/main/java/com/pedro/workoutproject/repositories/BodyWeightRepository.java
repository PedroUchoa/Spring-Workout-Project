package com.pedro.workoutproject.repositories;

import com.pedro.workoutproject.dtos.bodyWeightDtos.ReturnBodyWeightDto;
import com.pedro.workoutproject.models.BodyWeight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BodyWeightRepository extends JpaRepository<BodyWeight,String> {
    Page<BodyWeight> findAllByUserId(String userId, Pageable pageable);
}
