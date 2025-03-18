package com.mybank.mybankapi.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RestClientService {

    private final RestClient restClient;

    public <T> ResponseEntity<T> get(String baseUrl, String methodUrl, Map<String, String> uriVariables,
                                     ParameterizedTypeReference<T> responseType) {
        return restClient.get()
                .uri(baseUrl + methodUrl, uriVariables)
                .retrieve()
                .toEntity(responseType);
    }

    public <T, R> ResponseEntity<R> post(String baseUrl, String methodUrl, T requestBody,
                                         ParameterizedTypeReference<R> responseType) {
        return restClient.post()
                .uri(baseUrl + methodUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toEntity(responseType);
    }
}
