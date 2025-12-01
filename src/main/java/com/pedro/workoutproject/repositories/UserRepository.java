package com.pedro.workoutproject.repositories;

import com.pedro.workoutproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM user WHERE email = :email LIMIT 1", nativeQuery = true)
    Optional<User> findAnyByEmailIncludingInactive(String email);

}
