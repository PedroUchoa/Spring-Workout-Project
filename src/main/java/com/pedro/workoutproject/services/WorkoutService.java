package com.pedro.workoutproject.services;

import com.pedro.workoutproject.dtos.workoutDtos.CreateWorkoutDto;
import com.pedro.workoutproject.dtos.workoutDtos.ReturnWorkoutDto;
import com.pedro.workoutproject.dtos.workoutDtos.UpdateWorkoutDto;
import com.pedro.workoutproject.models.User;
import com.pedro.workoutproject.models.Workout;
import com.pedro.workoutproject.repositories.UserRepository;
import com.pedro.workoutproject.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ReturnWorkoutDto createWorkout(CreateWorkoutDto createWorkoutDto){
        User user = userRepository.findByIdAndIsActiveTrue(createWorkoutDto.userId()).orElseThrow(()->new RuntimeException("teste"));
        Workout workout =  workoutRepository.save(new Workout(createWorkoutDto,user));
        return new ReturnWorkoutDto(workout);
    }

    public ReturnWorkoutDto getWorkoutById(String id){
        Workout workout = workoutRepository.findByIdAndIsActiveTrue(id).orElseThrow(()->new RuntimeException("teste"));
        return new ReturnWorkoutDto(workout);
    }

    public Page<ReturnWorkoutDto> getAllWorkoutDto(Pageable pageable){
        return workoutRepository.findAllByIsActiveTrue(pageable).map(ReturnWorkoutDto::new);
    }

    @Transactional
    public ReturnWorkoutDto updateWorkoutDto(UpdateWorkoutDto updateWorkoutDto, String id){
        Workout workout = workoutRepository.findByIdAndIsActiveTrue(id).orElseThrow(()->new RuntimeException("teste"));
        workout.update(updateWorkoutDto);
        workoutRepository.save(workout);
        return new ReturnWorkoutDto(workout);
    }

    @Transactional
    public void deleteWorkoutDto(String id){
        Workout workout = workoutRepository.findByIdAndIsActiveTrue(id).orElseThrow(()->new RuntimeException("teste"));
        workout.disable();
        workoutRepository.save(workout);
    }




}
