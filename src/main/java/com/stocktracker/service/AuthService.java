package com.stocktracker.service;

import com.stocktracker.dto.AuthenticationResponse;
import com.stocktracker.dto.LoginRequest;
import com.stocktracker.dto.RegisterRequest;
import com.stocktracker.exception.StockTrackerException;
import com.stocktracker.model.NotificationEmail;
import com.stocktracker.model.User;
import com.stocktracker.model.VerificationToken;
import com.stocktracker.repository.UserRepository;
import com.stocktracker.repository.VerificationTokenRepository;
import com.stocktracker.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.stocktracker.util.Constants.ACTIVATION_EMAIL;
import static java.time.Instant.now;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setCreatedAt(now());
        user.setEnabled(false);
        log.info("registering...");
        userRepository.save(user);
        log.info("saved!!!!!");

        String token = generateVerificationToken(user);
        String message = mailContentBuilder.build("Thank you for signing up to Stock Tracker," +
                " please click on the below url to activate your account : " +
                ACTIVATION_EMAIL + "/" + token);

        mailService.sendMail(new NotificationEmail("Activate your account", user.getEmail(), message));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserEmail(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String authenticationToken = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(authenticationToken, loginRequest.getUserEmail());
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(() -> new StockTrackerException("Invalid Token"));
        fetchUserAndEnable(verificationTokenOptional.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String userEmail = verificationToken.getUser().getEmail();
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new StockTrackerException("User Not Found : " + userEmail));
        user.setEnabled(true);
        userRepository.save(user);
    }
}
