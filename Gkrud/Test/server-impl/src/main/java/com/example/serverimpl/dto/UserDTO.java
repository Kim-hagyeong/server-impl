package com.example.serverimpl.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createdAt;
}
