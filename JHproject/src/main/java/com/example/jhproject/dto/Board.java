package com.example.jhproject.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Board {
    private int boardId;
    private String title;
    private String content;
    private String name;
    private int userId;
    private LocalDateTime regdate;
    private int viewCnt;
}
