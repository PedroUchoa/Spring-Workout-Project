package com.pedro.workoutproject.controllers;

import com.pedro.workoutproject.dtos.authenticationDtos.AuthenticationDataDto;
import com.pedro.workoutproject.dtos.authenticationDtos.DataTokenJWT;
import com.pedro.workoutproject.dtos.authenticationDtos.RefreshTokenJWT;
import com.pedro.workoutproject.models.User;
import com.pedro.workoutproject.services.AuthenticationService;
import com.pedro.workoutproject.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<DataTokenJWT> makeLogin(@RequestBody AuthenticationDataDto data) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        authenticationManager.authenticate(authenticationToken);
        DataTokenJWT dataTokenJWT = authenticationService.obtainToken(data);
        return ResponseEntity.ok(dataTokenJWT);

    }


    @PostMapping("/refresh")
    public ResponseEntity<DataTokenJWT> authRefreshToken(@RequestBody RefreshTokenJWT data) {
        DataTokenJWT dataTokenJWT = authenticationService.obtainRefreshToken(data.refreshToken());
        return ResponseEntity.ok(dataTokenJWT);
    }


}
