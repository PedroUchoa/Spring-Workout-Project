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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BodyWeightService {

    @Autowired
    private BodyWeightRepository bodyWeightRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @CacheEvict(value = "bodyWeight", allEntries = true)
    public ReturnBodyWeightDto createBodyWeight(CreateBodyWeightDto createBodyWeightDto) {
        User user = userRepository.findById(createBodyWeightDto.userId()).orElseThrow(() -> new UserNotFoundException(createBodyWeightDto.userId()));
        BodyWeight bodyWeight = new BodyWeight(createBodyWeightDto.value(), user);
        bodyWeightRepository.save(bodyWeight);
        return new ReturnBodyWeightDto(bodyWeight);
    }

    @Cacheable(value = "bodyWeight")
    public ReturnBodyWeightDto getBodyWeightById(String id) {
        BodyWeight bodyWeight = bodyWeightRepository.findById(id).orElseThrow(() -> new BodyWeightNotFoundException(id));
        return new ReturnBodyWeightDto(bodyWeight);
    }

    @Cacheable(value = "bodyWeight")
    public Page<ReturnBodyWeightDto> getBodyWeightByUserId(String userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return bodyWeightRepository.findAllByUserIdId(userId, pageable).map(ReturnBodyWeightDto::new);
    }

    @Transactional
    @CacheEvict(value = "bodyWeight", allEntries = true)
    public ReturnBodyWeightDto updateBodyWeight(String id, UpdateBodyWeightDto updateBodyWeightDto) {
        BodyWeight bodyWeight = bodyWeightRepository.findById(id).orElseThrow(() -> new BodyWeightNotFoundException(id));
        bodyWeight.update(updateBodyWeightDto);
        bodyWeightRepository.save(bodyWeight);
        return new ReturnBodyWeightDto(bodyWeight);
    }

    @Transactional
    @CacheEvict(value = "bodyWeight")
    public void deleteBodyWeight(String id) {
        BodyWeight bodyWeight = bodyWeightRepository.findById(id).orElseThrow(() -> new BodyWeightNotFoundException(id));
        bodyWeight.disable();
        bodyWeightRepository.save(bodyWeight);
    }
}
