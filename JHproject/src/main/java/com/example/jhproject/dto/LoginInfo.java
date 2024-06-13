package com.example.jhproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter

public class LoginInfo {
    private int userId;
    private String email;
    private String name;
    private List<String> roles = new ArrayList<>();

    public LoginInfo(int userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
    }

    public void addRole(String roleName) {
        roles.add(roleName);
    }
}
