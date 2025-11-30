package com.pedro.workoutproject.controllers;

import com.pedro.workoutproject.dtos.workoutExerciseDtos.CreateWorkoutExerciseDto;
import com.pedro.workoutproject.dtos.workoutExerciseDtos.ReturnWorkoutExerciseDto;
import com.pedro.workoutproject.dtos.workoutExerciseDtos.UpdateWorkoutExerciseDto;
import com.pedro.workoutproject.services.WorkoutExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/workoutexercise")
public class WorkoutExerciseController {

    @Autowired
    private WorkoutExerciseService workoutExerciseService;

    @PostMapping
    public ResponseEntity<ReturnWorkoutExerciseDto> createWorkoutExercise(@RequestBody CreateWorkoutExerciseDto createWorkoutExerciseDto, UriComponentsBuilder uri) {
        ReturnWorkoutExerciseDto returnWorkoutExerciseDto = workoutExerciseService.createWorkoutExercise(createWorkoutExerciseDto);
        URI location = uri.path("/id")
                .buildAndExpand(returnWorkoutExerciseDto.id())
                .toUri();
        return ResponseEntity.created(location).body(returnWorkoutExerciseDto);
    }

    @GetMapping
    public ResponseEntity<Page<ReturnWorkoutExerciseDto>> getAllWorkoutExercise(@PageableDefault(sort = {"id"}) Pageable pageable) {
        Page<ReturnWorkoutExerciseDto> returnWorkoutExerciseDtos = workoutExerciseService.getAllWorkoutExercise(pageable);
        return ResponseEntity.ok(returnWorkoutExerciseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnWorkoutExerciseDto> findWorkoutExerciseById(@PathVariable String id){
        ReturnWorkoutExerciseDto returnWorkoutExerciseDto = workoutExerciseService.findWorkoutExerciseById(id);
        return ResponseEntity.ok(returnWorkoutExerciseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnWorkoutExerciseDto> updateWorkoutExercise(@PathVariable String id, @RequestBody UpdateWorkoutExerciseDto updateWorkoutExerciseDto){
        ReturnWorkoutExerciseDto returnWorkoutExerciseDto = workoutExerciseService.updateWorkoutExercise(updateWorkoutExerciseDto,id);
        return ResponseEntity.ok(returnWorkoutExerciseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkoutExercise(@PathVariable String id){
        workoutExerciseService.deleteWorkoutExercise(id);
        return ResponseEntity.noContent().build();
    }

}
