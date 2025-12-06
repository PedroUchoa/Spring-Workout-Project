package com.pedro.workoutproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pedro.workoutproject.dtos.bodyWeightDtos.CreateBodyWeightDto;
import com.pedro.workoutproject.dtos.bodyWeightDtos.ReturnBodyWeightDto;
import com.pedro.workoutproject.dtos.bodyWeightDtos.UpdateBodyWeightDto;
import com.pedro.workoutproject.infra.Exceptions.bodyWeightExceptions.BodyWeightNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.userExceptions.UserNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.userExceptions.UserWithEmailDuplicatedException;
import com.pedro.workoutproject.models.BodyWeight;
import com.pedro.workoutproject.models.User;
import com.pedro.workoutproject.services.BodyWeightService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BodyWeightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private BodyWeightService bodyWeightService;

    private BodyWeight bodyWeight;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        bodyWeight = new BodyWeight();
        bodyWeight.setId("1");
        bodyWeight.setValue(50.0);
        bodyWeight.setIsActive(Boolean.TRUE);
        bodyWeight.setLoggedOn(LocalDateTime.now());
        User user = new User();
        user.setId("1");
        bodyWeight.setUserId(user);
    }

    @Test
    @DisplayName("Must return status 201 created")
    void createBodyWeightSuccess() throws Exception {
        CreateBodyWeightDto dto = new CreateBodyWeightDto(50.5, "1",bodyWeight.getLoggedOn());

        when(bodyWeightService.createBodyWeight(dto)).thenReturn(new ReturnBodyWeightDto(bodyWeight));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/weight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        ArgumentCaptor<CreateBodyWeightDto> captor = ArgumentCaptor.forClass(CreateBodyWeightDto.class);

        verify(bodyWeightService).createBodyWeight(captor.capture());
        assertEquals(50.5, captor.getValue().value());
    }

    @Test
    @DisplayName("Must Return 409 Conflict Trying To Create User With Email Duplicated")
    void createUserError() throws Exception {
        CreateBodyWeightDto dto = new CreateBodyWeightDto(50.5, "1",bodyWeight.getLoggedOn());

        when(bodyWeightService.createBodyWeight(dto)).thenThrow(new UserWithEmailDuplicatedException("teste"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/weight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isConflict());

        ArgumentCaptor<CreateBodyWeightDto> captor = ArgumentCaptor.forClass(CreateBodyWeightDto.class);
        verify(bodyWeightService, times(1)).createBodyWeight(captor.capture());
    }

    @Test
    @DisplayName("Must Return 200 Ok Searching BodyWeight By Id")
    void listBodyWeightByIdSuccess() throws Exception {
        ReturnBodyWeightDto dto = new ReturnBodyWeightDto(bodyWeight);
        when(bodyWeightService.getBodyWeightById(bodyWeight.getId())).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/weight/{id}", bodyWeight.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value("1"));
    }


    @Test
    @DisplayName("Must Return 404 Not Found When BodyWeight Id Not Found")
    void listBodyWeightByIdError() throws Exception {
        when(bodyWeightService.getBodyWeightById(bodyWeight.getId())).thenThrow(new BodyWeightNotFoundException(bodyWeight.getId()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/weight/{id}", bodyWeight.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Must Return 200 Ok Searching BodyWeight By Id")
    void listBodyWeightByUserIdSuccess() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        ReturnBodyWeightDto dto = new ReturnBodyWeightDto(bodyWeight);
        Page<ReturnBodyWeightDto> page = new PageImpl<>(List.of(new ReturnBodyWeightDto(bodyWeight)), pageable, 1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/weight/user/{id}", bodyWeight.getId())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(bodyWeightService).getBodyWeightByUserId(eq("1"), any(Pageable.class));
    }

    @Test
    @DisplayName("Return 404 Not Found When Trying To Find BodyWeight Searching By Id Not Found")
    void listBodyWeightByUserIdError() throws Exception {
        when(bodyWeightService.getBodyWeightByUserId(eq(bodyWeight.getUserId().getId()), any(Pageable.class))).thenThrow(new UserNotFoundException(bodyWeight.getUserId().getId()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/weight/user/{id}", bodyWeight.getUserId().getId())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(bodyWeightService).getBodyWeightByUserId(eq(bodyWeight.getUserId().getId()), any(Pageable.class));
    }

    @Test
    @DisplayName("Return 200 Ok When Update Body Weight")
    void updateBodyWeightSuccess() throws Exception {
        UpdateBodyWeightDto update = new UpdateBodyWeightDto(10.0);
        ReturnBodyWeightDto dto = new ReturnBodyWeightDto("1", 10.0, bodyWeight.getLoggedOn());
        when(bodyWeightService.updateBodyWeight(bodyWeight.getId(), update)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/weight/{id}", bodyWeight.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(dto.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("value").value(dto.value()));

        verify(bodyWeightService, times(1)).updateBodyWeight(bodyWeight.getId(), update);
    }

    @Test
    @DisplayName("Must Return 404 Not Found When Trying To Update Body Weight With Id Not Found")
    void updateBodyWeightError() throws Exception {
        UpdateBodyWeightDto update = new UpdateBodyWeightDto(10.0);
        ReturnBodyWeightDto dto = new ReturnBodyWeightDto("1", 10.0, bodyWeight.getLoggedOn());
        when(bodyWeightService.updateBodyWeight(bodyWeight.getId(), update)).thenThrow(new BodyWeightNotFoundException(bodyWeight.getId()));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/weight/{id}", bodyWeight.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(bodyWeightService, times(1)).updateBodyWeight(bodyWeight.getId(), update);
    }

    @Test
    @DisplayName("Must Return 204 No Content When Trying To Delete Body Weight By Id")
    void deleteBodyWeightSuccess() throws Exception {
        doNothing().when(bodyWeightService).deleteBodyWeight(bodyWeight.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/weight/{id}", bodyWeight.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @DisplayName("Must Return 404 No Found When Trying To Delete Body Weight By Id Not Found")
    void deleteBodyWeightError() throws Exception {
        doThrow(new BodyWeightNotFoundException(bodyWeight.getId())).when(bodyWeightService).deleteBodyWeight(bodyWeight.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/weight/{id}", bodyWeight.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

}
