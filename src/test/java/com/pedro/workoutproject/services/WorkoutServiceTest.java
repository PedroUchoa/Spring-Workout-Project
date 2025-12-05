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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WorkoutService workoutService;

    private User user;

    private Workout workout;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("1");
        user.setPassword("teste");
        user.setEmail("teste@gmail.com");
        user.setIsActive(Boolean.TRUE);

        workout = new Workout();
        workout.setId("1");
        workout.setIsActive(Boolean.TRUE);
        workout.setNotes("teste");
        workout.setFinishedOn(LocalDateTime.now());
        workout.setStartedOn(LocalDateTime.now());
        workout.setCreatedOn(LocalDateTime.now());
        workout.setUserId(user);
    }

    @Test
    @DisplayName("Must Create Workout With Success")
    void createWorkoutSuccess() {
        CreateWorkoutDto createWorkoutDto = new CreateWorkoutDto(workout.getNotes(), workout.getStartedOn(), workout.getFinishedOn(), "1");

        when(userRepository.findById(createWorkoutDto.userId())).thenReturn(Optional.of(user));
        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        ReturnWorkoutDto returnWorkoutDto = workoutService.createWorkout(createWorkoutDto);

        assertEquals(returnWorkoutDto.notes(), createWorkoutDto.notes());
        assertEquals(returnWorkoutDto.startedOn(), createWorkoutDto.startedOn());
        assertEquals(returnWorkoutDto.finishedOn(), createWorkoutDto.finishedOn());
        verify(workoutRepository, times(1)).save(new Workout(createWorkoutDto, user));
    }

    @Test
    @DisplayName("Must Throw Exception When No Found User By Id To Create Workout")
    void createWorkoutError() {
        CreateWorkoutDto createWorkoutDto = new CreateWorkoutDto(workout.getNotes(), workout.getStartedOn(), workout.getFinishedOn(), "1");

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> workoutService.createWorkout(createWorkoutDto));

        assertEquals("User with Id: " + user.getId() + " Not Found Or Deleted, Please Try Again!", exception.getMessage());
        verify(workoutRepository, never()).save(workout);
    }

    @Test
    @DisplayName("Must Return All Workouts")
    void returnAllWorkoutSuccess() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Workout> mockPage = new PageImpl<>(List.of(workout));

        when(workoutRepository.findAll(pageable)).thenReturn(mockPage);

        Page<ReturnWorkoutDto> workoutPage = workoutService.getAllWorkouts(pageable);

        verify(workoutRepository, times(1)).findAll(pageable);
        assertEquals(1, workoutPage.getTotalElements());
        assertEquals(workout.getId(), workoutPage.getContent().getFirst().id());
    }

    @Test
    @DisplayName("Must Return Workout By Id")
    void returnWorkoutById() {
        when(workoutRepository.findById(workout.getId())).thenReturn(Optional.of(workout));

        ReturnWorkoutDto workoutDto = workoutService.getWorkoutById(workout.getId());

        assertEquals(new ReturnWorkoutDto(workout), workoutDto);
        verify(workoutRepository, times(1)).findById(workout.getId());
    }

    @Test
    @DisplayName("Must Thrown Exception When Workout Not Found By Id")
    void getExerciseByIdError() {
        WorkoutNotFoundException exception = assertThrows(WorkoutNotFoundException.class, () -> workoutService.getWorkoutById(workout.getId()));

        assertEquals("Workout With Id: " + workout.getId() + " Not Found In Database, Please Try Again", exception.getMessage());
        verify(workoutRepository, times(1)).findById(workout.getId());
    }

    @Test
    @DisplayName("Must Update Workout")
    void updateWorkoutSuccess() {
        when(workoutRepository.findById(workout.getId())).thenReturn(Optional.of(workout));

        UpdateWorkoutDto update = new UpdateWorkoutDto("Nova Nota", LocalDateTime.now(), LocalDateTime.now().plusHours(5));
        ReturnWorkoutDto result = workoutService.updateWorkoutDto(update, workout.getId());

        assertEquals(result.notes(), update.notes());
        verify(workoutRepository, times(1)).findById(workout.getId());
        verify(workoutRepository, times(1)).save(workout);
    }

    @Test
    @DisplayName("Must Thrown Exception When Workout Not Found To Be Updated By Id")
    void updateWorkoutError() {
        UpdateWorkoutDto update = new UpdateWorkoutDto("Nova Nota", LocalDateTime.now(), LocalDateTime.now().plusHours(5));

        WorkoutNotFoundException exception = assertThrows(WorkoutNotFoundException.class, () -> workoutService.updateWorkoutDto(update, workout.getId()));

        assertEquals("Workout With Id: " + workout.getId() + " Not Found In Database, Please Try Again", exception.getMessage());
        verify(workoutRepository, times(1)).findById(workout.getId());
        verify(workoutRepository, never()).save(workout);
    }

    @Test
    @DisplayName("Must Delete A Workout")
    void deleteWorkoutSuccess() {

        when(workoutRepository.findById(workout.getId())).thenReturn(Optional.of(workout));
        workoutService.deleteWorkout(workout.getId());

        ArgumentCaptor<Workout> captor = ArgumentCaptor.forClass(Workout.class);
        verify(workoutRepository, times(1)).save(captor.capture());

        verify(workoutRepository, times(1)).save(workout);
        Workout deletedExercise = captor.getValue();
        assertThat(deletedExercise.getIsActive()).isFalse();
        assertThat(deletedExercise.getDeleteOn()).isNotNull();
    }

    @Test
    @DisplayName("Must Thrown Exception When Workout Not Found To Be Deleted By Id")
    void deleteExerciseError() {
        WorkoutNotFoundException exception = assertThrows(WorkoutNotFoundException.class, () -> workoutService.deleteWorkout(workout.getId()));

        assertEquals("Workout With Id: " + workout.getId() + " Not Found In Database, Please Try Again", exception.getMessage());
        verify(workoutRepository, never()).save(workout);
        verify(workoutRepository, times(1)).findById(workout.getId());
    }

}
