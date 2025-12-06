package com.pedro.workoutproject.controllers;

import com.pedro.workoutproject.dtos.bodyWeightDtos.CreateBodyWeightDto;
import com.pedro.workoutproject.dtos.bodyWeightDtos.ReturnBodyWeightDto;
import com.pedro.workoutproject.dtos.bodyWeightDtos.UpdateBodyWeightDto;
import com.pedro.workoutproject.services.BodyWeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/weight")
public class BodyWeightController {

    @Autowired
    private BodyWeightService bodyWeightService;

    @PostMapping
    public ResponseEntity<ReturnBodyWeightDto> createBodyWeight(@RequestBody CreateBodyWeightDto createBodyWeightDto, UriComponentsBuilder uri) {
        ReturnBodyWeightDto returnBodyWeightDto = bodyWeightService.createBodyWeight(createBodyWeightDto);
        URI location = uri.path("/id")
                .buildAndExpand(returnBodyWeightDto.id())
                .toUri();
        return ResponseEntity.created(location).body(returnBodyWeightDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnBodyWeightDto> getBodyWeightById(@PathVariable String id) {
        ReturnBodyWeightDto returnBodyWeightDto = bodyWeightService.getBodyWeightById(id);
        return ResponseEntity.ok(returnBodyWeightDto);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Page<ReturnBodyWeightDto>> getBodyWeightDtoByUserId(@PathVariable String id, @PageableDefault(sort = {"id"}) Pageable pageable) {
        Page<ReturnBodyWeightDto> returnBodyWeightDto = bodyWeightService.getBodyWeightByUserId(id, pageable);
        return ResponseEntity.ok(returnBodyWeightDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnBodyWeightDto> updateBodyWeight(@PathVariable String id, @RequestBody UpdateBodyWeightDto updateBodyWeightDto) {
        ReturnBodyWeightDto returnBodyWeightDto = bodyWeightService.updateBodyWeight(id, updateBodyWeightDto);
        return ResponseEntity.ok(returnBodyWeightDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBodyWeight(@PathVariable String id) {
        bodyWeightService.deleteBodyWeight(id);
        return ResponseEntity.noContent().build();
    }


}
