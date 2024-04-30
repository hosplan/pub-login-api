package com.iuni.login.controller;

import com.iuni.login.domain.IuniToken;
import com.iuni.login.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/iunitoken")
public class JWTController {
    private final JWTService jwtService;

    @PostMapping()
    public IuniToken reissuance(@RequestBody IuniToken data){
        return jwtService.reissuance(data);
    }
}
