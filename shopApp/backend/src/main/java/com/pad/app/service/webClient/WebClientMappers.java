package com.pad.app.service.webClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Component
public class WebClientMappers {

    public MultiValueMap<String, String> convertToUriParams(Object o) {
        ObjectMapper objectMapper = new ObjectMapper();
        LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        Map<String, String> map = objectMapper.convertValue(o, new TypeReference<>() {});
        map.entrySet().stream().filter(e -> e.getValue() != null).forEach(e -> multiValueMap.add(e.getKey(), e.getValue()));
        return multiValueMap;
    }
}
