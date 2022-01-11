//package com.stocktracker.security;
//
//import com.stocktracker.exception.StockTrackerException;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//import java.io.InputStream;
//import java.security.*;
//import java.security.cert.CertificateException;
//
//import static io.jsonwebtoken.Jwts.parser;
//
//@Service
//public class JwtProvider {
//
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
//
//    private PrivateKey getPrivateKey() {
//        try {
//            return (PrivateKey) keyStore.getKey("stocktracker", "password".toCharArray());
//        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
//            throw new StockTrackerException("Exception occured while retrieving public key from keystore");
//        }
//    }
//
//    public String generateToken(Authentication authentication) {
//        org.springframework.security.core.userdetails.User principal = (User) authentication.getPrincipal();
//        return Jwts.builder()
//                .setSubject(principal.getUsername())
//                .signWith(getPrivateKey())
//                .compact();
//    }
//
//    public boolean validateToken(String jwt) {
//        parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
//        return true;
//    }
//
//    private PublicKey getPublicKey() {
//        try {
//            return keyStore.getCertificate("stocktracker").getPublicKey();
//        } catch (KeyStoreException e) {
//            throw new StockTrackerException("Exception occured while retrieving public key from keystore");
//        }
//    }
//
//    public String getUsernameFromJWT(String token) {
//        Claims claims = parser()
//                .setSigningKey(getPublicKey())
//                .parseClaimsJws(token)
//                .getBody();
//
//        return claims.getSubject();
//    }
//}
