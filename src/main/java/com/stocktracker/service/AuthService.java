package com.stocktracker.service;

import com.stocktracker.exception.StockTrackerException;
import com.stocktracker.model.NotificationEmail;
import com.stocktracker.model.Role;
import com.stocktracker.model.User;
import com.stocktracker.model.VerificationToken;
import com.stocktracker.repository.RoleRepository;
import com.stocktracker.repository.UserRepository;
import com.stocktracker.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static com.stocktracker.util.Constants.ACTIVATION_EMAIL;
import static java.time.Instant.now;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    @Transactional
    public void signup(User user) {
        user.setPassword(encodePassword(user.getPassword()));
        user.setCreatedAt(now());
        user.setEnabled(false);

        Role role = roleRepository.findByName("ROLE_USER");
        user.setRoles(new ArrayList<Role>() {{ add(role); }});

        log.info("registering...");
        userRepository.save(user);
        log.info("saved!!!!!");

        String token = generateVerificationToken(user);
        String message = mailContentBuilder.build("Thank you for signing up to Stock Tracker," +
                " please click on the below url to activate your account : " +
                ACTIVATION_EMAIL + "/" + token);
        log.info(String.format("Sending email to: [%s]", user.getUsername()));
        mailService.sendMail(new NotificationEmail("Activate your account", user.getUsername(), message));
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

//    public AuthenticationResponse login(LoginRequest loginRequest) {
//        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserEmail(),
//                loginRequest.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authenticate);
//        String authenticationToken = jwtProvider.generateToken(authenticate);
//        return new AuthenticationResponse(authenticationToken, loginRequest.getUserEmail());
//    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(() -> new StockTrackerException("Invalid Token"));
        fetchUserAndEnable(verificationTokenOptional.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String userEmail = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(userEmail).orElseThrow(() ->
                new StockTrackerException("User Not Found : " + userEmail));
        user.enable();
        userRepository.save(user);
    }
}
