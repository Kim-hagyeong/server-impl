package com.example.serverimpl.Controller;

import com.example.serverimpl.dto.PostDTO;
import com.example.serverimpl.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired private PostMapper postMapper;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PostDTO p,
                                    HttpSession session) {
        p.setUserId((Integer)session.getAttribute("userId"));
        postMapper.create(p);
        return ResponseEntity.ok(p.getId());
    }

    @GetMapping
    public ResponseEntity<Map<String,Object>> list(
            @RequestParam(defaultValue="0") int page) {
        int offset = page * 10;
        List<PostDTO> list = postMapper.findAll(offset);
        int total = postMapper.count();
        Map<String,Object> res = new HashMap<>();
        res.put("posts", list);
        res.put("total", total);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> detail(@PathVariable int id) {
        return ResponseEntity.ok(postMapper.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id,
                                    @RequestBody PostDTO p,
                                    HttpSession session) {
        p.setId(id);
        p.setUserId((Integer)session.getAttribute("userId"));
        int cnt = postMapper.update(p);
        if (cnt==0) return ResponseEntity.status(403).body("권한 없음");
        return ResponseEntity.ok("수정됨");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id,
                                    HttpSession session) {
        int uid = (Integer)session.getAttribute("userId");
        int cnt = postMapper.delete(id, uid);
        if (cnt==0) return ResponseEntity.status(403).body("권한 없음");
        return ResponseEntity.ok("삭제됨");
    }
}
