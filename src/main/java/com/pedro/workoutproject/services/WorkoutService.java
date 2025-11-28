package com.pedro.workoutproject.services;

import com.pedro.workoutproject.dtos.workoutDtos.CreateWorkoutDto;
import com.pedro.workoutproject.dtos.workoutDtos.ReturnWorkoutDto;
import com.pedro.workoutproject.dtos.workoutDtos.UpdateWorkoutDto;
import com.pedro.workoutproject.dtos.workoutExerciseDtos.ReturnWorkoutExerciseDto;
import com.pedro.workoutproject.models.User;
import com.pedro.workoutproject.models.Workout;
import com.pedro.workoutproject.models.WorkoutExercise;
import com.pedro.workoutproject.repositories.UserRepository;
import com.pedro.workoutproject.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ReturnWorkoutDto createWorkout(CreateWorkoutDto createWorkoutDto){
        User user = userRepository.getReferenceById(createWorkoutDto.userId());
        Workout workout =  workoutRepository.save(new Workout(createWorkoutDto,user));
        return new ReturnWorkoutDto(workout);
    }

    public ReturnWorkoutDto getWorkoutById(String id){
        Workout workout = workoutRepository.getReferenceById(id);
        return new ReturnWorkoutDto(workout);
    }

    public Page<ReturnWorkoutDto> getAllWorkoutDto(Pageable pageable){
        return workoutRepository.findAll(pageable).map(ReturnWorkoutDto::new);
    }

    @Transactional
    public ReturnWorkoutDto updateWorkoutDto(UpdateWorkoutDto updateWorkoutDto, String id){
        Workout workout = workoutRepository.getReferenceById(id);
        workout.update(updateWorkoutDto);
        workoutRepository.save(workout);
        return new ReturnWorkoutDto(workout);
    }

    @Transactional
    public void deleteWorkoutDto(String id){
        Workout workout = workoutRepository.getReferenceById(id);
        workoutRepository.delete(workout);
    }




}
