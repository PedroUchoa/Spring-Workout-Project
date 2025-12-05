package com.pedro.workoutproject.services;

import com.pedro.workoutproject.dtos.exerciseDtos.CreateExerciseDto;
import com.pedro.workoutproject.dtos.exerciseDtos.ReturnExerciseDto;
import com.pedro.workoutproject.infra.Exceptions.exercise.ExerciseNotFoundException;
import com.pedro.workoutproject.models.Exercise;
import com.pedro.workoutproject.repositories.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseService exerciseService;

    private Exercise exercise;

    @BeforeEach
    void setUp() {
        exercise = new Exercise();
        exercise.setId("1");
        exercise.setType("teste");
        exercise.setName("teste");
        exercise.setIsActive(Boolean.TRUE);
    }


    @Test
    @DisplayName("Must Create a Exercise")
    void createExerciseSuccess() {
        CreateExerciseDto createExerciseDto = new CreateExerciseDto(this.exercise.getName(), this.exercise.getType());

        ReturnExerciseDto returnExerciseDto = exerciseService.createExercise(createExerciseDto);

        assertEquals(returnExerciseDto.name(), createExerciseDto.name());
        assertEquals(returnExerciseDto.type(), createExerciseDto.type());
        verify(exerciseRepository, times(1)).save(new Exercise(createExerciseDto));
    }

    @Test
    @DisplayName("Must Return All Exercises")
    void getaAllExercisesSuccess() {

        Pageable pageable = PageRequest.of(0, 1);
        Page<Exercise> mockPage = new PageImpl<>(List.of(exercise));

        when(exerciseRepository.findAll(pageable)).thenReturn(mockPage);

        Page<ReturnExerciseDto> exercisePage = exerciseService.getAllExercises(pageable);

        verify(exerciseRepository, times(1)).findAll(pageable);
        assertThat(exercisePage).usingRecursiveComparison().isEqualTo(mockPage);
    }

    @Test
    @DisplayName("Must Return Exercise By Id")
    void getExerciseByIdSuccess() {

        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));

        ReturnExerciseDto returnExerciseDto = exerciseService.getExerciseById(exercise.getId());

        assertEquals(new ReturnExerciseDto(exercise), returnExerciseDto);
        verify(exerciseRepository, times(1)).findById(exercise.getId());
    }

    @Test
    @DisplayName("Must Thrown Exception When Exercise Not Found By Id")
    void getExerciseByIdError() {

        ExerciseNotFoundException exception = assertThrows(ExerciseNotFoundException.class, () -> exerciseService.getExerciseById(exercise.getId()));

        assertEquals("Exercise With Id: " + exercise.getId() + " Not Found In Database, Please Try Again!", exception.getMessage());
        verify(exerciseRepository, times(1)).findById(exercise.getId());
    }

    @Test
    @DisplayName("Must Update Exercise")
    void updateExerciseSuccess() {
        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));

        CreateExerciseDto update = new CreateExerciseDto("novo", "novo");
        ReturnExerciseDto result = exerciseService.updateExercise(update, exercise.getId());

        assertEquals(result.type(), update.type());
        assertEquals(result.name(), update.name());
        verify(exerciseRepository, times(1)).findById(exercise.getId());
    }

    @Test
    @DisplayName("Must Thrown Exception When Exercise Not Found To Be Updated By Id")
    void updateExerciseError() {

        ExerciseNotFoundException exception = assertThrows(ExerciseNotFoundException.class, () -> exerciseService.updateExercise(new CreateExerciseDto("novo", "novo"), exercise.getId()));

        assertEquals("Exercise With Id: " + exercise.getId() + " Not Found In Database, Please Try Again!", exception.getMessage());
        verify(exerciseRepository, times(1)).findById(exercise.getId());
    }

    @Test
    @DisplayName("Must Delete A Exercise")
    void deleteExerciseSuccess() {

        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));
        exerciseService.deleteExercise(exercise.getId());

        ArgumentCaptor<Exercise> captor = ArgumentCaptor.forClass(Exercise.class);
        verify(exerciseRepository, times(1)).save(captor.capture());

        Exercise deletedExercise = captor.getValue();
        assertThat(deletedExercise.getIsActive()).isFalse();
        assertThat(deletedExercise.getDeleteOn()).isNotNull();
    }

    @Test
    @DisplayName("Must Thrown Exception When Exercise Not Found To Be Deleted By Id")
    void deleteExerciseError() {
        ExerciseNotFoundException exception = assertThrows(ExerciseNotFoundException.class, () -> exerciseService.deleteExercise(exercise.getId()));

        assertEquals("Exercise With Id: " + exercise.getId() + " Not Found In Database, Please Try Again!", exception.getMessage());
        verify(exerciseRepository, times(1)).findById(exercise.getId());
    }


}
