package com.example.rqchallenge.employees.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class RestTemplateExecutor {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public RestTemplateExecutor(@Value("${base.url}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    public <T> Optional<T> execute(String resourcePath, HttpMethod method, Object request, Class<T> responseType) {
        String url = baseUrl + resourcePath;
        switch (method) {
            case GET:
                return Optional.ofNullable(restTemplate.getForObject(url, responseType));
            case POST:
                HttpEntity<Object> httpEntity = new HttpEntity<>(request);
                return Optional.ofNullable(restTemplate.postForObject(url, httpEntity, responseType));
            case PUT:
                HttpEntity<Object> httpEntityPut = new HttpEntity<>(request);
                return Optional.ofNullable(restTemplate.exchange(url, HttpMethod.PUT, httpEntityPut, responseType).getBody());
            case DELETE:
                restTemplate.delete(url, responseType);
                return Optional.empty();
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }
}

