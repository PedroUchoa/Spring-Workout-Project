package com.pedro.workoutproject.services;

import com.pedro.workoutproject.dtos.bodyWeightDtos.CreateBodyWeightDto;
import com.pedro.workoutproject.dtos.bodyWeightDtos.ReturnBodyWeightDto;
import com.pedro.workoutproject.dtos.bodyWeightDtos.UpdateBodyWeightDto;
import com.pedro.workoutproject.infra.Exceptions.bodyWeightExceptions.BodyWeightNotFoundException;
import com.pedro.workoutproject.infra.Exceptions.userExceptions.UserNotFoundException;
import com.pedro.workoutproject.models.BodyWeight;
import com.pedro.workoutproject.models.User;
import com.pedro.workoutproject.repositories.BodyWeightRepository;
import com.pedro.workoutproject.repositories.UserRepository;
import org.assertj.core.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BodyWeightServiceTest {

    @Mock
    private BodyWeightRepository bodyWeightRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BodyWeightService bodyWeightService;


    @Test
    @DisplayName("Must Create a Body Weight Successfully")
    void createBodyWeightSuccess() {
        CreateBodyWeightDto bodyWeightDto = new CreateBodyWeightDto(this.buildMockBodyWeight().getValue(), "1");

        when(userRepository.findById("1")).thenReturn(Optional.of(this.buildMockUser()));

        this.bodyWeightService.createBodyWeight(bodyWeightDto);

        verify(bodyWeightRepository, times(1)).save(new BodyWeight(bodyWeightDto.value(), this.buildMockUser()));
    }

    @Test
    @DisplayName("Must Thrown Exception When No Found An User With The Id Passed")
    void createBodyWeightError() {
        CreateBodyWeightDto bodyWeightDto = new CreateBodyWeightDto(this.buildMockBodyWeight().getValue(), "1");

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> bodyWeightService.createBodyWeight(bodyWeightDto));

        assertEquals("User with Id: " + bodyWeightDto.userId() + " Not Found Or Deleted, Please Try Again!", exception.getMessage());
        verify(userRepository, times(1)).findById(bodyWeightDto.userId());

    }

    @Test
    @DisplayName("Must Return A Body Weight By Id")
    void getBodyWeightByIdSuccess() {
        BodyWeight bodyWeight = buildMockBodyWeight();

        when(bodyWeightRepository.findById(bodyWeight.getId())).thenReturn(Optional.of(bodyWeight));

        ReturnBodyWeightDto findBodyWeight = bodyWeightService.getBodyWeightById(bodyWeight.getId());

        assertEquals(new ReturnBodyWeightDto(bodyWeight), findBodyWeight);
        verify(bodyWeightRepository, times(1)).findById(bodyWeight.getId());
    }

    @Test
    @DisplayName("Must Throw Exception When Not Found Body Weight By Id")
    void getBodyWeightByIdError() {
        BodyWeight bodyWeight = buildMockBodyWeight();

        BodyWeightNotFoundException exception = assertThrows(BodyWeightNotFoundException.class, () -> bodyWeightService.getBodyWeightById(bodyWeight.getId()));

        assertEquals("Body Weight With Id: " + bodyWeight.getId() + " Not Found In Database, Please Try Again!", exception.getMessage());
        verify(bodyWeightRepository, times(1)).findById(bodyWeight.getId());
    }

    @Test
    @DisplayName("Must Return All Body Weights From a User")
    void getBodyWeightByUserIdSuccess() {
        User user = this.buildMockUser();
        BodyWeight bodyWeight = this.buildMockBodyWeight();
        Pageable pageable = PageRequest.of(0, 1);
        Page<BodyWeight> mockPage = new PageImpl<>(List.of(bodyWeight));

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bodyWeightRepository.findAllByUserIdId(user.getId(), pageable)).thenReturn(mockPage);

        Page<ReturnBodyWeightDto> bodyWeightDtos = bodyWeightService.getBodyWeightByUserId(user.getId(), pageable);

        verify(bodyWeightRepository, times(1)).findAllByUserIdId(user.getId(), pageable);
        Assertions.assertThat(bodyWeightDtos).usingRecursiveComparison().isEqualTo(mockPage);
    }

    @Test
    @DisplayName("Must Throw Exception When User Id Not Found")
    void getBodyWeightByUserIdError() {
        User user = this.buildMockUser();
        Pageable pageable = PageRequest.of(0, 1);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> bodyWeightService.getBodyWeightByUserId(user.getId(), pageable));

        assertEquals("User with Id: " + user.getId() + " Not Found Or Deleted, Please Try Again!", exception.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    @DisplayName("Must Update a Body Weight")
    void updateBodyWeightSuccess() {
        BodyWeight bodyWeight = this.buildMockBodyWeight();

        given(bodyWeightRepository.findById(bodyWeight.getId())).willReturn(Optional.of(bodyWeight));

        UpdateBodyWeightDto update = new UpdateBodyWeightDto(30.0);
        ReturnBodyWeightDto result = bodyWeightService.updateBodyWeight(bodyWeight.getId(), update);

        assertEquals(result.value(), update.value());
        assertEquals(result.id(), bodyWeight.getId());
        verify(bodyWeightRepository, times(1)).findById(bodyWeight.getId());
    }

    @Test
    @DisplayName("Must Throw Exception When Try To Update BodyWeight Not Found By Id")
    void updateBodyWeightError() {
        BodyWeight bodyWeight = this.buildMockBodyWeight();

        BodyWeightNotFoundException exception = assertThrows(BodyWeightNotFoundException.class, () -> bodyWeightService.updateBodyWeight(bodyWeight.getId(), new UpdateBodyWeightDto(30.0)));

        assertEquals("Body Weight With Id: " + bodyWeight.getId() + " Not Found In Database, Please Try Again!", exception.getMessage());
        verify(bodyWeightRepository, times(1)).findById(bodyWeight.getId());
    }

    @Test
    @DisplayName("Must Delete A BodyWeight")
    void deleteBodyWeightSuccess() {
        BodyWeight bodyWeight = this.buildMockBodyWeight();

        when(bodyWeightRepository.findById(bodyWeight.getId())).thenReturn(Optional.of(bodyWeight));
        bodyWeightService.deleteBodyWeight(bodyWeight.getId());

        ArgumentCaptor<BodyWeight> captor = ArgumentCaptor.forClass(BodyWeight.class);
        verify(bodyWeightRepository, times(1)).save(captor.capture());

        BodyWeight deletedBodyWeight = captor.getValue();
        Assertions.assertThat(deletedBodyWeight.getIsActive()).isFalse();
    }

    @Test
    @DisplayName("Must Throw Exception When Don't find BodyWeight By Id")
    void deleteBodyWeightError() {
        BodyWeight bodyWeight = this.buildMockBodyWeight();

        BodyWeightNotFoundException exception = assertThrows(BodyWeightNotFoundException.class, () -> bodyWeightService.deleteBodyWeight(bodyWeight.getId()));

        assertEquals("Body Weight With Id: " + bodyWeight.getId() + " Not Found In Database, Please Try Again!", exception.getMessage());
        verify(bodyWeightRepository, times(1)).findById(bodyWeight.getId());
    }

    private BodyWeight buildMockBodyWeight() {
        BodyWeight bodyWeight = new BodyWeight();
        bodyWeight.setId("1");
        bodyWeight.setValue(50.0);
        bodyWeight.setIsActive(Boolean.TRUE);
        bodyWeight.setLoggedOn(LocalDateTime.now());
        return bodyWeight;
    }

    private User buildMockUser() {
        User user = new User();
        user.setId("1");
        user.setPassword("teste");
        user.setEmail("teste@gmail.com");
        user.setIsActive(Boolean.TRUE);
        return user;
    }


}
