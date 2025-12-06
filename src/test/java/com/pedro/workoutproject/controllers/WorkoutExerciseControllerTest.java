package com.pedro.workoutproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.workoutproject.dtos.workoutExerciseDtos.CreateWorkoutExerciseDto;
import com.pedro.workoutproject.dtos.workoutExerciseDtos.ReturnWorkoutExerciseDto;
import com.pedro.workoutproject.dtos.workoutExerciseDtos.UpdateWorkoutExerciseDto;
import com.pedro.workoutproject.infra.Exceptions.exercise.ExerciseNameNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.workoutExceptions.WorkoutNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.workoutExerciseException.WorkoutExerciseNotFoundException;
import com.pedro.workoutproject.models.Exercise;
import com.pedro.workoutproject.models.Workout;
import com.pedro.workoutproject.models.WorkoutExercise;
import com.pedro.workoutproject.services.WorkoutExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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
public class WorkoutExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private WorkoutExerciseService workoutExerciseService;

    private WorkoutExercise workoutExercise;

    @BeforeEach
    void setUp(){
        Exercise exercise = new Exercise();
        exercise.setId("1");
        Workout workout = new Workout();
        workout.setId("1");
        workoutExercise = new WorkoutExercise();
        workoutExercise.setId("1");
        workoutExercise.setNotes("teste");
        workoutExercise.setIsActive(Boolean.TRUE);
        workoutExercise.setReps(8);
        workoutExercise.setSets(8);
        workoutExercise.setCreatedOn(LocalDateTime.now());
        workoutExercise.setWeight(8);
        workoutExercise.setExerciseId(exercise);
        workoutExercise.setWorkoutId(workout);
    }

    @Test
    @DisplayName("Must Return Status 201 Created When Create User")
    void createWorkoutExerciseSuccess() throws Exception {
        CreateWorkoutExerciseDto dto = new CreateWorkoutExerciseDto(workoutExercise.getWeight(),workoutExercise.getSets(),workoutExercise.getReps(),workoutExercise.getNotes(),"1","Teste");

        when(workoutExerciseService.createWorkoutExercise(dto)).thenReturn(new ReturnWorkoutExerciseDto(workoutExercise));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/workoutexercise")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        ArgumentCaptor<CreateWorkoutExerciseDto> captor = ArgumentCaptor.forClass(CreateWorkoutExerciseDto.class);

        verify(workoutExerciseService).createWorkoutExercise(captor.capture());
        assertEquals(workoutExercise.getSets(), captor.getValue().sets());
        assertEquals(workoutExercise.getReps(), captor.getValue().reps());
    }

    @Test
    @DisplayName("Must Return Status 404 Not Found When Try Create WorkoutExercise With Workout Id Not Found")
    void createWorkoutExerciseError() throws Exception {
        CreateWorkoutExerciseDto dto = new CreateWorkoutExerciseDto(workoutExercise.getWeight(),workoutExercise.getSets(),workoutExercise.getReps(),workoutExercise.getNotes(),"1","Teste");

        when(workoutExerciseService.createWorkoutExercise(dto)).thenThrow(new WorkoutNotFoundException(dto.workoutId()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/workoutexercise")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(workoutExerciseService,times(1)).createWorkoutExercise(dto);
    }

    @Test
    @DisplayName("Must Return Status 404 Not Found When Try Create WorkoutExercise With Exercise Name Not Found")
    void createWorkoutExerciseErrorTwo() throws Exception {
        CreateWorkoutExerciseDto dto = new CreateWorkoutExerciseDto(workoutExercise.getWeight(),workoutExercise.getSets(),workoutExercise.getReps(),workoutExercise.getNotes(),"1","Teste");

        when(workoutExerciseService.createWorkoutExercise(dto)).thenThrow(new ExerciseNameNotFoundException(dto.exerciseName()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/workoutexercise")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(workoutExerciseService,times(1)).createWorkoutExercise(dto);
    }

    @Test
    @DisplayName("Must Return 200 Ok Searching All WorkoutsExercises")
    void listAllWorkoutsSuccess() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/workoutexercise")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(workoutExerciseService,times(1)).getAllWorkoutExercise(any(Pageable.class));
    }

    @Test
    @DisplayName("Must Return 200 Ok Searching Workoutexercise By Id")
    void listWorkoutExerciseByIdSuccess() throws Exception {
        when(workoutExerciseService.findWorkoutExerciseById(workoutExercise.getId())).thenReturn(new ReturnWorkoutExerciseDto(workoutExercise));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/workoutexercise/{id}", workoutExercise.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value("1"));

        verify(workoutExerciseService,times(1)).findWorkoutExerciseById(workoutExercise.getId());
    }

    @Test
    @DisplayName("Must Return 404 Not Found When WorkoutExercise Id Not Found")
    void listWorkoutExerciseByIdError() throws Exception {
        when(workoutExerciseService.findWorkoutExerciseById(workoutExercise.getId())).thenThrow(new WorkoutNotFoundException(workoutExercise.getId()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/workoutexercise/{id}", workoutExercise.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(workoutExerciseService,times(1)).findWorkoutExerciseById(workoutExercise.getId());
    }

    @Test
    @DisplayName("Return 200 Ok When Update WorkoutExercise")
    void updateWorkoutExerciseSuccess() throws Exception {
        UpdateWorkoutExerciseDto update = new UpdateWorkoutExerciseDto(2,2,2,"new");
        ReturnWorkoutExerciseDto dto = new ReturnWorkoutExerciseDto(workoutExercise);
        when(workoutExerciseService.updateWorkoutExercise(update,workoutExercise.getId())).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/workoutexercise/{id}", workoutExercise.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("notes").value(dto.notes()))
                .andExpect(MockMvcResultMatchers.jsonPath("weight").value(dto.weight()))
                .andExpect(MockMvcResultMatchers.jsonPath("sets").value(dto.sets()))
                .andExpect(MockMvcResultMatchers.jsonPath("reps").value(dto.reps()));

        verify(workoutExerciseService, times(1)).updateWorkoutExercise(update,workoutExercise.getId());
    }

    @Test
    @DisplayName("Must Return 404 Not Found When Trying To Update WorkoutExercise With Id Not Found")
    void updateWorkoutExerciseError() throws Exception {
        UpdateWorkoutExerciseDto update = new UpdateWorkoutExerciseDto(2,2,2,"new");
        when(workoutExerciseService.updateWorkoutExercise(update,workoutExercise.getId())).thenThrow(new WorkoutExerciseNotFoundException(workoutExercise.getId()));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/workoutexercise/{id}", workoutExercise.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(workoutExerciseService, times(1)).updateWorkoutExercise(update,workoutExercise.getId());
    }

    @Test
    @DisplayName("Must Return 204 No Content When Trying To Delete WorkoutExercise By Id")
    void deleteWorkoutSuccess() throws Exception {
        doNothing().when(workoutExerciseService).deleteWorkoutExercise(workoutExercise.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/workoutexercise/{id}", workoutExercise.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(workoutExerciseService, times(1)).deleteWorkoutExercise(workoutExercise.getId());
    }

    @Test
    @DisplayName("Must Return 404 No Found When Trying To Delete WorkoutExercise By Id Not Found")
    void deleteWorkoutError() throws Exception {
        doThrow(new WorkoutNotFoundException(workoutExercise.getId())).when(workoutExerciseService).deleteWorkoutExercise(workoutExercise.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/workoutexercise/{id}", workoutExercise.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(workoutExerciseService, times(1)).deleteWorkoutExercise(workoutExercise.getId());
    }


}
