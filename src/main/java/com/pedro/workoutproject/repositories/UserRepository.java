package com.pedro.workoutproject.repositories;

import com.pedro.workoutproject.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndIsActiveTrue(String id);

    Page<User> findAllByIsActiveTrue(Pageable pageable);
}
