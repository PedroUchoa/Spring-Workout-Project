package com.pedro.workoutproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.workoutproject.dtos.exerciseDtos.CreateExerciseDto;
import com.pedro.workoutproject.dtos.exerciseDtos.ReturnExerciseDto;
import com.pedro.workoutproject.infra.Exceptions.exercise.ExerciseNotFoundException;
import com.pedro.workoutproject.models.Exercise;
import com.pedro.workoutproject.services.ExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
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
    @DisplayName("Must Return Status 201 Created When Create Exercise")
    void createExerciseSuccess() throws Exception {
        CreateExerciseDto dto = new CreateExerciseDto("teste","teste");

        when(exerciseService.createExercise(dto)).thenReturn(new ReturnExerciseDto(exercise));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/exercise")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        ArgumentCaptor<CreateExerciseDto> captor = ArgumentCaptor.forClass(CreateExerciseDto.class);

        verify(exerciseService).createExercise(captor.capture());
        assertEquals(exercise.getType(), captor.getValue().type());
        assertEquals(exercise.getName(), captor.getValue().name());
    }

    @Test
    @DisplayName("Must Return 200 Ok Searching Exercise By Id")
    void listExerciseByIdSuccess() throws Exception {
        when(exerciseService.getExerciseById(exercise.getId())).thenReturn(new ReturnExerciseDto(exercise));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/exercise/{id}", exercise.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value("1"));
    }

    @Test
    @DisplayName("Must Return 404 Not Found When Exercise Id Not Found")
    void listExerciseByIdError() throws Exception {
        when(exerciseService.getExerciseById(exercise.getId())).thenThrow(new ExerciseNotFoundException(exercise.getId()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/exercise/{id}", exercise.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(exerciseService,times(1)).getExerciseById(exercise.getId());
    }

    @Test
    @DisplayName("Must Return 200 Ok Searching All exercises")
    void listAllExerciseSuccess() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        ReturnExerciseDto dto = new ReturnExerciseDto(exercise);
        Page<ReturnExerciseDto> page = new PageImpl<>(List.of(new ReturnExerciseDto(exercise)), pageable, 1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/exercise")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(exerciseService).getAllExercises(any(Pageable.class));
    }

    @Test
    @DisplayName("Return 200 Ok When Update Exercise")
    void updateExerciseSuccess() throws Exception {
        CreateExerciseDto update = new CreateExerciseDto("teste","teste");
        ReturnExerciseDto dto = new ReturnExerciseDto(exercise);
        when(exerciseService.updateExercise(update,exercise.getId())).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/exercise/{id}", exercise.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(dto.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(dto.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("type").value(dto.type()));

        verify(exerciseService, times(1)).updateExercise(update,exercise.getId());
    }

    @Test
    @DisplayName("Must Return 404 Not Found When Trying To Update Exercise With Id Not Found")
    void updateExerciseError() throws Exception {
        CreateExerciseDto update = new CreateExerciseDto("teste","teste");
        ReturnExerciseDto dto = new ReturnExerciseDto(exercise);
        when(exerciseService.updateExercise(update,exercise.getId())).thenThrow(new ExerciseNotFoundException(exercise.getId()));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/exercise/{id}", exercise.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(exerciseService, times(1)).updateExercise(update,exercise.getId());
    }

    @Test
    @DisplayName("Must Return 204 No Content When Trying To Delete Exercise By Id")
    void deleteExerciseSuccess() throws Exception {
        doNothing().when(exerciseService).deleteExercise(exercise.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/exercise/{id}", exercise.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(exerciseService, times(1)).deleteExercise(exercise.getId());
    }

    @Test
    @DisplayName("Must Return 404 No Found When Trying To Delete Exercise By Id Not Found")
    void deleteExerciseError() throws Exception {
        doThrow(new ExerciseNotFoundException(exercise.getId())).when(exerciseService).deleteExercise(exercise.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/exercise/{id}", exercise.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(exerciseService, times(1)).deleteExercise(exercise.getId());
    }

}
