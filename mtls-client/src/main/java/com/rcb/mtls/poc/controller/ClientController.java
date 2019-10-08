package com.rcb.mtls.poc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ClientController {

    private RestTemplate restTemplate;

    static final String URL= "https://localhost:8443/server";

    @Autowired
    public ClientController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/mtls")
    public ResponseEntity<String> mtls() {
        return restTemplate.getForEntity(URL, String.class);
    }
}
