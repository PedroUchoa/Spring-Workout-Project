package com.pedro.workoutproject.services;

import com.pedro.workoutproject.dtos.workoutExerciseDtos.CreateWorkoutExerciseDto;
import com.pedro.workoutproject.dtos.workoutExerciseDtos.ReturnWorkoutExerciseDto;
import com.pedro.workoutproject.dtos.workoutExerciseDtos.UpdateWorkoutExerciseDto;
import com.pedro.workoutproject.models.Exercise;
import com.pedro.workoutproject.models.Workout;
import com.pedro.workoutproject.models.WorkoutExercise;
import com.pedro.workoutproject.repositories.ExerciseRepository;
import com.pedro.workoutproject.repositories.WorkoutExerciseRepository;
import com.pedro.workoutproject.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkoutExerciseService {

    @Autowired
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Transactional
    public ReturnWorkoutExerciseDto createWorkoutExercise(CreateWorkoutExerciseDto createWorkoutExerciseDto){
        Workout workout = workoutRepository.findByIdAndIsActiveTrue(createWorkoutExerciseDto.workoutId()).orElseThrow(()->new RuntimeException("teste"));
        Exercise exercise = exerciseRepository.getExerciseByNameAndIsActiveTrue(createWorkoutExerciseDto.exerciseName()).orElseThrow(()->new RuntimeException("teste"));
        WorkoutExercise workoutExercise = workoutExerciseRepository.save(new WorkoutExercise(createWorkoutExerciseDto,workout,exercise));
        return new ReturnWorkoutExerciseDto(workoutExercise);
    }

    public ReturnWorkoutExerciseDto findWorkoutExerciseById(String id){
        WorkoutExercise workoutExercise = workoutExerciseRepository.findByIdAndIsActiveTrue(id).orElseThrow(()->new RuntimeException("teste"));
        return new ReturnWorkoutExerciseDto(workoutExercise);
    }

    public Page<ReturnWorkoutExerciseDto> getAllWorkoutExercise(Pageable pageable){
        return workoutExerciseRepository.findAllByIsActiveTrue(pageable).map(ReturnWorkoutExerciseDto::new);
    }

    @Transactional
    public ReturnWorkoutExerciseDto updateWorkoutExercise(UpdateWorkoutExerciseDto updateWorkoutExerciseDto, String id){
        WorkoutExercise workoutExercise = workoutExerciseRepository.findByIdAndIsActiveTrue(id).orElseThrow(()->new RuntimeException("teste"));
        workoutExercise.update(updateWorkoutExerciseDto);
        workoutExerciseRepository.save(workoutExercise);
        return new ReturnWorkoutExerciseDto(workoutExercise);
    }

    @Transactional
    public void deleteWorkoutExercise(String id){
        WorkoutExercise workoutExercise = workoutExerciseRepository.findByIdAndIsActiveTrue(id).orElseThrow(()->new RuntimeException("teste"));
        workoutExercise.disable();
        workoutExerciseRepository.save(workoutExercise);
    }

}
