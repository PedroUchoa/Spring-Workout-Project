package com.pedro.workoutproject.services;

import com.pedro.workoutproject.dtos.workoutDtos.CreateWorkoutDto;
import com.pedro.workoutproject.dtos.workoutDtos.ReturnWorkoutDto;
import com.pedro.workoutproject.dtos.workoutDtos.UpdateWorkoutDto;
import com.pedro.workoutproject.infra.Exceptions.userExceptions.UserNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.workoutExceptions.WorkoutNotFoundException;
import com.pedro.workoutproject.models.User;
import com.pedro.workoutproject.models.Workout;
import com.pedro.workoutproject.repositories.UserRepository;
import com.pedro.workoutproject.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @CacheEvict(value = "workout", allEntries = true)
    public ReturnWorkoutDto createWorkout(CreateWorkoutDto createWorkoutDto){
        User user = userRepository.findById(createWorkoutDto.userId()).orElseThrow(()->new UserNotFoundException(createWorkoutDto.userId()));
        Workout workout =  workoutRepository.save(new Workout(createWorkoutDto,user));
        return new ReturnWorkoutDto(workout);
    }

    @Cacheable(value = "workout")
    public Page<ReturnWorkoutDto> getAllWorkouts(Pageable pageable){
        return workoutRepository.findAll(pageable).map(ReturnWorkoutDto::new);
    }

    @Cacheable(value = "workout")
    public ReturnWorkoutDto getWorkoutById(String id){
        Workout workout = workoutRepository.findById(id).orElseThrow(()->new WorkoutNotFoundException(id));
        return new ReturnWorkoutDto(workout);
    }

    @Transactional
    @CacheEvict(value = "workout", allEntries = true)
    public ReturnWorkoutDto updateWorkoutDto(UpdateWorkoutDto updateWorkoutDto, String id){
        Workout workout = workoutRepository.findById(id).orElseThrow(()->new WorkoutNotFoundException(id));
        workout.update(updateWorkoutDto);
        workoutRepository.save(workout);
        return new ReturnWorkoutDto(workout);
    }

    @Transactional
    @CacheEvict(value = "workout", allEntries = true)
    public void deleteWorkout(String id){
        Workout workout = workoutRepository.findById(id).orElseThrow(()->new WorkoutNotFoundException(id));
        workout.disable();
        workoutRepository.save(workout);
    }




}
