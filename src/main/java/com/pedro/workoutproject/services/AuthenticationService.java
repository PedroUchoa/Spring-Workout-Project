package com.pedro.workoutproject.services;

import com.pedro.workoutproject.dtos.authenticationDtos.AuthenticationDataDto;
import com.pedro.workoutproject.dtos.authenticationDtos.DataTokenJWT;
import com.pedro.workoutproject.infra.Exceptions.userExceptions.UserEmailNotFoundException;
import com.pedro.workoutproject.models.User;
import com.pedro.workoutproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()->new UserEmailNotFoundException(email));
    }

    public DataTokenJWT obtainToken(AuthenticationDataDto data){
        User user = userRepository.findByEmail(data.email()).orElseThrow(()->new UserEmailNotFoundException(data.email()));
        return new DataTokenJWT(tokenService.generateToken(user,1), tokenService.generateToken(user,8));
    }

    public DataTokenJWT obtainRefreshToken(String refreshToken) {
        String email = tokenService.getSubject(refreshToken);
        User user = userRepository.findByEmail(email).orElseThrow(()->new UserEmailNotFoundException(email));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new DataTokenJWT(tokenService.generateToken(user,1), tokenService.generateToken(user,8));

    }
}
