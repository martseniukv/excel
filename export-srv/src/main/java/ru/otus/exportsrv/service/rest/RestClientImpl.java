package ru.otus.exportsrv.service.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestClientImpl {

    private final RestTemplate restTemplate;

    //@Override
    public <T> T sendGet(String url, ParameterizedTypeReference<T> paramType) {
        return sendRequest(url, HttpMethod.GET, null, new LinkedMultiValueMap<>(), paramType);
    }

    //@Override
    public <T> T sendGet(String url, MultiValueMap<String, String> headers, ParameterizedTypeReference<T> paramType) {
        return sendRequest(url, HttpMethod.GET, null, headers, paramType);
    }

    //@Override
    public <T> T sendPost(String url, Object body,  ParameterizedTypeReference<T> paramType) {
        return sendRequest(url, HttpMethod.POST, body, new LinkedMultiValueMap<>(), paramType);
    }

    //@Override
    public ResponseEntity<String> sendPost(String url, Object body, MultiValueMap<String, String> headers) {
        return sendRequest(url, HttpMethod.POST, body, headers);
    }

    //@Override
    public ResponseEntity<String> sendPut(String url, Object body) {
        return sendPut(url, body, new LinkedMultiValueMap<>());
    }

    //@Override
    public ResponseEntity<String> sendPut(String url, Object body, MultiValueMap<String, String> headers) {
        return sendRequest(url, HttpMethod.PUT, body, headers);
    }

    //@Override
    public ResponseEntity<String> sendPatch(String url, Object body) {
        return sendPatch(url, body, new LinkedMultiValueMap<>());
    }

    //@Override
    public ResponseEntity<String> sendPatch(String url, Object body, MultiValueMap<String, String> headers) {
        return sendRequest(url, HttpMethod.PATCH, body, headers);
    }

    //@Override
    public ResponseEntity<String> sendDelete(String url) {
        return sendDelete(url, new LinkedMultiValueMap<>());
    }

    //@Override
    public ResponseEntity<String> sendDelete(String url, MultiValueMap<String, String> headers) {
        return sendRequest(url, HttpMethod.DELETE, null, headers);
    }

    private <T> T sendRequest(String url, HttpMethod httpMethod, Object body, MultiValueMap<String, String> headers, ParameterizedTypeReference<T> paramType) {

        var httpHeaders = new HttpHeaders(headers);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        var requestEntity = new HttpEntity<>(body, httpHeaders);
        return restTemplate.exchange(url, httpMethod, requestEntity, paramType).getBody();
    }

    private ResponseEntity<String> sendRequest(String url, HttpMethod httpMethod, Object body, MultiValueMap<String, String> headers) {

        var httpHeaders = new HttpHeaders(headers);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        var requestEntity = new HttpEntity<>(body, httpHeaders);

        try {
            return restTemplate.exchange(url, httpMethod, requestEntity, new ParameterizedTypeReference<>() {});
        } catch (RestClientResponseException e) {
            RestClientImpl.log.error("Rest client exception during send request.", e);
            return ResponseEntity
                    .status(e.getRawStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString());
        } catch (Exception e) {
            RestClientImpl.log.error("Unexpected exception during send request.", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
