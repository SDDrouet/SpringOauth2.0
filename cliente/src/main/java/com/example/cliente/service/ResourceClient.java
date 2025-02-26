package com.example.cliente.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class ResourceClient {

    private final WebClient webClient;
    private final OAuth2AuthorizedClientService clientService;
    private final String resourceServerUrl;

    public ResourceClient(
            WebClient.Builder webClientBuilder,
            OAuth2AuthorizedClientService clientService,
            @Value("${resource.server.url}") String resourceServerUrl) {
        this.webClient = webClientBuilder.build();
        this.clientService = clientService;
        this.resourceServerUrl = resourceServerUrl;
    }

    public Map getMessages(Authentication authentication) {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        return webClient.get()
                .uri(resourceServerUrl + "/api/messages")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map getPublicMessage() {
        return webClient.get()
                .uri(resourceServerUrl + "/api/public/message")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
