package com.stocktracker.controller;

import com.stocktracker.dto.RegisterRequest;
import com.stocktracker.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.IM_USED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody RegisterRequest registerRequest) {
        log.info("signup " + registerRequest.toString());
        try {
            authService.signup(registerRequest);
            log.info("signup " + registerRequest.toString());
        } catch (Exception e) {
            return new ResponseEntity<>("Email already registered", IM_USED);
        }
        return new ResponseEntity(OK);
    }

//    @PostMapping("/login")
//    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
//        return authService.login(loginRequest);
//    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testAccess() {
        return new ResponseEntity<>("Test", OK);
    }
}
