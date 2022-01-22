package com.stocktracker.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.stocktracker.cache.LoggedOutJwtTokenCache;
import com.stocktracker.event.OnUserLogoutSuccessEvent;
import com.stocktracker.exception.StockTrackerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtProvider {

    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

    @Autowired
    private LoggedOutJwtTokenCache loggedOutJwtTokenCache;


//    private KeyStore keyStore;
//
//    @PostConstruct
//    public void init() {
//        try {
//            keyStore = KeyStore.getInstance("JKS");
//            InputStream resourceAsStream = getClass().getResourceAsStream("/stocktracker.jks");
//            keyStore.load(resourceAsStream, "password".toCharArray());
//        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
//            throw new StockTrackerException("Exception occurred while loading keystore");
//        }
//
//    }

//    private PrivateKey getPrivateKey() {
//        try {
//            return (PrivateKey) keyStore.getKey("stocktracker", "password".toCharArray());
//        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
//            throw new StockTrackerException("Exception occured while retrieving public key from keystore");
//        }
//    }

    public String generateJwtToken(Authentication authentication) {
        User user = (User)authentication.getPrincipal();
        log.info("username for token: {}", user.getUsername());

        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(getExpiry()))
                .withIssuer("StockTracker")
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        return access_token;
    }

    public String generateTokenFromUser(com.stocktracker.model.User u) {
        String access_token = JWT.create()
                .withSubject(u.getUsername())
                .withExpiresAt(new Date(getExpiry()))
                .withIssuer("StockTracker")
                .withClaim("roles", u.getRoles().stream().collect(Collectors.toList()))
                .sign(algorithm);
        return access_token;
    }

    public Long getExpiry() {
        return System.currentTimeMillis() + 525600 * 60 * 1000;
    }

    public long getExpiryDuration() {
        return 525600 * 60 * 1000;
    }

//    public boolean validateToken(String jwt) {
//        parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
//        return true;
//    }

//    private PublicKey getPublicKey() {
//        try {
//            return keyStore.getCertificate("stocktracker").getPublicKey();
//        } catch (KeyStoreException e) {
//            throw new StockTrackerException("Exception occured while retrieving public key from keystore");
//        }
//    }

    public String getUsernameFromJWT(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }

    public Date getTokenExpiryFromJWT(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getExpiresAt();
    }

    public boolean validateJwtToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            validateTokenIsNotForALoggedOutDevice(token);
            return true;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty -> Message: {}", e);
        }

        return false;
    }

    private void validateTokenIsNotForALoggedOutDevice(String authToken) {
        OnUserLogoutSuccessEvent previouslyLoggedOutEvent = loggedOutJwtTokenCache.getLogoutEventForToken(authToken);
        if (previouslyLoggedOutEvent != null) {
            String userEmail = previouslyLoggedOutEvent.getUserEmail();
            Date logoutEventDate = previouslyLoggedOutEvent.getEventTime();
            String errorMessage = String.format("Token corresponds to an already logged out user [%s] at [%s]. Please login again", userEmail, logoutEventDate);
            throw new StockTrackerException(String.format("JWT [%s]", errorMessage));
        }
    }
}
