package com.stocktracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id @GeneratedValue(strategy = AUTO)
    private Long userId;
    @Email
    //@NotBlank(message = "Email is required")
    @Column(unique=true)
    private String username;
    //@NotBlank(message = "Password is required")
    private String password;
    //@NotEmpty(message = "Name is required")
    private String name;
    private Instant createdAt;
    private Boolean enabled;
    private Boolean refreshActive;

    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }
}
