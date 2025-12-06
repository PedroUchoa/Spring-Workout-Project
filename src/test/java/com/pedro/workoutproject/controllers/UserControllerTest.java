package com.pedro.workoutproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.workoutproject.dtos.userDtos.CreateUserDto;
import com.pedro.workoutproject.dtos.userDtos.ReturnUserDto;
import com.pedro.workoutproject.enumerated.Role;
import com.pedro.workoutproject.infra.Exceptions.userExceptions.UserNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.userExceptions.UserWithEmailDuplicatedException;
import com.pedro.workoutproject.models.User;
import com.pedro.workoutproject.services.UserService;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("1");
        user.setEmail("email@teste.com");
        user.setPassword("teste");
        user.setRole(Role.USER);
        user.setIsActive(Boolean.TRUE);
        user.setCreatedOn(LocalDateTime.now());
    }

    @Test
    @DisplayName("Must Return Status 201 Created When Create User")
    void createUserSuccess() throws Exception {
        CreateUserDto dto = new CreateUserDto("email@teste.com","teste");

        when(userService.createUser(dto)).thenReturn(new ReturnUserDto(user));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        ArgumentCaptor<CreateUserDto> captor = ArgumentCaptor.forClass(CreateUserDto.class);

        verify(userService).createUser(captor.capture());
        assertEquals(user.getEmail(), captor.getValue().email());
        assertEquals(user.getPassword(), captor.getValue().password());
    }

    @Test
    @DisplayName("Must Return Status 409 Conflict When Create User With Email Duplicated")
    void createUserError() throws Exception {
        CreateUserDto dto = new CreateUserDto("email@teste.com","teste");

        when(userService.createUser(dto)).thenThrow(new UserWithEmailDuplicatedException(dto.email()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isConflict());

        verify(userService,times(1)).createUser(dto);
    }

    @Test
    @DisplayName("Must Return 200 Ok Searching User By Id")
    void listUserByIdSuccess() throws Exception {
        when(userService.getUserById(user.getId())).thenReturn(new ReturnUserDto(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value("1"));

        verify(userService,times(1)).getUserById(user.getId());
    }

    @Test
    @DisplayName("Must Return 404 Not Found When User Id Not Found")
    void listUserByIdError() throws Exception {
        when(userService.getUserById(user.getId())).thenThrow(new UserNotFoundException(user.getId()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", user.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(userService,times(1)).getUserById(user.getId());
    }

    @Test
    @DisplayName("Must Return 200 Ok Searching All Users")
    void listAllUsersSuccess() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userService,times(1)).getAllUsers(any(Pageable.class));
    }

    @Test
    @DisplayName("Must Return 200 Ok Searching User By Token JWT")
    void listUserByTokenJwtSuccess() throws Exception {
        String token = "eyJhbGciOiJub25lIn0.eyJpZCI6IjEifQ.c2lnbmF0dXJl";
        when(userService.getUserByTokenJWT(token)).thenReturn(new ReturnUserDto(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/token")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value("1"));

        verify(userService,times(1)).getUserByTokenJWT(token);
    }

    @Test
    @DisplayName("Must Return 404 Not Found Searching User By Token JWT")
    void listUserByTokenJwtError() throws Exception {
        String token = "eyJhbGciOiJub25lIn0.eyJpZCI6IjEifQ.c2lnbmF0dXJl";
        when(userService.getUserByTokenJWT(token)).thenThrow(new UserNotFoundException(user.getId()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/token")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(userService,times(1)).getUserByTokenJWT(token);
    }

    @Test
    @DisplayName("Return 200 Ok When Update User")
    void updateUserSuccess() throws Exception {
        CreateUserDto update = new CreateUserDto("email@teste.com","teste");
        ReturnUserDto dto = new ReturnUserDto(user);
        when(userService.updateUser(update,user.getId())).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(dto.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(dto.email()));

        verify(userService, times(1)).updateUser(update,user.getId());
    }

    @Test
    @DisplayName("Must Return 404 Not Found When Trying To Update User With Email Duplicated")
    void updateUserError() throws Exception {
        CreateUserDto update = new CreateUserDto("email@teste.com","teste");
        when(userService.updateUser(update,user.getId())).thenThrow(new UserWithEmailDuplicatedException(user.getEmail()));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(MockMvcResultMatchers.status().isConflict());

        verify(userService, times(1)).updateUser(update,user.getId());
    }

    @Test
    @DisplayName("Must Return 404 Not Found When Trying To Update User With Id Not Found")
    void updateUserErrorTwo() throws Exception {
        CreateUserDto update = new CreateUserDto("email@teste.com","teste");
        when(userService.updateUser(update,user.getId())).thenThrow(new UserNotFoundException(user.getId()));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(userService, times(1)).updateUser(update,user.getId());
    }

    @Test
    @DisplayName("Must Return 204 No Content When Trying To Delete User By Id")
    void deleteUserSuccess() throws Exception {
        doNothing().when(userService).deleteUser(user.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", user.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(userService, times(1)).deleteUser(user.getId());
    }

    @Test
    @DisplayName("Must Return 404 No Found When Trying To Delete User By Id Not Found")
    void deleteUserError() throws Exception {
        doThrow(new UserNotFoundException(user.getId())).when(userService).deleteUser(user.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", user.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(userService, times(1)).deleteUser(user.getId());
    }

}
