package com.pedro.workoutproject.controllers;

import com.pedro.workoutproject.dtos.workoutDtos.CreateWorkoutDto;
import com.pedro.workoutproject.dtos.workoutDtos.ReturnWorkoutDto;
import com.pedro.workoutproject.dtos.workoutDtos.UpdateWorkoutDto;
import com.pedro.workoutproject.services.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/workout")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @PostMapping
    public ResponseEntity<ReturnWorkoutDto> createWorkout(@RequestBody CreateWorkoutDto createWorkoutDto, UriComponentsBuilder uri) {
        ReturnWorkoutDto returnWorkoutDto = workoutService.createWorkout(createWorkoutDto);
        URI location = uri.path("/id").buildAndExpand(returnWorkoutDto.id()).toUri();
        return ResponseEntity.created(location).body(returnWorkoutDto);
    }

    @GetMapping
    public ResponseEntity<Page<ReturnWorkoutDto>> returnAllWorkout(@PageableDefault(sort = {"id"}) Pageable pageable) {
        Page<ReturnWorkoutDto> returnWorkoutDtoPage = workoutService.getAllWorkouts(pageable);
        return ResponseEntity.ok(returnWorkoutDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnWorkoutDto> returnWorkoutById(@PathVariable String id) {
        ReturnWorkoutDto returnWorkoutDto = workoutService.getWorkoutById(id);
        return ResponseEntity.ok(returnWorkoutDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnWorkoutDto> updateWorkout(@PathVariable String id, @RequestBody UpdateWorkoutDto updateWorkoutDto) {
        ReturnWorkoutDto returnWorkoutDto = workoutService.updateWorkoutDto(updateWorkoutDto, id);
        return ResponseEntity.ok(returnWorkoutDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable String id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }


}
