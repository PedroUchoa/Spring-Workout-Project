package com.pedro.workoutproject.services;

import com.auth0.jwt.JWT;
import com.pedro.workoutproject.dtos.userDtos.CreateUserDto;
import com.pedro.workoutproject.dtos.userDtos.ReturnUserDto;
import com.pedro.workoutproject.enumerated.Role;
import com.pedro.workoutproject.models.User;
import com.pedro.workoutproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public ReturnUserDto createUser(CreateUserDto createUserDto) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(createUserDto.password());
        User user = userRepository.save(new User(createUserDto.email(), encryptedPassword, Role.USER));
        return new ReturnUserDto(user);
    }

    public Page<ReturnUserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(ReturnUserDto::new);
    }

    public ReturnUserDto getUserById(String id) {
        User user = userRepository.getReferenceById(id);
        return new ReturnUserDto(user);
    }

    public ReturnUserDto getUserByTokenJWT(String token) {
        String id = JWT.decode(token).getClaim("id").asString();
        User user = userRepository.getReferenceById(id);
        return new ReturnUserDto(user);
    }

    public ReturnUserDto updateUser(CreateUserDto createUserDto, String id){
        User user = userRepository.getReferenceById(id);
        user.updateUser(createUserDto);
        userRepository.save(user);
        return new ReturnUserDto(user);
    }

    public void deleteUser(String id){
        User user = userRepository.getReferenceById(id);
        userRepository.delete(user);
    }

}
