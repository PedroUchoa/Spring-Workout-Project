package com.pedro.workoutproject.controllers;

import com.pedro.workoutproject.dtos.exerciseDtos.CreateExerciseDto;
import com.pedro.workoutproject.dtos.exerciseDtos.ReturnExerciseDto;
import com.pedro.workoutproject.services.ExerciseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {


    @Autowired
    private ExerciseService exerciseService;

    @PostMapping
    public ResponseEntity<ReturnExerciseDto> createExercise(@RequestBody CreateExerciseDto createExerciseDto, UriComponentsBuilder uri){
        ReturnExerciseDto returnExerciseDto = exerciseService.createExercise(createExerciseDto);
        URI location = uri.path("/id")
                .buildAndExpand(returnExerciseDto.id())
                .toUri();
        return ResponseEntity.created(location).body(returnExerciseDto);
    }

    @GetMapping
    public ResponseEntity<Page<ReturnExerciseDto>> getAllExercises(@PageableDefault(sort = {"id"}) Pageable pageable){
        Page<ReturnExerciseDto> returnExerciseDtos = exerciseService.getAllExercises(pageable);
        return ResponseEntity.ok(returnExerciseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnExerciseDto> getExerciseById(@PathVariable String id){
        ReturnExerciseDto returnExerciseDto = exerciseService.getExerciseById(id);
        return ResponseEntity.ok(returnExerciseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnExerciseDto> updateExercise(@PathVariable String id, @RequestBody CreateExerciseDto createExerciseDto){
        ReturnExerciseDto returnExerciseDto = exerciseService.updateExercise(createExerciseDto,id);
        return ResponseEntity.ok(returnExerciseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable String id){
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }


}
