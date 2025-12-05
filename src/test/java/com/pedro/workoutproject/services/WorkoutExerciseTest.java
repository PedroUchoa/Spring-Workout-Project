package com.pedro.workoutproject.services;

import com.pedro.workoutproject.dtos.workoutExerciseDtos.CreateWorkoutExerciseDto;
import com.pedro.workoutproject.dtos.workoutExerciseDtos.ReturnWorkoutExerciseDto;
import com.pedro.workoutproject.dtos.workoutExerciseDtos.UpdateWorkoutExerciseDto;
import com.pedro.workoutproject.infra.Exceptions.exercise.ExerciseNameNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.workoutExceptions.WorkoutNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.workoutExerciseException.WorkoutExerciseNotFoundException;
import com.pedro.workoutproject.models.Exercise;
import com.pedro.workoutproject.models.User;
import com.pedro.workoutproject.models.Workout;
import com.pedro.workoutproject.models.WorkoutExercise;
import com.pedro.workoutproject.repositories.ExerciseRepository;
import com.pedro.workoutproject.repositories.WorkoutExerciseRepository;
import com.pedro.workoutproject.repositories.WorkoutRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutExerciseTest {

    @Mock
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private WorkoutExerciseService workoutExerciseService;

    private WorkoutExercise workoutExercise;
    private Workout workout;
    private Exercise exercise;

    @BeforeEach
    void setUp() {
        exercise = new Exercise();
        exercise.setId("1");
        exercise.setType("teste");
        exercise.setName("teste");
        exercise.setIsActive(Boolean.TRUE);

        workout = new Workout();
        workout.setId("1");
        workout.setIsActive(Boolean.TRUE);
        workout.setNotes("teste");
        workout.setFinishedOn(LocalDateTime.now());
        workout.setStartedOn(LocalDateTime.now());
        workout.setCreatedOn(LocalDateTime.now());
        workout.setUserId(new User());

        workoutExercise = new WorkoutExercise();
        workoutExercise.setId("1");
        workoutExercise.setExerciseId(exercise);
        workoutExercise.setNotes("teste");
        workoutExercise.setIsActive(Boolean.TRUE);
        workoutExercise.setReps(8);
        workoutExercise.setSets(8);
        workoutExercise.setCreatedOn(LocalDateTime.now());
        workoutExercise.setWeight(8);
    }

    @Test
    @DisplayName("Must Create a WorkoutExercise")
    void createWorkoutExerciseSuccess() {
        CreateWorkoutExerciseDto createWorkoutExerciseDto = new CreateWorkoutExerciseDto(8, 8, 8, "teste", "1", "1");

        when(workoutRepository.findById(createWorkoutExerciseDto.workoutId())).thenReturn(Optional.of(workout));
        when(exerciseRepository.findByName(createWorkoutExerciseDto.exerciseName())).thenReturn(Optional.of(exercise));
        when(workoutExerciseRepository.save(any(WorkoutExercise.class))).thenReturn(workoutExercise);

        ReturnWorkoutExerciseDto returnWorkoutExerciseDto = workoutExerciseService.createWorkoutExercise(createWorkoutExerciseDto);

        assertEquals(returnWorkoutExerciseDto.reps(), createWorkoutExerciseDto.reps());
        assertEquals(returnWorkoutExerciseDto.sets(), createWorkoutExerciseDto.sets());
        assertEquals(returnWorkoutExerciseDto.weight(), createWorkoutExerciseDto.weight());
        assertEquals(returnWorkoutExerciseDto.notes(), createWorkoutExerciseDto.notes());
        verify(workoutExerciseRepository, times(1)).save(new WorkoutExercise(createWorkoutExerciseDto, workout, exercise));
    }

    @Test
    @DisplayName("Must Throw Exception For Trying To Create WorkoutExercise With Workout Id Not Found")
    void createWorkoutExerciseError() {
        CreateWorkoutExerciseDto createWorkoutExerciseDto = new CreateWorkoutExerciseDto(8, 8, 8, "teste", "1", "1");

        WorkoutNotFoundException exception = assertThrows(WorkoutNotFoundException.class, () -> workoutExerciseService.createWorkoutExercise(createWorkoutExerciseDto));

        assertEquals("Workout With Id: " + workoutExercise.getId() + " Not Found In Database, Please Try Again", exception.getMessage());
        verify(workoutRepository, times(1)).findById(workout.getId());
        verify(workoutExerciseRepository, never()).save(workoutExercise);
    }


    @Test
    @DisplayName("Must Throw Exception For Trying To Create WorkoutExercise With Exercise Name Not Found")
    void createWorkoutExerciseErrorTwo() {
        CreateWorkoutExerciseDto createWorkoutExerciseDto = new CreateWorkoutExerciseDto(8, 8, 8, "teste", "1", "1");

        when(workoutRepository.findById(createWorkoutExerciseDto.workoutId())).thenReturn(Optional.of(workout));

        ExerciseNameNotFoundException exception = assertThrows(ExerciseNameNotFoundException.class, () -> workoutExerciseService.createWorkoutExercise(createWorkoutExerciseDto));

        assertEquals("Exercise With Name: " + createWorkoutExerciseDto.exerciseName() + " Not Found In Database, Please Try Again!", exception.getMessage());
        verify(exerciseRepository, times(1)).findByName(createWorkoutExerciseDto.exerciseName());
        verify(workoutExerciseRepository, never()).save(workoutExercise);
    }

    @Test
    @DisplayName("Must Return WorkoutExercise By Id")
    void returnWorkoutExerciseByIDSuccess() {
        when(workoutExerciseRepository.findById(workoutExercise.getId())).thenReturn(Optional.of(workoutExercise));

        ReturnWorkoutExerciseDto returnWorkoutExerciseDto = workoutExerciseService.findWorkoutExerciseById(workoutExercise.getId());

        assertEquals(new ReturnWorkoutExerciseDto(workoutExercise), returnWorkoutExerciseDto);
        verify(workoutExerciseRepository, times(1)).findById(workoutExercise.getId());
    }

    @Test
    @DisplayName("Must Throw Exception Trying To Return WorkoutExercise By Id Not Found")
    void returnWorkoutExerciseByIDError() {
        WorkoutExerciseNotFoundException exception = assertThrows(WorkoutExerciseNotFoundException.class, () -> workoutExerciseService.findWorkoutExerciseById(workout.getId()));

        assertEquals("Workout Exercise With Id: " + workoutExercise.getId() + " Not Found in Database, Please Try Again!", exception.getMessage());
        verify(workoutExerciseRepository, times(1)).findById(workoutExercise.getId());
    }

    @Test
    @DisplayName("Must Return All WorkoutExercise")
    void returnAllWorkoutExerciseSuccess() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<WorkoutExercise> mockPage = new PageImpl<>(List.of(workoutExercise));

        when(workoutExerciseRepository.findAll(pageable)).thenReturn(mockPage);

        Page<ReturnWorkoutExerciseDto> result = workoutExerciseService.getAllWorkoutExercise(pageable);

        verify(workoutExerciseRepository, times(1)).findAll(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(workoutExercise.getId(), result.getContent().getFirst().id());
    }

    @Test
    @DisplayName("Must Update WorkoutExecise")
    void updateWorkoutExerciseSuccess() {
        UpdateWorkoutExerciseDto update = new UpdateWorkoutExerciseDto(1, 1, 1, "update");

        when(workoutExerciseRepository.findById(workoutExercise.getId())).thenReturn(Optional.of(workoutExercise));

        ReturnWorkoutExerciseDto result = workoutExerciseService.updateWorkoutExercise(update, workoutExercise.getId());

        assertEquals(result.notes(), update.notes());
        assertEquals(result.weight(), update.weight());
        assertEquals(result.reps(), update.reps());
        assertEquals(result.sets(), update.sets());
        verify(workoutExerciseRepository, times(1)).findById(workoutExercise.getId());
        verify(workoutExerciseRepository, times(1)).save(any(WorkoutExercise.class));
    }

    @Test
    @DisplayName("Must Thrown Exception When Trying To Update WorkoutExercise With Id Not Found")
    void updateWorkoutExerciseError() {
        UpdateWorkoutExerciseDto update = new UpdateWorkoutExerciseDto(1, 1, 1, "update");
        WorkoutExerciseNotFoundException exception = assertThrows(WorkoutExerciseNotFoundException.class, () -> workoutExerciseService.updateWorkoutExercise(update, workoutExercise.getId()));

        assertEquals("Workout Exercise With Id: " + workoutExercise.getId() + " Not Found in Database, Please Try Again!", exception.getMessage());
        verify(workoutExerciseRepository, times(1)).findById(workoutExercise.getId());
        verify(workoutExerciseRepository, never()).save(workoutExercise);
    }

    @Test
    @DisplayName("Must Delete Workout Exercise")
    void deleteWorkoutExerciseSuccess() {
        when(workoutExerciseRepository.findById(workoutExercise.getId())).thenReturn(Optional.of(workoutExercise));
        workoutExerciseService.deleteWorkoutExercise(workoutExercise.getId());

        ArgumentCaptor<WorkoutExercise> captor = ArgumentCaptor.forClass(WorkoutExercise.class);
        verify(workoutExerciseRepository, times(1)).save(captor.capture());

        WorkoutExercise deletedWorkoutExercise = captor.getValue();
        assertThat(deletedWorkoutExercise.getIsActive()).isFalse();
        assertThat(deletedWorkoutExercise.getDeleteOn()).isNotNull();
    }

    @Test
    @DisplayName("Must Thrown Exception When Trying To Delete WorkoutExcercise By Id Not Found")
    void deleteWorkoutExerciseError() {
        WorkoutExerciseNotFoundException exception = assertThrows(WorkoutExerciseNotFoundException.class, () -> workoutExerciseService.deleteWorkoutExercise(workoutExercise.getId()));

        assertEquals("Workout Exercise With Id: " + workoutExercise.getId() + " Not Found in Database, Please Try Again!", exception.getMessage());
        verify(workoutExerciseRepository, times(1)).findById(workoutExercise.getId());
        verify(workoutExerciseRepository, never()).save(workoutExercise);
    }

}
