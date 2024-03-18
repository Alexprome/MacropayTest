package com.alexmacro.bookinfo.controllers;

import com.alexmacro.bookinfo.service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService){
        this.tokenService=tokenService;
    }

    @PostMapping("/auth")
    public String token(Authentication authentication) {
        return tokenService.tokenGenerator(authentication);
    }

}
