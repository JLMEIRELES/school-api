package com.example.school.controller;

import com.example.school.entity.User;
import com.example.school.records.AuthenticationData;
import com.example.school.records.JWTTokenData;
import com.example.school.service.JWTService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @PostMapping
    public ResponseEntity<JWTTokenData> login(@RequestBody @Valid AuthenticationData data){
        var token = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var authentication = authenticationManager.authenticate(token);
        var jwtToken = jwtService.generateJWT((User) authentication.getPrincipal());

        return ResponseEntity.ok(new JWTTokenData(jwtToken));
    }
}
