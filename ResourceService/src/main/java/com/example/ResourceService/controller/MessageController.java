package com.example.ResourceService.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MessageController {

    @GetMapping("/public/message")
    public Map<String, String> publicMessage() {
        return Collections.singletonMap("message", "Este es un mensaje público");
    }

    @GetMapping("/messages")
    public Map<String, Object> getMessages(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
                "message", "Este es un mensaje privado",
                "sub", jwt.getSubject(),
                "scope", jwt.getClaim("scope")
        );
    }
}
