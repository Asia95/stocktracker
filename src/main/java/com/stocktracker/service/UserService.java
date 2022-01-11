package com.stocktracker.service;

import com.stocktracker.model.Role;
import com.stocktracker.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String email, String roleName);
    User getUser(String email);
    List<User> getUsers();
    //void signup(RegisterRequest request);
}
