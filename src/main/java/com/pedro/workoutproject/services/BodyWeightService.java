package com.pedro.workoutproject.services;

import com.pedro.workoutproject.dtos.bodyWeightDtos.CreateBodyWeightDto;
import com.pedro.workoutproject.dtos.bodyWeightDtos.ReturnBodyWeightDto;
import com.pedro.workoutproject.dtos.bodyWeightDtos.UpdateBodyWeightDto;
import com.pedro.workoutproject.models.BodyWeight;
import com.pedro.workoutproject.models.User;
import com.pedro.workoutproject.repositories.BodyWeightRepository;
import com.pedro.workoutproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ReturnBodyWeightDto createBodyWeight(CreateBodyWeightDto createBodyWeightDto){
        User user = userRepository.findByIdAndIsActiveTrue(createBodyWeightDto.userId()).orElseThrow(()->new RuntimeException("teste"));
        BodyWeight bodyWeight = new BodyWeight(createBodyWeightDto.value(),user);
        bodyWeightRepository.save(bodyWeight);
        return new ReturnBodyWeightDto(bodyWeight);
    }

    public ReturnBodyWeightDto getBodyWeightById(String id){
        BodyWeight bodyWeight = bodyWeightRepository.findBodyWeightByIdAndIsActiveTrue(id).orElseThrow(()->new RuntimeException("teste"));
        return new ReturnBodyWeightDto(bodyWeight);
    }

    public Page<ReturnBodyWeightDto> getBodyWeightByUserId(String userId, Pageable pageable){
        userRepository.findByIdAndIsActiveTrue(userId).orElseThrow(()->new RuntimeException("Invalid"));
        return  bodyWeightRepository.findAllByUserIdId(userId, pageable).map(ReturnBodyWeightDto::new);
    }

    @Transactional
    public ReturnBodyWeightDto updateBodyWeight(String id, UpdateBodyWeightDto updateBodyWeightDto){
        BodyWeight bodyWeight = bodyWeightRepository.findBodyWeightByIdAndIsActiveTrue(id).orElseThrow(()->new RuntimeException("teste"));
        bodyWeight.update(updateBodyWeightDto);
        bodyWeightRepository.save(bodyWeight);
        return new ReturnBodyWeightDto(bodyWeight);
    }

    @Transactional
    public void deleteBodyWeight(String id){
        BodyWeight bodyWeight = bodyWeightRepository.findBodyWeightByIdAndIsActiveTrue(id).orElseThrow(()->new RuntimeException("teste"));
        bodyWeight.disable();
        bodyWeightRepository.save(bodyWeight);
    }
}
