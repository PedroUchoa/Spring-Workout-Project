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

import java.util.List;

@Service
public class BodyWeightService {

    @Autowired
    private BodyWeightRepository bodyWeightRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ReturnBodyWeightDto createBodyWeight(CreateBodyWeightDto createBodyWeightDto){
        User user = userRepository.getReferenceById(createBodyWeightDto.userId());
        BodyWeight bodyWeight = new BodyWeight(createBodyWeightDto.value(),user);
        bodyWeightRepository.save(bodyWeight);
        return new ReturnBodyWeightDto(bodyWeight);
    }

    public ReturnBodyWeightDto getBodyWeightById(String id){
        BodyWeight bodyWeight = bodyWeightRepository.getReferenceById(id);
        return new ReturnBodyWeightDto(bodyWeight);
    }

    public Page<ReturnBodyWeightDto> getBodyWeightByUserId(String userId, Pageable pageable){
        return  bodyWeightRepository.findAllByUserId(userId, pageable).map(ReturnBodyWeightDto::new);
    }

    @Transactional
    public ReturnBodyWeightDto updateBodyWeight(String id, UpdateBodyWeightDto updateBodyWeightDto){
        BodyWeight bodyWeight = bodyWeightRepository.getReferenceById(id);
        bodyWeight.update(updateBodyWeightDto);
        bodyWeightRepository.save(bodyWeight);
        return new ReturnBodyWeightDto(bodyWeight);
    }

    @Transactional
    public void deleteBodyWeight(String id){
        BodyWeight bodyWeight = bodyWeightRepository.getReferenceById(id);
        bodyWeightRepository.delete(bodyWeight);
    }

}
