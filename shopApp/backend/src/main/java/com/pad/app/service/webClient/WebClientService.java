package com.pad.app.service.webClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
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

    public <T> T webClientGet(String uri, Class<T> clazz, MultiValueMap<String, String> params) {
        log.debug("web client get request, uri: {}, params: {}", uri, params);
        T response = webClient.baseUrl(uri).build().get()
                .uri(uriBuilder -> uriBuilder.queryParams(params).build())
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

    public <T> T webClientDelete(String uri, Class<T> clazz) {
        log.debug("web client delete request body");
        T response = webClient.build().delete()
                .uri(uri)
//                .header()
                .retrieve()
                .bodyToMono(clazz).block();
        log.debug("web client delete response: {}", response);
        return response;
    }
}
