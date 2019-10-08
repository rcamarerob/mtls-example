package com.rcb.mtls.poc.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {

    @GetMapping("/server")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("MTLS is working! \n");
    }
}
