package com.pedro.workoutproject.services;

import com.pedro.workoutproject.dtos.exerciseDtos.CreateExerciseDto;
import com.pedro.workoutproject.dtos.exerciseDtos.ReturnExerciseDto;
import com.pedro.workoutproject.models.Exercise;
import com.pedro.workoutproject.repositories.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Transactional
    public ReturnExerciseDto createExercise(com.pedro.workoutproject.dtos.exerciseDtos.CreateExerciseDto createExerciseDto) {
        Exercise exercise = new Exercise(createExerciseDto);
        exerciseRepository.save(exercise);
        return new ReturnExerciseDto(exercise);
    }

    public Page<ReturnExerciseDto> getAllExerciser(Pageable pageable) {
        return exerciseRepository.findAll(pageable).map(ReturnExerciseDto::new);
    }

    public ReturnExerciseDto getExerciseById(String id) {
        Exercise exercise = exerciseRepository.getReferenceById(id);
        return new ReturnExerciseDto(exercise);
    }

    @Transactional
    public ReturnExerciseDto updateExercise(CreateExerciseDto createExerciseDto, String id){
        Exercise exercise = exerciseRepository.getReferenceById(id);
        exercise.update(createExerciseDto);
        exerciseRepository.save(exercise);
        return new ReturnExerciseDto(exercise);
    }

    @Transactional
    public void deleteExercise(String id){
        Exercise exercise = exerciseRepository.getReferenceById(id);
        exerciseRepository.delete(exercise);
    }


}
