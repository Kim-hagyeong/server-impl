package com.example.serverimpl.Controller;

import com.example.serverimpl.dto.UserDTO;
import com.example.serverimpl.dto.UserLoginDTO;
import com.example.serverimpl.dto.UserUpdateDTO;
import com.example.serverimpl.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired private UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO u) {
        if (userMapper.findByUsername(u.getUsername()) != null)
            return ResponseEntity.badRequest().body("이미 존재하는 아이디");
        userMapper.create(u);
        return ResponseEntity.ok(u.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO dto,
                                   HttpSession session) {
        UserDTO u = userMapper.findByUsername(dto.getUsername());
        if (u==null || !u.getPassword().equals(dto.getPassword()))
            return ResponseEntity.status(401).body("로그인 실패");
        session.setAttribute("userId", u.getId());
        return ResponseEntity.ok("로그인 성공");
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(HttpSession session) {
        Integer id = (Integer)session.getAttribute("userId");
        return ResponseEntity.ok(userMapper.findById(id));
    }

    @PutMapping("/me")
    public ResponseEntity<?> update(@RequestBody UserUpdateDTO dto,
                                    HttpSession session) {
        Integer id = (Integer)session.getAttribute("userId");
        UserDTO u = new UserDTO();
        u.setId(id);
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());
        userMapper.update(u);
        return ResponseEntity.ok("수정 완료");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃");
    }
}
