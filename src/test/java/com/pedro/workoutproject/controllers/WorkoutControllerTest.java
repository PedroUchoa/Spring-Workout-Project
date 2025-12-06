package com.pedro.workoutproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pedro.workoutproject.dtos.workoutDtos.CreateWorkoutDto;
import com.pedro.workoutproject.dtos.workoutDtos.ReturnWorkoutDto;
import com.pedro.workoutproject.dtos.workoutDtos.UpdateWorkoutDto;
import com.pedro.workoutproject.infra.Exceptions.userExceptions.UserNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.workoutExceptions.WorkoutNotFoundException;
import com.pedro.workoutproject.models.User;
import com.pedro.workoutproject.models.Workout;
import com.pedro.workoutproject.services.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class WorkoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private WorkoutService workoutService;

    private Workout workout;

    @BeforeEach
    void setUp(){
        objectMapper.registerModule(new JavaTimeModule());
        workout = new Workout();
        workout.setId("1");
        workout.setIsActive(Boolean.TRUE);
        workout.setNotes("teste");
        workout.setFinishedOn(LocalDateTime.now());
        workout.setStartedOn(LocalDateTime.now());
        workout.setCreatedOn(LocalDateTime.now());
        User user = new User();
        user.setId("1");
        workout.setUserId(user);
    }

    @Test
    @DisplayName("Must Return Status 201 Created When Create workout")
    void createWorkoutSuccess() throws Exception {
        CreateWorkoutDto dto = new CreateWorkoutDto(workout.getNotes(),LocalDateTime.now(),LocalDateTime.now().plusHours(5),"1");

        when(workoutService.createWorkout(dto)).thenReturn(new ReturnWorkoutDto(workout));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/workout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        ArgumentCaptor<CreateWorkoutDto> captor = ArgumentCaptor.forClass(CreateWorkoutDto.class);

        verify(workoutService).createWorkout(captor.capture());
        assertEquals(workout.getNotes(), captor.getValue().notes());
        assertEquals(workout.getUserId().getId(), captor.getValue().userId());
    }

    @Test
    @DisplayName("Must Return Status 404 Not Found When Create Workout With User Id Not Found")
    void createWorkoutError() throws Exception {
        CreateWorkoutDto dto = new CreateWorkoutDto(workout.getNotes(),LocalDateTime.now(),LocalDateTime.now().plusHours(5),"1");

        when(workoutService.createWorkout(dto)).thenThrow(new UserNotFoundException(workout.getUserId().getId()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/workout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(workoutService,times(1)).createWorkout(dto);
    }

    @Test
    @DisplayName("Must Return 200 Ok Searching All Workouts")
    void listAllWorkoutsSuccess() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/workout")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(workoutService,times(1)).getAllWorkouts(any(Pageable.class));
    }

    @Test
    @DisplayName("Must Return 200 Ok Searching Workout By Id")
    void listWorkoutByIdSuccess() throws Exception {
        when(workoutService.getWorkoutById(workout.getId())).thenReturn(new ReturnWorkoutDto(workout));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/workout/{id}", workout.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value("1"));

        verify(workoutService,times(1)).getWorkoutById(workout.getId());
    }

    @Test
    @DisplayName("Must Return 404 Not Found When Workout Id Not Found")
    void listWorkoutByIdError() throws Exception {
        when(workoutService.getWorkoutById(workout.getId())).thenThrow(new WorkoutNotFoundException(workout.getId()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/workout/{id}", workout.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(workoutService,times(1)).getWorkoutById(workout.getId());
    }

    @Test
    @DisplayName("Return 200 Ok When Update Workout")
    void updateWorkoutSuccess() throws Exception {
        UpdateWorkoutDto update = new UpdateWorkoutDto("novo", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        ReturnWorkoutDto dto = new ReturnWorkoutDto(workout);
        when(workoutService.updateWorkoutDto(update,workout.getId())).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/workout/{id}", workout.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("notes").value(dto.notes()));

        verify(workoutService, times(1)).updateWorkoutDto(update,workout.getId());
    }

    @Test
    @DisplayName("Must Return 404 Not Found When Trying To Update Workout With Id Not Found")
    void updateWorkoutError() throws Exception {
        UpdateWorkoutDto update = new UpdateWorkoutDto("novo", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        ReturnWorkoutDto dto = new ReturnWorkoutDto(workout);
        when(workoutService.updateWorkoutDto(update,workout.getId())).thenThrow(new WorkoutNotFoundException(workout.getId()));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/workout/{id}", workout.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(workoutService, times(1)).updateWorkoutDto(update,workout.getId());
    }

    @Test
    @DisplayName("Must Return 204 No Content When Trying To Delete Workout By Id")
    void deleteWorkoutSuccess() throws Exception {
        doNothing().when(workoutService).deleteWorkout(workout.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/workout/{id}", workout.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(workoutService, times(1)).deleteWorkout(workout.getId());
    }

    @Test
    @DisplayName("Must Return 404 No Found When Trying To Delete Workout By Id Not Found")
    void deleteWorkoutError() throws Exception {
        doThrow(new WorkoutNotFoundException(workout.getId())).when(workoutService).deleteWorkout(workout.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/workout/{id}", workout.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(workoutService, times(1)).deleteWorkout(workout.getId());
    }

}
