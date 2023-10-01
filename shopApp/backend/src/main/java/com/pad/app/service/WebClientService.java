package com.pad.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebClientService {

    private final WebClient.Builder webClient;


    public <T> T webClientGet(String uri, Class<T> clazz) {
        T response = webClient.build().get()
                .uri(uri)
//                .header()
                .retrieve()
                .bodyToMono(clazz).block();
        log.debug("web client get response: {}", response);
        return response;
    }

    public <T, V> T webClientPost(String uri, Class<T> clazz, V body) {
        log.debug("web client post request body: {}", body);
        T response = webClient.build().post()
                .uri(uri)
//                .header()
                .body(Mono.just(body), body.getClass())
                .retrieve()
                .bodyToMono(clazz).block();
        log.debug("web client post response: {}", response);
        return response;
    }
}
