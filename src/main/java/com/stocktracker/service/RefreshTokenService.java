package com.stocktracker.service;

import com.stocktracker.exception.StockTrackerException;
import com.stocktracker.model.RefreshToken;
import com.stocktracker.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByUserUsername(String username) {
        return refreshTokenRepository.findByUserUsername(username);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().plusMillis(3600000));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setRefreshCount(0L);
        return refreshToken;
    }

    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new StockTrackerException("Expired token. Please issue a new request");
        }
    }

    public void increaseCount(RefreshToken refreshToken) {
        refreshToken.incrementRefreshCount();
        refreshTokenRepository.save(refreshToken);
    }

    public void deleteById(Long tokenId) {
        refreshTokenRepository.deleteById(tokenId);
    }

    public RefreshToken save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
}
