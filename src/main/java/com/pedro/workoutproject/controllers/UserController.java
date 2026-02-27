package com.pedro.workoutproject.controllers;

import com.pedro.workoutproject.dtos.bodyWeightDtos.ReturnBodyWeightStatisticDto;
import com.pedro.workoutproject.dtos.userDtos.CreateUserDto;
import com.pedro.workoutproject.dtos.userDtos.ReturnUserDto;
import com.pedro.workoutproject.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ReturnUserDto> createUser(@Valid  @RequestBody CreateUserDto createUserDto, UriComponentsBuilder uri) {
        ReturnUserDto returnUserDto = userService.createUser(createUserDto);
        URI location = uri.path("/id")
                .buildAndExpand(returnUserDto.id())
                .toUri();
        return ResponseEntity.created(location).body(returnUserDto);
    }

    @GetMapping
    public ResponseEntity<Page<ReturnUserDto>> getAllUsers(@PageableDefault(sort = {"id"}) Pageable pageable) {
        Page<ReturnUserDto> returnUserDtos = userService.getAllUsers(pageable);
        return ResponseEntity.ok(returnUserDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnUserDto> getUserById(@PathVariable String id) {
        ReturnUserDto returnUserDto = userService.getUserById(id);
        return ResponseEntity.ok(returnUserDto);
    }

    @GetMapping("/token")
    public ResponseEntity<ReturnUserDto> getUserByTokenJWT(@RequestHeader(name = "Authorization") String token) {
        ReturnUserDto returnUserDto = userService.getUserByTokenJWT(token.replace("Bearer ", ""));
        return ResponseEntity.ok(returnUserDto);
    }

    @GetMapping("/statistics/{id}")
    public ResponseEntity<ReturnBodyWeightStatisticDto> getUserWeightStatistics(@RequestParam(required = false,defaultValue = "1") Integer interval,@PathVariable String id){
        ReturnBodyWeightStatisticDto statisticDto = userService.getUserWeightStatistics(id,interval);
        return ResponseEntity.ok(statisticDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnUserDto> updateUser(@PathVariable String id,@Valid @RequestBody CreateUserDto createUserDto) {
        ReturnUserDto returnUserDto = userService.updateUser(createUserDto, id);
        return ResponseEntity.ok(returnUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserDto(@PathVariable String id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
