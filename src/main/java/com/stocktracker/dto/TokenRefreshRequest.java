package com.stocktracker.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TokenRefreshRequest {
    @NotBlank(message = "Refresh token cannot be blank")
    private String refreshToken;
}
