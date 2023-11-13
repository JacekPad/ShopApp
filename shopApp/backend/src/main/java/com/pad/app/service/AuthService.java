package com.pad.app.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class AuthService {
    private final OAuth2AuthorizedClientManager authManager;

    public void securityTest(HttpServletRequest request, HttpServletResponse response) {
        log.info("AuthService START");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("keycloak")
                .principal(authentication)
                .attributes(attr -> {
                    attr.put(HttpServletRequest.class.getName(), request);
                    attr.put(HttpServletResponse.class.getName(), response);
                })
                .build();
        OAuth2AuthorizedClient authorizedClient = authManager.authorize(authorizeRequest);
        log.info("TOKEN:");
        log.info(authorizedClient.getAccessToken().getTokenValue());
    }


}
