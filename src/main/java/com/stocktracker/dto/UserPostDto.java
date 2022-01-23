package com.stocktracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPostDto {
    @NotNull
    @JsonProperty("username")
    private String username;
    @NotNull
    @JsonProperty("password")
    private String password;
    @NotNull
    @JsonProperty("name")
    private String name;
}
