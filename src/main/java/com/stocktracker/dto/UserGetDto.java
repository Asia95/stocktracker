package com.stocktracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGetDto {
    @NotNull
    @JsonProperty("username")
    private String username;
    @NotNull
    @JsonProperty("name")
    private String name;
}
