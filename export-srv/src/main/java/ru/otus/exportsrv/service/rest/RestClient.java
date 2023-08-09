package ru.otus.exportsrv.service.rest;

import org.springframework.http.*;
import org.springframework.util.MultiValueMap;

public interface RestClient {
    ResponseEntity<String> sendGet(String url);

    ResponseEntity<String> sendGet(String url, MultiValueMap<String, String> headers);

    ResponseEntity<String> sendPost(String url, Object body);

    ResponseEntity<String> sendPost(String url, Object body, MultiValueMap<String, String> headers);

    ResponseEntity<String> sendPut(String url, Object body);

    ResponseEntity<String> sendPut(String url, Object body, MultiValueMap<String, String> headers);

    ResponseEntity<String> sendPatch(String url, Object body);

    ResponseEntity<String> sendPatch(String url, Object body, MultiValueMap<String, String> headers);

    ResponseEntity<String> sendDelete(String url);

    ResponseEntity<String> sendDelete(String url, MultiValueMap<String, String> headers);
}
