package com.stocktracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<String> testAccess() {
        return new ResponseEntity<>("Test", OK);
    }
}
