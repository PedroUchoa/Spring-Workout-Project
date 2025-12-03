package com.pedro.workoutproject.services;

import com.pedro.workoutproject.dtos.exerciseDtos.CreateExerciseDto;
import com.pedro.workoutproject.dtos.exerciseDtos.ReturnExerciseDto;
import com.pedro.workoutproject.infra.Exceptions.exercise.ExerciseNotFoundException;
import com.pedro.workoutproject.models.Exercise;
import com.pedro.workoutproject.repositories.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Transactional
    @CacheEvict(value = "exercise",allEntries = true)
    public ReturnExerciseDto createExercise(com.pedro.workoutproject.dtos.exerciseDtos.CreateExerciseDto createExerciseDto) {
        Exercise exercise = new Exercise(createExerciseDto);
        exerciseRepository.save(exercise);
        return new ReturnExerciseDto(exercise);
    }

    @Cacheable("exercise")
    public Page<ReturnExerciseDto> getAllExercises(Pageable pageable) {
        return exerciseRepository.findAll(pageable).map(ReturnExerciseDto::new);
    }

    @Cacheable("exercise")
    public ReturnExerciseDto getExerciseById(String id) {
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(()->new ExerciseNotFoundException(id));
        return new ReturnExerciseDto(exercise);
    }

    @Transactional
    @CacheEvict(value = "exercise",allEntries = true)
    public ReturnExerciseDto updateExercise(CreateExerciseDto createExerciseDto, String id){
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(()->new ExerciseNotFoundException(id));
        exercise.update(createExerciseDto);
        exerciseRepository.save(exercise);
        return new ReturnExerciseDto(exercise);
    }

    @Transactional
    @CacheEvict(value = "exercise",allEntries = true)
    public void deleteExercise(String id){
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(()->new ExerciseNotFoundException(id));
        exercise.disable();
        exerciseRepository.save(exercise);
    }


}
