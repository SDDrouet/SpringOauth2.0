package com.example.cliente.controller;

import com.example.cliente.service.ResourceClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ResourceClient resourceClient;

    public HomeController(ResourceClient resourceClient) {
        this.resourceClient = resourceClient;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("publicMessage", resourceClient.getPublicMessage());
        return "home";
    }

    @GetMapping("/secured")
    public String secured(Model model, Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken token)) {
            return "redirect:/error"; // Redirigir en caso de error
        }
        model.addAttribute("userName", token.getPrincipal().getAttribute("sub"));
        model.addAttribute("clientName", token.getAuthorizedClientRegistrationId());
        model.addAttribute("userAttributes", token.getPrincipal().getAttributes());
        model.addAttribute("messages", resourceClient.getMessages(authentication));
        return "secured";
    }
}
