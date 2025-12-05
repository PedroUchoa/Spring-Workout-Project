package com.pedro.workoutproject.services;

import com.pedro.workoutproject.dtos.userDtos.CreateUserDto;
import com.pedro.workoutproject.dtos.userDtos.ReturnUserDto;
import com.pedro.workoutproject.enumerated.Role;
import com.pedro.workoutproject.infra.Exceptions.userExceptions.UserNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.userExceptions.UserWithEmailDuplicatedException;
import com.pedro.workoutproject.models.User;
import com.pedro.workoutproject.repositories.UserRepository;
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
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
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
    @DisplayName("Must Create a User")
    void createUserSuccess() {
        CreateUserDto createUserDto = new CreateUserDto(user.getEmail(), user.getPassword());

        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.createUser(createUserDto);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getEmail()).isEqualTo(createUserDto.email());
    }


    @Test
    @DisplayName("Must Throw Error When User With Email Duplicated")
    void CreateUserError() {
        CreateUserDto dto = new CreateUserDto(user.getEmail(), user.getPassword());

        when(userRepository.findAnyByEmailIncludingInactive(user.getEmail())).thenReturn(Optional.of(user));

        UserWithEmailDuplicatedException exception = assertThrows(UserWithEmailDuplicatedException.class, () -> userService.createUser(dto));

        assertEquals("The email: " + user.getEmail() + " Is Already In Use, Please Try Again Or Re-Enable Your Old Account!", exception.getMessage());
        verify(userRepository, never()).save(user);
    }

    @Test
    @DisplayName("Must Return All Users")
    void getAllUsersSuccess() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<User> mockPage = new PageImpl<>(List.of(user));

        when(userRepository.findAll(pageable)).thenReturn(mockPage);

        Page<ReturnUserDto> userDtoPage = userService.getAllUsers(pageable);

        verify(userRepository, times(1)).findAll(pageable);
        assertThat(userDtoPage).usingRecursiveComparison().isEqualTo(mockPage);
    }

    @Test
    @DisplayName("Must Return User By Id")
    void getUserByIdSuccess() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ReturnUserDto returnUserDto = userService.getUserById(user.getId());

        assertEquals(new ReturnUserDto(user), returnUserDto);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    @DisplayName("Must Thrown Exception When User Not Found By Id")
    void getUserByIdError() {

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserById(user.getId()));

        assertEquals("User with Id: " + user.getId() + " Not Found Or Deleted, Please Try Again!", exception.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    @DisplayName("Must Return User By Token JWT")
    void getUserByTokenJWTSuccess() {

        String token = "eyJhbGciOiJub25lIn0.eyJpZCI6IjEifQ.c2lnbmF0dXJl";

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ReturnUserDto returnUserDto = userService.getUserByTokenJWT(token);

        assertEquals(new ReturnUserDto(user), returnUserDto);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    @DisplayName("Must Thrown Exception When User Not Found By TokenJwt Id")
    void getUserByTokenJWTError() {

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserById(user.getId()));

        assertEquals("User with Id: " + user.getId() + " Not Found Or Deleted, Please Try Again!", exception.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    @DisplayName("Must Update User")
    void updateUserSuccess() {
        CreateUserDto update = new CreateUserDto("newEmail@teste.com", "newPassword");

        when(userRepository.findAnyByEmailIncludingInactive(update.email())).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        ReturnUserDto result = userService.updateUser(update, user.getId());

        assertEquals(result.email(), update.email());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Must Throw Exception When Try Update User With Email Duplicated")
    void updateUserError() {
        CreateUserDto update = new CreateUserDto("newEmail@teste.com", "newPassword");

        when(userRepository.findAnyByEmailIncludingInactive(update.email())).thenReturn(Optional.of(new User()));

        UserWithEmailDuplicatedException exception = assertThrows(UserWithEmailDuplicatedException.class, () -> userService.updateUser(update, user.getId()));

        assertEquals("The email: " + update.email() + " Is Already In Use, Please Try Again Or Re-Enable Your Old Account!", exception.getMessage());
        verify(userRepository, times(1)).findAnyByEmailIncludingInactive(update.email());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Must Throw Exception When Try Update User With Id Invalid")
    void updateUserErrorTwo() {
        CreateUserDto update = new CreateUserDto("newEmail@teste.com", "newPassword");

        when(userRepository.findAnyByEmailIncludingInactive(update.email())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.updateUser(update, user.getId()));

        assertEquals("User with Id: " + user.getId() + " Not Found Or Deleted, Please Try Again!", exception.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Must Delete A User")
    void deleteUserSuccess() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.deleteUser(user.getId());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(captor.capture());

        User deletedUser = captor.getValue();
        assertThat(deletedUser.getIsActive()).isFalse();
        assertThat(deletedUser.getDeleteOn()).isNotNull();
    }

    @Test
    @DisplayName("Must Thrown Exception When User Not Found To Be Deleted By Id")
    void deleteExerciseError() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUser(user.getId()));

        assertEquals("User with Id: " + user.getId() + " Not Found Or Deleted, Please Try Again!", exception.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
    }


}
