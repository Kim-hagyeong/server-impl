package com.example.serverimpl.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostDTO {
    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // 작성자 이름
    private String username;
}
