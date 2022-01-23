package com.stocktracker.controller;

import com.stocktracker.dto.UserPostDto;
import com.stocktracker.dto.mappers.MapStructMapper;
import com.stocktracker.model.User;
import com.stocktracker.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private MapStructMapper mapstructMapper;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserPostDto userPostDto) {
        log.info("signup " + userPostDto.toString());
        try {
            User user = mapstructMapper.userPostDtoToUser(userPostDto);
            log.info("User: " + mapstructMapper);
            authService.signup(mapstructMapper.userPostDtoToUser(userPostDto));
            log.info("signup " + userPostDto.toString());
        } catch (Exception e) {
            return new ResponseEntity<>("Email already registered", IM_USED);
        }
        return new ResponseEntity(CREATED);
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
