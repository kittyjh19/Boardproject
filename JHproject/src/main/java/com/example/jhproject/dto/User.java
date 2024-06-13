package com.example.jhproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class User {
    private int userId;
    private String email;
    private String name;
    private String password;
    private LocalDateTime regdate;
}
