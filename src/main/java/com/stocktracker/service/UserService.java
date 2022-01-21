package com.stocktracker.service;

import com.stocktracker.model.Role;
import com.stocktracker.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    Role getRole(String role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getUsers();
    //void signup(RegisterRequest request);
}
