package com.pedro.workoutproject.services;

import com.pedro.workoutproject.dtos.workoutExerciseDtos.CreateWorkoutExerciseDto;
import com.pedro.workoutproject.dtos.workoutExerciseDtos.ReturnWorkoutExerciseDto;
import com.pedro.workoutproject.dtos.workoutExerciseDtos.UpdateWorkoutExerciseDto;
import com.pedro.workoutproject.infra.Exceptions.exercise.ExerciseNameNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.workoutExceptions.WorkoutNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.workoutExerciseException.WorkoutExerciseNotFoundException;
import com.pedro.workoutproject.models.Exercise;
import com.pedro.workoutproject.models.Workout;
import com.pedro.workoutproject.models.WorkoutExercise;
import com.pedro.workoutproject.repositories.ExerciseRepository;
import com.pedro.workoutproject.repositories.WorkoutExerciseRepository;
import com.pedro.workoutproject.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkoutExerciseService {

    @Autowired
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Transactional
    @CacheEvict(value = "workoutExercise", allEntries = true)
    public ReturnWorkoutExerciseDto createWorkoutExercise(CreateWorkoutExerciseDto createWorkoutExerciseDto){
        Workout workout = workoutRepository.findById(createWorkoutExerciseDto.workoutId()).orElseThrow(()->new WorkoutNotFoundException(createWorkoutExerciseDto.workoutId()));
        Exercise exercise = exerciseRepository.findByName(createWorkoutExerciseDto.exerciseName()).orElseThrow(()->new ExerciseNameNotFoundException(createWorkoutExerciseDto.exerciseName()));
        WorkoutExercise workoutExercise = workoutExerciseRepository.save(new WorkoutExercise(createWorkoutExerciseDto,workout,exercise));
        return new ReturnWorkoutExerciseDto(workoutExercise);
    }

    @Cacheable(value = "workoutExercise")
    public ReturnWorkoutExerciseDto findWorkoutExerciseById(String id){
        WorkoutExercise workoutExercise = workoutExerciseRepository.findById(id).orElseThrow(()->new WorkoutExerciseNotFoundException(id));
        return new ReturnWorkoutExerciseDto(workoutExercise);
    }

    @Cacheable(value = "workoutExercise")
    public Page<ReturnWorkoutExerciseDto> getAllWorkoutExercise(Pageable pageable){
        return workoutExerciseRepository.findAll(pageable).map(ReturnWorkoutExerciseDto::new);
    }

    @Transactional
    @CacheEvict(value = "workoutExercise", allEntries = true)
    public ReturnWorkoutExerciseDto updateWorkoutExercise(UpdateWorkoutExerciseDto updateWorkoutExerciseDto, String id){
        WorkoutExercise workoutExercise = workoutExerciseRepository.findById(id).orElseThrow(()-> new WorkoutExerciseNotFoundException(id));
        workoutExercise.update(updateWorkoutExerciseDto);
        workoutExerciseRepository.save(workoutExercise);
        return new ReturnWorkoutExerciseDto(workoutExercise);
    }

    @Transactional
    @CacheEvict(value = "workoutExercise", allEntries = true)
    public void deleteWorkoutExercise(String id){
        WorkoutExercise workoutExercise = workoutExerciseRepository.findById(id).orElseThrow(()->new WorkoutExerciseNotFoundException(id));
        workoutExercise.disable();
        workoutExerciseRepository.save(workoutExercise);
    }

}
