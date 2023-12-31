package com.pad.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityChain(HttpSecurity http) throws Exception{
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new OAuthRoleConverter());
        http
                .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requestManager -> {
                    //TODO - keycloak does not allow external URL as issuer when comparing it with internal url
                    requestManager.requestMatchers("/*").permitAll();
//                    requestManager.requestMatchers("*/**").hasAnyRole("USER", "ADMIN")
//                            .anyRequest().authenticated();
                })
                .oauth2ResourceServer(oatuh -> oatuh.jwt(jwt -> {
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter);
                }));
        return http.build();
    }
//    ADD BEARER TO THE WEBCLIENT.BUILDER AUTOMATICALLY
    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager (ClientRegistrationRepository clientRegistrationRepository) {
        OAuth2AuthorizedClientService service = new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
        AuthorizedClientServiceOAuth2AuthorizedClientManager manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, service);
        OAuth2AuthorizedClientProvider auth2AuthorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials().build();
        manager.setAuthorizedClientProvider(auth2AuthorizedClientProvider);
        return manager;
    }
    @Bean
    WebClient.Builder webClient(OAuth2AuthorizedClientManager manager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(manager);
        oauth2.setDefaultClientRegistrationId("keycloak");
        return WebClient.builder().apply(oauth2.oauth2Configuration());
    }


}
