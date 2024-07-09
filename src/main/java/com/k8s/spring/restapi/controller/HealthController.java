package com.k8s.spring.restapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class HealthController {

    @GetMapping("/started")
    public ResponseEntity<HttpStatus> isStarted() {
        log.info("I'm started");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/ready")
    public ResponseEntity<HttpStatus> isReady() {
        log.info("I'm ready");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/alive")
    public ResponseEntity<HttpStatus> isAlive() {
        log.info("I'm alive");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
