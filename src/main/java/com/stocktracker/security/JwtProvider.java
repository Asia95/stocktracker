package com.stocktracker.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;

//import static io.jsonwebtoken.Jwts.parser;

@Service
@Slf4j
public class JwtProvider {

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
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 525600 * 60 * 1000))
                //.withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        return access_token;
    }

    public String generateTokenFromUser(com.stocktracker.model.User u) {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String access_token = JWT.create()
                .withSubject(u.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 525600 * 60 * 1000))
                //.withIssuer(request.getRequestURL().toString())
                .withClaim("roles", u.getRoles().stream().collect(Collectors.toList()))
                .sign(algorithm);
        return access_token;
    }

    public Long getExpiryDuration() {
        return System.currentTimeMillis() + 525600 * 60 * 1000;
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

//    public String getUsernameFromJWT(String token) {
//        Claims claims = parser()
//                .setSigningKey(getPublicKey())
//                .parseClaimsJws(token)
//                .getBody();
//
//        return claims.getSubject();
//    }
}
