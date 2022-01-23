package com.stocktracker.controller;

import com.stocktracker.dto.AuthenticationResponse;
import com.stocktracker.dto.LogOutRequest;
import com.stocktracker.dto.TokenRefreshRequest;
import com.stocktracker.event.OnUserLogoutSuccessEvent;
import com.stocktracker.exception.StockTrackerException;
import com.stocktracker.model.RefreshToken;
import com.stocktracker.model.Role;
import com.stocktracker.model.User;
import com.stocktracker.security.JwtProvider;
import com.stocktracker.service.CurrentUser;
import com.stocktracker.service.RefreshTokenService;
import com.stocktracker.service.UserDetailsServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserDetailsServiceImpl userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getEmail(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

        Optional<String> token = Optional.of(refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshToken -> {
                    refreshTokenService.verifyExpiration(refreshToken);
                    userService.verifyRefreshAvailability(refreshToken);
                    refreshTokenService.increaseCount(refreshToken);
                    return refreshToken;
                })
                .map(RefreshToken::getUser)
                .map(u -> jwtProvider.generateTokenFromUser(u))
                .orElseThrow(() -> new StockTrackerException("Missing refresh token in database. Please login again")));
        return ResponseEntity.ok().body(new AuthenticationResponse(token.get(), tokenRefreshRequest.getRefreshToken(), jwtProvider.getExpiry()));
    }

    @PutMapping("/user/logout")
    public ResponseEntity<String> logoutUser(@CurrentUser User currentUser2,
                                                  @Valid @RequestBody LogOutRequest logOutRequest) {
        User currentUser = userService.getUser(jwtProvider.getUsernameFromJWT(logOutRequest.getToken()));
        RefreshToken token = refreshTokenService.findByUserUsername(currentUser.getUsername())
                .orElseThrow(() -> new StockTrackerException("No refresh token found for the user"));
        refreshTokenService.deleteById(token.getId());

        OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(currentUser.getUsername(), logOutRequest.getToken(), logOutRequest);
        applicationEventPublisher.publishEvent(logoutSuccessEvent);
        return ResponseEntity.ok().body("User has successfully logged out from the system!");
    }

    @GetMapping("/user/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok().body("access successful");
    }
}

@Data
class RoleToUserForm {
    private String email;
    private String roleName;
}

